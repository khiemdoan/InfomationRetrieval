package utility;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Khiem
 */
public class PropertiesFile {
    public PropertiesFile() {
        String excutePath = PropertiesFile.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String configPath = new File(excutePath).getParent();
        configPath += File.separatorChar + "config.properties";
        try {
            FileInputStream f = new FileInputStream(configPath);
            prop.load(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Properties prop = new Properties();
    
    public String getString(String key) {
        return getString(key, "");
    }
    
    public String getString(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }
}
