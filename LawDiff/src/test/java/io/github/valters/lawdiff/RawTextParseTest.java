package io.github.valters.lawdiff;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.outerj.eclipse.jgit.util.IntList;

public class RawTextParseTest {

    @Test
    public void shouldConsolidateShortWords() throws Exception {
        final byte[] myStr = "(this is a test. 10.10.2016 . gendalf 2017. you should get ready.)".getBytes();
        final IntList map = RawTextParse.MAP_SENTENCES_GREEDY.tokenMap( myStr, 0, myStr.length );
        System.out.println( map );
        assertThat( map.size(), is( 8 ) );
        assertThat( map.get( 1 ), is( 0 ) );
        assertThat( map.get( 2 ), is( 1 ) );
        assertThat( map.get( 3 ), is( 16 ) );
        assertThat( map.get( 4 ), is( 29 ) );
        assertThat( map.get( 5 ), is( 43 ) );
        assertThat( map.get( 6 ), is( 65 ) );
        assertThat( map.get( 7 ), is( 66 ) );
    }

    @Test
    public void shouldResetOnLineEnds() throws Exception {
        final byte[] myStr = "\n10.10.2016. you should get ready.\n10.10. get greedy.".getBytes();
        final IntList map = RawTextParse.MAP_SENTENCES_GREEDY.tokenMap( myStr, 0, myStr.length );
        System.out.println( map );
        assertThat( map.size(), is( 8 ) );
        assertThat( map.get( 1 ), is( 0 ) );
        assertThat( map.get( 2 ), is( 1 ) );
        assertThat( map.get( 3 ), is( 12 ) );
        assertThat( map.get( 4 ), is( 34 ) );
        assertThat( map.get( 5 ), is( 35 ) );
        assertThat( map.get( 6 ), is( 41 ) );
        assertThat( map.get( 7 ), is( 53 ) );
    }

}
