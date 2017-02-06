package io.github.valters.lawdiff;

import org.outerj.eclipse.jgit.diff.RawTextTokenizer;
import org.outerj.eclipse.jgit.util.IntList;

public class RawTextParse {

    /** ties to join up "short" sentences to make bigger blocks out if them. */
    public static final RawTextTokenizer MAP_SENTENCES_GREEDY = new RawTextTokenizer() {

        @Override
        public IntList tokenMap( final byte[] buf, final int ptr, final int end ) {
            return sentenceMapGreedy( buf, ptr, end );
        }
    };

    /** consolidate shorter sentences into a little bit longer sentences - see if you can add word if next "sentence" is short.
     * this effectively makes single sentence out of dd.mm.yyyy. type dates */
    public static final IntList sentenceMapGreedy(final byte[] buf, int ptr, final int end) {
        final IntList map = new IntList((end - ptr) / 10);
        map.fillTo(1, Integer.MIN_VALUE);
        int prevPtr = ptr;
        if( ptr < end ) {
            map.add( ptr );
        }
        for ( ptr = nextSentenceDelimiter(buf, ptr); ptr < end; ptr = nextSentenceDelimiter(buf, ptr)) {
            // hard delimit on line-endings. don't allow to consolidate over line end, when sentence delimiters occur very early in line start, and previous line end.
            final byte ch = buf[ptr-1];
            if( ch == '\n') {
                map.add(ptr);
                prevPtr = ptr;
                continue;
            }

            final int nextPtr = nextSentenceDelimiter(buf, ptr);
            final int currSentenceLength = ptr - prevPtr;
            final int nextTokenLength = nextPtr - ptr;
            if( currSentenceLength < 9 && nextTokenLength < 7 ) {
                ;
            }
            else {
                map.add(ptr);
                prevPtr = ptr;
            }
        }
        map.add(end);
        return map;
    }

    public static final int nextSentenceDelimiter(final byte[] b, int ptr) {
        final char chrA = '.';
        final char chrB = ',';
        final char chrC = '(';
        final char chrD = ')';

        final int sz = b.length;
        while (ptr < sz) {
            final byte c = b[ptr++];
            if (c == chrA || c == chrB || c == chrC || c == chrD || c == '\n')
                return ptr;
        }
        return ptr;
    }

}
