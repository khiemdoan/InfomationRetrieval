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
import java.io.File;
import java.io.FileFilter;

public class TextFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".tt")
                || pathname.getName().toLowerCase().endsWith(".kt")
                || pathname.getName().toLowerCase().endsWith(".pl")
                || pathname.getName().toLowerCase().endsWith(".cn");
    }
}
