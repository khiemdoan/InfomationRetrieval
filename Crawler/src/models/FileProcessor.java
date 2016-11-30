package models;

import crawlweb.DB.Article;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Stream;
import static crawlweb.Constants.*;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class FileProcessor {

    private String AbsolutePath = new File("").getAbsolutePath();
    private String sourcePath = AbsolutePath + "/websources/rss.txt";
    //private String dataPath = AbsolutePath + "/data/data";
    private String dataPath = "data/data";
    private static Properties prop = new Properties();

    public FileProcessor() {
        try (FileInputStream f = new FileInputStream(PROPERTIES_FILE)) {
            prop.load(f);
            dataPath = prop.getProperty(DATAPATH_PROP);
            sourcePath = prop.getProperty(SOURCEPATH_PROP);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createProperties() {
        try (FileOutputStream f = new FileOutputStream(PROPERTIES_FILE, false)) {
            prop.setProperty(DATAPATH_PROP, "");
            prop.setProperty(SOURCEPATH_PROP, "");
            prop.store(f, "datapath");
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Rss> getRssLinks() {

        List<Rss> rSSs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sourcePath))) {

            String link;

            while ((link = br.readLine()) != null) {
                System.out.println(link);
                Rss rss = new Rss();
                rss.setCatAndWebsite(link);
                rss.setLink(link);

                rSSs.add(rss);
            }
            //br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rSSs;
    }

    public String createPath(String extension) throws IOException {
        File folder = new File(dataPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Stream<String> stream = Arrays.stream(folder.list());
        int[] array = stream.mapToInt((t)
                -> Integer.parseInt(t.split("\\.")[0])
        ).sorted().toArray();
        int name = 0;
        if (array.length > 0) {
            name = array[array.length - 1] + 1;
        }

        String filePath = dataPath + "/" + name + extension;
        return filePath;
    }

    public void saveFile(Article post) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(post.getFilePath())))) {
            writer.write(post.getContent());
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
