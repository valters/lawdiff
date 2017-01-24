package io.github.valters.lawdiff;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Ignore;
import org.junit.Test;
import org.outerj.daisy.diff.DaisyDiff;

import io.github.valters.lawdiff.HtmlContentOutput.OutputMode;

public class TestDiff {

    @Ignore
    @Test
    public void shouldDiffDaisy() throws Exception {

        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20030605.html.txt" );
        assertThat( old, notNullValue() );
        final InputStream test = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20041021.html.txt" );
        assertThat( test, notNullValue() );

        final HtmlContentOutput out = HtmlContentOutput.startOutput( new File("./target/test-d.html" ), OutputMode.XML );

        DaisyDiff.diffTag( new BufferedReader( new InputStreamReader( old ) ), new BufferedReader( new InputStreamReader( test ) ), out.getHandler() );

        out.finishOutput();
    }

    @Ignore
    @Test
    public void shouldDiffHistogram() throws Exception {

        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20030605.html.txt" );
        assertThat( old, notNullValue() );
//        final InputStream test = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20041021.html.txt" );
//        assertThat( test, notNullValue() );

//        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20030605.html.txt" ); // 20140722
//        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20160614a.html.txt" ); // 20140722
//        assertThat( old, notNullValue() );
        final InputStream test = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20160614.html.txt" );
        assertThat( test, notNullValue() );

        final HtmlContentOutput out = HtmlContentOutput.startOutput( new File("./target/test-h.html" ), OutputMode.HTML );

        DaisyDiff.diffHistogram( new BufferedReader( new InputStreamReader( old ) ), new BufferedReader( new InputStreamReader( test ) ), out.getHandler() );

        out.finishOutput();
    }

    @Test
    public void shouldDiffHistogramRaw() throws Exception {

        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20030605.html.txt" );
        assertThat( old, notNullValue() );
//        final InputStream test = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20041021.html.txt" );
//        assertThat( test, notNullValue() );

//        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20030605.html.txt" ); // 20140722
//        final InputStream old = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20160614a.html.txt" ); // 20140722
//        assertThat( old, notNullValue() );
        final InputStream test = Thread.currentThread().getContextClassLoader().getResourceAsStream( "law/20160614.html.txt" );
        assertThat( test, notNullValue() );

        final HtmlContentOutput out = HtmlContentOutput.startOutput( new File("./target/test-hr.html" ), OutputMode.HTML );

        DaisyDiff.diffHistogramRaw( old, test, out.getHandler() );

        out.finishOutput();
    }
}
