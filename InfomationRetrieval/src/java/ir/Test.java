/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author thinhnt
 */
public class Test {

    public static void main(String[] args) throws IOException, ParseException {
        IR ir = new IR();

        // Tạo chỉ mục
//        ir.createIndex();
        /**
         * ********Tìm theo trường*********
         */
        // Cú pháp: <tên_trường>:"<từ_khóa>"
        Document[] docs = ir.search("filename:\"409.kt\" AND contents:\"chính thức ra mắt quỹ đầu tư\"", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        // Nếu tìm trên trường mắc đinh (trường contents) thì không cần ghi tên trường
        docs = ir.search("\"quỹ đầu tư\"", LuceneConstants.MAX_SEARCH);
        showResult(docs);

        /**
         * ********Wildcard Searches*********
         */
        // Sử dụng ? để thay thế cho 1 ký tự; * để thay thế cho nhiều ký tự
        // Cách này chỉ áp dụng cho tìm kiếm từ, ko áp dụng trong cụm từ
        // Chú ý: Không có ngoặc kép trong câu truy vấn
        docs = ir.search("quốc*", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        /**
         * ********Tìm kiếm gấn đúng*********
         */
        // Tìm các văn bản chức các từ trong truy vấn. Nhưng các từ này có thể 
        // không đứng liên tiếp nhau trong văn bản
        // Cú pháp "<Cụm từ>"~<Khoảng các tối đa giữa các từ trong văn bản>
        docs = ir.search("\"giá vàng tăng\"~100", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        docs = ir.search("\"quỹ đầu tư Thái Bình Dương\"~100", LuceneConstants.MAX_SEARCH);
        showResult(docs); //455
        
        /**
         * ********Tìm kiếm trong khoản*********
         */
        // Tìm các văn bản có trường nằm trong khoảng nào đó
        // Cú pháp <tên trường>:[min TO max] // tính cả min, max
        // hoặc     <tên trường>:{min TO max} // Ko tính min, max
        docs = ir.search("filename:[100 TO 200]", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        
        /**
         * ********Truy vấn boolean*********
         */
        
        // Tìm văn bản chứa cụm "Cảm biến vân tay" hoặc "Điều tra tội phạm"
        docs = ir.search("\"Cảm biến vân tay\" OR \"Điều tra tội phạm\"", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        // Tìm văn bản chứa cụm "Liên đoàn điền kinh" và "Vô địch thế giới"
        docs = ir.search("\"Liên đoàn điền kinh\" AND \"Vô địch thế giới\"", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        // Tìm văn bản chứa cụm "Liên đoàn điền kinh" nhưng không chứa "Vô địch thế giới"
        docs = ir.search("\"Liên đoàn điền kinh\" NOT \"Vô địch thế giới\"", LuceneConstants.MAX_SEARCH);
        showResult(docs);
        
        // Chú ý: Sử dụng dấu ngoặc đơn để gộp các mệnh đề

    }

    private static void showResult(Document[] docs) {
        System.out.println("");
        System.out.println("Result: ");
        for (Document doc : docs) {
            System.out.println(doc.get(LuceneConstants.FILE_NAME));
            System.out.println(doc.get(LuceneConstants.FILE_PATH));

        }
    }
}
