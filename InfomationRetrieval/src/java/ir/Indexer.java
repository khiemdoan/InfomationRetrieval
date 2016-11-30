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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

   private IndexWriter writer;

   public Indexer(String indexDirectoryPath) throws IOException{
      //this directory will contain the indexes
      Directory indexDirectory = 
         FSDirectory.open(Paths.get(LuceneConstants.Path.INDEX_PATH));

      Analyzer analyzer = new VNAnalyzer(LuceneConstants.Path.STOPWORDS_PATH);
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      //create the indexer
      writer = new IndexWriter(indexDirectory, iwc);
   }

   public void close() throws CorruptIndexException, IOException{
      writer.close();
   }

   private Document getDocument(File file) throws IOException{
      Document document = new Document();
      
      InputStream stream = Files.newInputStream(file.toPath());
      
      BufferedReader reader = new BufferedReader(
              new InputStreamReader(stream, StandardCharsets.UTF_8));
      
      //index title
//      String title = reader.readLine();
//      Field titleField = new TextField(LuceneConstants.TITLE, new StringReader(title));
      
      //index file contents
      Field contentField = new TextField(LuceneConstants.CONTENTS, reader);
      //index file name
      Field fileNameField = new StringField(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES);
      //index file path
      Field filePathField = new StringField(LuceneConstants.FILE_PATH,
         file.getCanonicalPath(),  Field.Store.YES);
      
//      document.add(titleField);
      document.add(contentField);
      document.add(fileNameField);
      document.add(filePathField);

      return document;
   }   

   public void indexFile(File file) throws IOException{
      System.out.println("Indexing "+file.getCanonicalPath());
      Document document = getDocument(file);
      writer.addDocument(document);
   }

   public int createIndex(String dataDirPath, FileFilter filter) 
      throws IOException{
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();

      for (File file : files) {
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()
            && filter.accept(file)
         ){
            indexFile(file);
         }
      }
      return writer.numDocs();
   }
}