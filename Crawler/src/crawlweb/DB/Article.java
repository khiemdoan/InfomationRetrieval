/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlweb.DB;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import static crawlweb.Constants.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utility.PropertiesFile;

/**
 *
 * @author thai bieu dien 1 doi tuong Article trong table Article mysql
 */
public class Article {

    private int mId;
    private String mUrl;
    private String mContent;
    private String mFilePath;
    private boolean mIndexed;
    private String mTitle;

    public boolean isIndexed() {
        return mIndexed;
    }

    public void setIndexed(boolean mIndexed) {
        this.mIndexed = mIndexed;
    }
    private static final DBProperties sDbProperties = new DBProperties();
    private static final String TABLE_NAME = "articles";
    private static final String URL_FIELD = "Url";
    private static final String FILEPATH_FIELD = "FilePath";
    private static final String ID_FIELD = "Id";
    private static final String CONTENT_FIELD = "Content";
    private static final String ENCODE = "?useUnicode=true&characterEncoding=UTF-8";
    private static final String INDEXED_FIELD = "Indexed";
    private static final String TITLE_FIELD = "Title";

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getContent() {
        return mContent;
    }
    
    public String getContent2(String keyword) {        
        int length = mContent.length() < 300 ? mContent.length() : 300;
        String sContent = mContent.substring(0, length) + "...";
        return sContent;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    /**
     * @param id id
     * @param url url address
     * @param content content
     * @param filePath filePath
     *
     */
    public Article(int id, String url, String title, String content, String filePath) {
        this(id, url, title, content, filePath, false);
        mId = id;
    }

    private Article(int id, String url, String title, String content, String filePath, boolean indexed) {
        this(url, title, content, filePath, indexed);
        mId = id;
    }

    private Article(String url, String title, String content, String filePath, boolean indexed) {
        mUrl = url;
        mContent = content;
        mFilePath = filePath;
        mIndexed = indexed;
        mTitle = title;
    }

    /**
     * @param url url address
     * @param content content
     * @param filePath filePath
     *
     */
    public Article(String url, String title, String content, String filePath) {
        this(url, title, content, filePath, false);
        //mDbProperties = new DBProperties();
    }

    /**
     * tra lai cac bai viet theo trang thai indexed
     *
     * @param indexed trang thai index
     * @return List cac post theo trang thai, null neu loi ket noi
     *
     */
    public static List<Article> getByIndexedCheck(boolean indexed) {
        try (Connection conn = sDbProperties.getConnection()) {
            List<Article> posts = new ArrayList<>();
            String query = "select * from " + TABLE_NAME + " where " + INDEXED_FIELD + " = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setBoolean(1, indexed);
            ResultSet rs = statement.executeQuery();
            while (true) {
                Article post = getPost(rs);
                if (post == null) {
                    break;
                }
                posts.add(post);
            }
            return posts;
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Double calcIndexedRate() {
        double result = 0;
        try (Connection conn = sDbProperties.getConnection()) {
            String query = "select count(*) from " + TABLE_NAME;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            int all = rs.getInt(1);
            query += " where " + INDEXED_FIELD + " = 0";
            rs = st.executeQuery(query);
            rs.next();
            int indexed = rs.getInt(1);
            if (all > 0) {
                result = 1.0 * indexed / all;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * lay tat ca post trong mysql
     *
     * @return List cac Article trong csdl, null neu loi ket noi
     */
    public static List<Article> all() {
        try (Connection conn = sDbProperties.getConnection()) {

            List<Article> posts = new ArrayList<>();
            String query = "select * from " + TABLE_NAME;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (true) {
                Article post = getPost(rs);
                if (post == null) {
                    break;
                }
                posts.add(post);
            }
            return posts;
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static Article getPost(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        int id = rs.getInt(1);
        String url = rs.getString(2);
        String title = rs.getString(3);
        String content = rs.getString(4);
        String filePath = rs.getString(5);
        boolean mIndexed = rs.getBoolean(6);
        return new Article(id, url, title, content, filePath, mIndexed);
    }

    /**
     * luu post trong mysql
     *
     * @return true neu thanh cong va nguoc lai neu that bai
     */
    public boolean save() {
        try (Connection conn = sDbProperties.getConnection()) {
            String query = "insert INTO " + TABLE_NAME + "(" + URL_FIELD + ", "
                    + TITLE_FIELD + ", " + CONTENT_FIELD + ", " + FILEPATH_FIELD + ", " + INDEXED_FIELD
                    + ") values(?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, mUrl);
            statement.setString(2, mTitle);
            statement.setString(3, mContent);
            File file = new File(mFilePath);
            statement.setString(4, file.getName());
            statement.setBoolean(5, mIndexed);
            int count = statement.executeUpdate();
            return count > 0;
        } catch (Exception ex) {
            //  Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static Article findByURL(String url) {
        try (Connection conn = sDbProperties.getConnection()) {
            String query = "select * from " + TABLE_NAME + " where " + URL_FIELD
                    + " = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, url);
            ResultSet rs = statement.executeQuery();
            return getPost(rs);
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Article findById(int id) {
        try (Connection conn = sDbProperties.getConnection()) {
            String query = "select * from " + TABLE_NAME + " where " + ID_FIELD
                    + " = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            return getPost(rs);
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Tim kiem Article bang duong dan file
     *
     * @param filePath
     * @return lay ra Article theo duong dan file, null neu khong co hoac loi
     * ket noi
     */
    public static Article findByFilePath(String filePath) {
        try (Connection conn = sDbProperties.getConnection()) {
            String query = "select * from " + TABLE_NAME + " where " + FILEPATH_FIELD
                    + " = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, filePath);
            ResultSet rs = statement.executeQuery();
            return getPost(rs);
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * set trang thai da index cho post
     *
     * @return true: thanh cong, false: that bai
     */
    public boolean setIndexChecked() {
        try (Connection conn = sDbProperties.getConnection()) {
            String query = "UPDATE " + TABLE_NAME + " set " + INDEXED_FIELD + " = ? where " + ID_FIELD + " =?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setBoolean(1, true);
            statement.setInt(2, mId);
            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * lay config tu file config.properties
     */
    private static class DBProperties {

        private String mServer;
        private String mUserName;
        private String mPassWord;
        private String mDatabase;
        private String mUrl;
        private int mPort;
        private static BasicDataSource sDataSource;
        private static final String DRIVER_STRING = "jdbc:mysql:";

        private PropertiesFile p = new PropertiesFile();
        
        public DBProperties() {
            try {
                mUserName = p.getString(USER_PROP);
                mPassWord = p.getString(PASSWORD_PROP);
                mServer = p.getString(SERVER_PROP);
                mDatabase = p.getString(DATABASE_PROP);
                mPort = Integer.parseInt(p.getString(PORT_PROP));
                mUrl = DRIVER_STRING + "//" + mServer + ":" + mPort
                        + "/" + mDatabase + ENCODE;
                sDataSource = new BasicDataSource();
                sDataSource.setDriverClassName("com.mysql.jdbc.Driver");
                sDataSource.setUrl(mUrl);
                sDataSource.setUsername(mUserName);
                sDataSource.setPassword(mPassWord);
            } catch (NumberFormatException ex) {
                System.err.println("invalid port value");
            }
        }

        public Connection getConnection() throws SQLException {
            return sDataSource.getConnection();
        }

        private void generateProperties() {
            Properties p = new Properties();
            p.setProperty(USER_PROP, USER_PROP);
            p.setProperty(PASSWORD_PROP, PASSWORD_PROP);
            p.setProperty(SERVER_PROP, SERVER_PROP);
            p.setProperty(DATABASE_PROP, DATABASE_PROP);
            p.setProperty(PORT_PROP, PORT_PROP);
            try (FileWriter f = new FileWriter(PROPERTIES_FILE)) {
                p.store(f, "");
            } catch (IOException ex) {
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            //DBProperties dbConnector = new DBProperties();
////        Article post = new Article("a", "b", "c");
////        post.save();
////        Article post = Article.findByFilePath("c");
////        post.setIndexChecked();
//            Connection conn = new DBProperties().getConnection();
//            DatabaseMetaData meta = conn.getMetaData();
//
//// gets driver info:
//            System.out.println("JDBC driver version is " + meta.getDriverVersion());
//            System.out.println(Article.calcIndexedRate());
//        } catch (SQLException ex) {
//            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(Article.calcIndexedRate());
//    }

}
