/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Liezarda
 */
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Liezarda
 */
public class MP3Filter extends FileFilter{
    
    private final String extension;
    private final String description;
    
    // Constructor Methodnya
    public MP3Filter(String extension, String description){
        // Set Contructor Values
        this.extension = extension;
        this.description = description;
    }
    
    @Override
    public boolean accept(File file) {
        // Balikin directory
        if(file.isDirectory()){
            return true;
        }
        // Balikin nama file + formatnya
        return file.getName().endsWith(extension);
    }

    @Override
    public String getDescription() {
        //Balikin deskripsi lengkapnya
        return description + String.format(" (*%s)", extension);
    }
    
}
