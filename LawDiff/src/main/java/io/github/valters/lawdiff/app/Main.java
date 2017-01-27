package io.github.valters.lawdiff.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

import org.outerj.daisy.diff.DaisyDiff;

import io.github.valters.lawdiff.HtmlContentOutput;
import io.github.valters.lawdiff.HtmlContentOutput.OutputMode;

public class Main {

    public static void main( final String[] args ) {
        try {
            if( args.length == 2 || args.length == 3 ) {
                new App(args).run( args[0], args[1] );
            }
            else {
                usage();
            }

        }
        catch( final Exception e ) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println( "Usage: lawdiff-app <law folder> <diff output folder> [--xml] [--debug]" );
        System.out.println( "  By default HTML diff report is produced. Use '--xml' flag to output machine-readable XML." );
        System.out.println( "  If running with '--debug' flag additional diff will be produced using another/competing algorithm." );
    }

    public static class App {

        private final boolean xmlMode;
        private final boolean debugMode;

        public App( final String[] args ) {
            if( args.length == 3 ) {
                debugMode = checkDebug( args[2] );
                xmlMode = checkXmlMode( args[2] );
            } else {
                xmlMode = false;
                debugMode = false;
            }
        }

        private boolean checkDebug( final String option ) {
            if( "--debug".equals( option ) ) {
                System.out.println( ". running in debug mode" );
                return true;
            }
            else {
                return false;
            }
        }

        private boolean checkXmlMode( final String option ) {
            if( "--xml".equals( option ) ) {
                System.out.println( ". xml mode" );
                return true;
            }
            else {
                return false;
            }
        }

        public void run( final String inDirArg, final String outDirArg ) throws Exception {
            System.out.println( "Generating law diffs: "+inDirArg+" -> "+outDirArg+"..." );

            final Path inDir = FileSystems.getDefault().getPath( inDirArg );
            if( ! Files.isDirectory( inDir ) ) {
                throw new RuntimeException("Error, folder '"+inDirArg+"' must exist.");
            }

            final Path outDir = FileSystems.getDefault().getPath( outDirArg );
            if( ! Files.exists( outDir ) ) {
                Files.createDirectory( outDir );
                System.out.println(" . created output folder: "+outDir );
            }

            final ArrayList<Path> files = new ArrayList<>();
            try( DirectoryStream<Path> stream = Files.newDirectoryStream( inDir, "*.txt" ) ) {
                for( final Path file : stream ) {
                    files.add( file );
                }
            }

            Collections.sort( files );

            for( int i = 1; i < files.size(); i++ ) {
                generateDiff( files.get( i - 1 ), files.get( i ), outDir );
            }

        }

        private void generateDiff( final Path fileA, final Path fileB, final Path outDir ) throws Exception {
            if( debugMode ) {
                generateDaisyDiff( fileA, fileB, outDir );
            }
            generateHistogramDiff( fileA, fileB, outDir );
        }

        private void generateDaisyDiff( final Path fileA, final Path fileB, final Path outDir ) throws Exception {
            final String diffName = fileB.getFileName()+"-diff-d.html";
            System.out.println("diff: "+fileA+" : "+fileB+" -> "+diffName );

            final OutputMode outputMode = xmlMode ? OutputMode.XML :OutputMode.HTML;
            final HtmlContentOutput out = HtmlContentOutput.startOutput( new File( outDir.toFile(), diffName ), outputMode );

            try( BufferedReader brA = Files.newBufferedReader( fileA, StandardCharsets.UTF_8 );
                 BufferedReader brB = Files.newBufferedReader( fileB, StandardCharsets.UTF_8 ) ) {

                DaisyDiff.diffTag( brA, brB, out.getHandler() );
            }

            out.finishOutput();
        }

        private void generateHistogramDiff( final Path fileA, final Path fileB, final Path outDir ) throws Exception {

            final OutputMode outputMode = xmlMode ? OutputMode.XML :OutputMode.HTML;
            final String ext = xmlMode ? ".xml" : ".html";

            final String diffName = fileB.getFileName()+"-diff"+ext;
            System.out.println("diff: "+fileA+" : "+fileB+" -> "+diffName );

            final HtmlContentOutput out = HtmlContentOutput.startOutput( new File( outDir.toFile(), diffName ), outputMode );

            try( InputStream brA = new FileInputStream( fileA.toFile() );
                    InputStream brB = new FileInputStream( fileB.toFile()  ) ) {

                   DaisyDiff.diffHistogramRaw( brA, brB, out.getHandler() );
               }

            out.finishOutput();
        }

    }
}
