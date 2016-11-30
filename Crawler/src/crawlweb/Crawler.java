package crawlweb;

import crawlweb.DB.Article;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import models.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class Crawler {

    private FileProcessor fileProcessor = new FileProcessor();

    public void crawl(List<Rss> rSSs) throws IOException {
        Document docRSS;
        for (Rss rss : rSSs) {
            docRSS = Jsoup.connect(rss.getLink()).get();
            // get all links
            Elements items = docRSS.select("item");
            for (Element item : items) {
                String link = item.getElementsByTag("link").text();
                if (Article.findByURL(link) != null) {
                    continue;
                }
                System.out.println(link);
                Document docContent;
                try {
                    docContent = Jsoup.connect(link).get();
                } catch (Exception ex) {
                    continue;
                }
                
                Element divContent = null;
                Element eTitle = docContent.getElementsByTag("title").first();
                String sTitle = eTitle.text();
                switch (rss.getWebsite()) {
                    case "vietnamnet.vn":
                        divContent = docContent.getElementById("ArticleContent");
                        break;
                    case "vnexpress.net":
                        divContent = docContent.getElementsByClass("fck_detail").first();
                        break;
                    case "www.24h.com.vn":
                        divContent = docContent.getElementsByClass("text-conent").first();
                        break;
                }

                //Article article = new Article();
                StringBuilder builder = new StringBuilder(sTitle).append("\n");
                String content;
                if (divContent != null) {
                    for (Element p : divContent.getElementsByTag("p")) {
                        if (!p.html().contains("<strong>") && p.hasText()
                                && !p.html().contains("<em>")) {
                            builder.append(p.text());
                        }
                    }
                    content = builder.toString();
                    String extension = "." + rss.getCat();
                    String filePath = fileProcessor.createPath(extension);
                    Article post = new Article(link, sTitle, content, filePath);
                    if (post.save()) {
                        fileProcessor.saveFile(post);
                    }
                    //System.out.println("article " + article.getContent());
                    //fileProcessor.generateDataFromWeb(article);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Crawler clawler = new Crawler();
            clawler.crawl(new FileProcessor().getRssLinks());
        } catch (Exception ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Indexer indexer = new Indexer();
            indexer.appendIndex();
        } catch (Exception ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
