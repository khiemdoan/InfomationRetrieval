/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlweb;

import crawlweb.DB.Article;
import ir.LuceneConstants;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import static crawlweb.Constants.*;
import ir.TextFileFilter;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import utility.PropertiesFile;

/**
 *
 * @author thai
 */
public class Indexer {

    private ir.Indexer indexer;
    private Properties prop;
    private double mThreshold;
    private String mDataPath;
    private PropertiesFile p = new PropertiesFile();

    public Indexer() throws IOException {
        indexer = new ir.Indexer(LuceneConstants.Path.INDEX_PATH);
        mThreshold = Double.parseDouble(p.getString(THRESHOLD_PROP, "0.3"));
        mDataPath = p.getString(DATAPATH_PROP, "C:\\ir\\data");
    }

    public void appendIndex() {
        double unindexedRate = Article.calcIndexedRate();
        FileFilter filter = new TextFileFilter();
        if (unindexedRate >= mThreshold) {
            List<Article> articles = Article.getByIndexedCheck(false);
            try {
                for(Article article: articles){
                    String path = mDataPath + "/" + article.getFilePath();
                    indexer.indexFile(new File(path));
                    article.setIndexChecked();
                }
                indexer.close();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
