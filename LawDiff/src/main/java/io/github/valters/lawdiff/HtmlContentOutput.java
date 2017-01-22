package io.github.valters.lawdiff;
/*
  This file is licensed to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/


import java.io.File;
import java.io.IOException;

import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.outerj.daisy.diff.XslFilter;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** provides couple helpers to make html writing easier */
public class HtmlContentOutput {

    /** how to convert XML into HTML */
    protected static final String HTML_TRANSFORMATION_XSL = "xslfilter/tagheader-lawdiff.xsl";

    /** XSL looks for diffreport/diff/node() */
    protected static final String XSL_PLACEHOLDER = "diffreport";
    /** XSL looks for diffreport/diff/node() */
    protected static final String XSL_DIFF_PLACEHOLDER = "diff";

    private final ContentHandler consumer;

    public HtmlContentOutput( final ContentHandler content ) {
        this.consumer = content;
    }

    public static HtmlContentOutput startOutput( final File outFile, final boolean produceHtml ) throws Exception {
        final SAXTransformerFactory tf = XmlDomUtils.saxTransformerFactory();

        final TransformerHandler content = XmlDomUtils.newTransformerHandler( tf );
        content.setResult(new StreamResult( outFile ));

        final ContentHandler consumer =  produceHtml ? applyTransform( content ) : content;

        startOutput( consumer );

        final HtmlContentOutput contentOutput = new HtmlContentOutput( consumer );
        return contentOutput;
    }

    /** run generated XML through XSL transform to produce stand alone HTML */
    private static ContentHandler applyTransform( final TransformerHandler content ) throws IOException {
        final XslFilter filter = new XslFilter();
        final ContentHandler withTransform = filter.xsl( content, HTML_TRANSFORMATION_XSL );
        return withTransform;
    }

    private static void startOutput( final ContentHandler content ) throws SAXException {
        content.startDocument();
        content.startElement("", XSL_PLACEHOLDER, XSL_PLACEHOLDER, new AttributesImpl());
        content.startElement("", XSL_DIFF_PLACEHOLDER, XSL_DIFF_PLACEHOLDER, new AttributesImpl());
    }

    public void finishOutput() throws Exception {
        consumer.endElement("", XSL_DIFF_PLACEHOLDER, XSL_DIFF_PLACEHOLDER);
        consumer.endElement("", XSL_PLACEHOLDER, XSL_PLACEHOLDER);
        consumer.endDocument();
    }

    /** for straight-through write */
    public ContentHandler getHandler() {
        return consumer;
    }


}
