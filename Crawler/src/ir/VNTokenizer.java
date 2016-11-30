/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir;

import java.io.IOException;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 *
 * @author thinhnt
 */
public class VNTokenizer extends Tokenizer {

    protected String[] tokens;
    protected int index;
    protected CharTermAttribute charTermAttribute
            = addAttribute(CharTermAttribute.class);
    protected VietTokenizer vietTokenizer;

    public VNTokenizer() {
        super();
        vietTokenizer = new VietTokenizer();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (tokens == null) {
            index = -1;
            int numChars;
            char[] buffer = new char[1024];
            StringBuilder stringBuilder = new StringBuilder();
            try {
                while ((numChars
                        = this.input.read(buffer, 0, buffer.length)) != -1) {
                    stringBuilder.append(buffer, 0, numChars);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.tokens = vietTokenizer.tokenize(stringBuilder.toString())[0].split(" ");
        }
        
        clearAttributes();
        index++;
        if (index == tokens.length){
            return false;
        }
        
        this.charTermAttribute.append(tokens[index]);
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset(); //To change body of generated methods, choose Tools | Templates.
        index = -1;
        tokens = null;
    }

}
