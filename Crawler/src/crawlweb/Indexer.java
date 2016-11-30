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

/**
 *
 * @author thai
 */
public class Indexer {

    private ir.Indexer indexer;
    private Properties prop;
    private double mThreshold;
    private String mDataPath;

    public Indexer() throws IOException {
        indexer = new ir.Indexer(LuceneConstants.Path.INDEX_PATH);
        
        try (FileInputStream f = new FileInputStream(PROPERTIES_FILE)) {
            prop = new Properties();
            prop.load(f);
            mThreshold = Double.parseDouble(prop.getProperty(THRESHOLD_PROP, "0.3"));
            mDataPath = prop.getProperty(DATAPATH_PROP, "data/data");
        }
    }

    public void appendIndex() {
        double unindexedRate = Article.calcIndexedRate();
        FileFilter filter = new TextFileFilter();
        if (unindexedRate >= mThreshold) {
            List<Article> articles = Article.getByIndexedCheck(false);
            for(Article article: articles){
                try {
                    String path = mDataPath + "/" + article.getFilePath();
                    indexer.indexFile(new File(path));
                    article.setIndexChecked();
                } catch (IOException ex) {
                    Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
