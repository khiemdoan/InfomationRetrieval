/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import crawlweb.DB.Article;
import ir.IR;
import ir.LuceneConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.hus.nlp.tokenizer.VietTokenizer;
/**
 *
 * @author Khiem
 */
@WebServlet(urlPatterns = {"/query"})
public class Query extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String query = request.getParameter("query");
        if (query != null) {
            query = new String(query.getBytes("iso-8859-1"), "utf-8");
        }
        
        JSONArray arrayResults = new JSONArray();
        
        IR ir = new IR();
        Document[] docs;
        try {
            docs = ir.search(query, LuceneConstants.MAX_SEARCH);
            for (Document doc : docs) {
                Article article = Article.findByFilePath(doc.get(LuceneConstants.FILE_NAME));
                if (article == null) {
                    continue;
                }
                
                JSONObject result = new JSONObject();
                result.put("title", article.getTitle());
                result.put("content", article.getContent());
                result.put("url", article.getUrl());
                arrayResults.add(result);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(arrayResults.toJSONString());
    }

}
