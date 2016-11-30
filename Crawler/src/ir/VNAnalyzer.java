/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;

/**
 *
 * @author thinhnt
 */
public class VNAnalyzer extends StopwordAnalyzerBase {

    public VNAnalyzer(String stopwordFilePath) throws IOException {
        super(StopwordAnalyzerBase.loadStopwordSet(Paths.get(stopwordFilePath)));
    }

    @Override
    protected TokenStreamComponents createComponents(String string) {
        VNTokenizer vNTokenizer = new VNTokenizer();
        TokenStream tok = new StandardFilter(vNTokenizer);
        tok = new LowerCaseFilter(tok);
        tok = new StopFilter(tok, stopwords);
        return new TokenStreamComponents(vNTokenizer, tok) {

            @Override
            protected void setReader(final Reader reader) {
                super.setReader(reader);
            }
        };
    }

}
