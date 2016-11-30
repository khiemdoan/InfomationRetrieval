/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir;

/**
 *
 * @author thinhnt
 */
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	
   IndexSearcher indexSearcher;
   QueryParser queryParser;
   Query query;
   IndexReader reader;
   
   public Searcher(String indexDirectoryPath) 
      throws IOException{
      Directory indexDirectory = 
         FSDirectory.open(Paths.get(LuceneConstants.Path.INDEX_PATH));
      reader = DirectoryReader.open(indexDirectory);
      indexSearcher = new IndexSearcher(reader);
      Analyzer analyzer = new VNAnalyzer(LuceneConstants.Path.STOPWORDS_PATH);
      queryParser = new QueryParser(LuceneConstants.CONTENTS, analyzer);
   }
   
   public TopDocs search(String searchQuery, int maxSearch) 
      throws IOException,ParseException{
      query = queryParser.parse(searchQuery);
      return indexSearcher.search(query, maxSearch);
   }

   public Document getDocument(ScoreDoc scoreDoc) 
      throws CorruptIndexException, IOException{
      return indexSearcher.doc(scoreDoc.doc);	
   }

   public void close() throws IOException{
      reader.close();
   }
}