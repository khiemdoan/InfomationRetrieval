/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir;

import static crawlweb.Constants.DATAPATH_PROP;
import static crawlweb.Constants.INDEX_PATH_PROP;
import static crawlweb.Constants.PROPERTIES_FILE;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author thinhnt
 */
public class IR {
    
    private Indexer indexer;
    private Searcher searcher;

//    public static void main(String[] args) {
//        IR tester;
//        try {
//            tester = new IR();
//            tester.createIndex();
//            tester.search("Xóa thuế nhập khẩu hàng ngàn mặt hàng\n"
//                    + "Bộ trưởng Bộ NN&PTNT Cao Đức Phát cam kết luôn sát cánh cùng nông dân, doanh nghiệp. Việt Nam bỏ thuế nhập khẩu ôtô từ Châu Âu trong 10 năm Giảm thuế nhập khẩu xăng dầu Thuế nhập khẩu giảm \"neo\" giá xăng dầu không tăng Theo Bộ Tài chính, các nước TPP cam kết dành cho Việt Nam khoảng 78%-95% số dòng thuế được xóa bỏ thuế nhập khẩu ngay khi TPP có hiệu lực. Các mặt hàng còn lại sẽ có lộ trình xóa bỏ thuế trong vòng 5-10 năm, trừ một số mặt hàng nhạy cảm có lộ trình trên 10 năm hoặc áp dụng biện pháp hạn ngạch thuế quan. Cụ thể, các mặt hàng xuất khẩu chủ lực của Việt Nam vào thị trường TPP được hưởng thuế suất 0% ngay sau khi hiệp định có hiệu lực hoặc sau 3-5 năm như nông sản, thủy sản, dệt may, giày dép, đồ gỗ, hàng điện, điện tử, cao su…  Đồ gỗ xuất khẩu là một trong những ngành hàng hưởng thuế suất 0% ngay sau khi hiệp định TPP có hiệu lực. Ảnh: HTD Ngược lại, Việt Nam cam kết xóa bỏ thuế nhập khẩu ngay khi TPP có hiệu lực bao gồm: động vật sống, thức ăn gia súc, một số sản phẩm sữa, ngũ cốc, gạo, da và sản phẩm da, cao su và sản phẩm cao su, chất dẻo, dược phẩm, thuốc trừ sâu, hóa chất, khoáng sản, một số loại giấy, nguyên liệu dệt may, da giày, vải bông các loại, sản phẩm dệt may, phân bón, nước hoa, mỹ phẩm, máy móc thiết bị, đồ nội thất, gỗ và sản phẩm gỗ, nhạc cụ, sản phẩm sắt thép, linh kiện điện tử… Những nhóm có lộ trình xóa bỏ thuế quan sau bốn năm là bánh kẹo, chè và cà phê, ngô ngọt, đồng hồ, hàng gia dụng, máy khâu, máy phát điện, đồ trang sức, vật liệu xây dựng, sữa, máy móc thiết bị, nhựa và sản phẩm nhựa, sản phẩm điện tử… Nhóm có lộ trình xóa bỏ thuế sau tám năm: Bộ phận linh kiện xe đạp, xe máy, một số linh kiện ô tô, bánh kẹo, chế phẩm thủy sản, dầu mỡ động thực vật, rau quả, sắt thép, xe đạp nguyên chiếc, một số loại xe chuyên dụng… Nhóm có lộ trình xóa bỏ thuế sau 10-11 năm: thịt các loại, bia rượu, đường, trứng, muối, xăng dầu, ô tô, sắt thép, một số loại linh kiện phụ tùng ô tô, phôi thép, săm lốp… Theo Bộ Tài chính, hiện nay các thành viên TPP đang hoàn tất các công tác rà soát kỹ thuật và các thủ tục cần thiết chuẩn bị cho ký kết chính thức dự kiến vào đầu năm 2016.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    public void createIndex() throws IOException {
        
        String indexDir = "";
        try (FileInputStream f = new FileInputStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            prop.load(f);
            indexDir = prop.getProperty(INDEX_PATH_PROP, "index");
        }
        
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        
        String dataDir = "";
        try (FileInputStream f = new FileInputStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            prop.load(f);
            dataDir = prop.getProperty(DATAPATH_PROP, "data");
        }
        
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed + " File indexed, time taken: "
                + (endTime - startTime) + " ms");
    }

    /**
     * Truy vấn tài liệu
     * @param searchQuery từ khóa truy vấn
     * @param maxSearch số lượng kết quả tối đa
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    public Document[] search(String searchQuery, int maxSearch) throws IOException, ParseException {
        Document[] result = null;
         String indexDir = "";
        try (FileInputStream f = new FileInputStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            prop.load(f);
            indexDir = prop.getProperty(INDEX_PATH_PROP, "index");
        }
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery, maxSearch);
        long endTime = System.currentTimeMillis();

        result = new Document[hits.scoreDocs.length];
        for (int i = 0; i < result.length; i++) {
            Document doc = searcher.getDocument(hits.scoreDocs[i]);
            result[i] = doc;
        }
        searcher.close();
        return result;
    }

}
