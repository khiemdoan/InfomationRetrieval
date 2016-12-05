package models;

import utility.PropertiesFile;
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

    private static Properties prop = new Properties();
    private PropertiesFile p = new PropertiesFile();

    private void createProperties() {
        try (FileOutputStream f = new FileOutputStream(PROPERTIES_FILE, false)) {
            prop.setProperty(DATAPATH_PROP, "");
            prop.setProperty(SOURCEPATH_PROP, "");
            prop.store(f, "datapath");
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Rss> getRssLinks() throws IOException {
        List<Rss> rSSs = new ArrayList<>();
        String sourcePath = p.getString(SOURCEPATH_PROP);
        
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
        String dataPath = p.getString(DATAPATH_PROP);
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
            writer.write(post.getTitle() + "\n" + post.getContent());
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
