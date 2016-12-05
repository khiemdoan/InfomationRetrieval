package models;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class Rss {
    private String cat;
    private String website;
    private String link;

    public Rss() {}

    
    public Rss(String cat, String link) {
        this.cat = cat;
        this.link = link;
    }
    
    public String getCat() {
        return cat;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    public void setCatAndWebsite(String link) {
        String[] res = link.split("/");
        this.website = res[2];
        String lastRes = res[res.length-1];
        int lastPos = lastRes.indexOf(".rss");
        
        switch(lastRes.substring(0,lastPos)) {
            case "cong-nghe":
                this.cat = "cn";
                break;
            case "congnghethongtin": 
                this.cat = "cn";
                break;
            case "kinh-te":
                this.cat = "kt";
                break;
            case "thitruongtieudung":
                this.cat = "kt";
                break;
            case "taichinhbatdongsan":
                this.cat = "kt";
                break;
            case "the-thao":
                this.cat = "tt";
                break;
            case "thethao": 
                this.cat = "tt";
                break;
            case "kinh-doanh":
                this.cat = "kt";
                break;
            case "phap-luat":
                this.cat = "pl";
                break;
            case "anninhhinhsu":
                this.cat = "pl";
                break;
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
