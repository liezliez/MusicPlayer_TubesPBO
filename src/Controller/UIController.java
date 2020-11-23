/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.UI;
import jaco.mp3.player.MP3Player;
import javax.swing.DefaultListModel;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.nio.file.Paths;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Controller.MP3Filter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import Model.Song;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
/**
 *
 * @author Liezl
 */
public class UIController {
    private UI view;
    public MP3Player player;
    File songFile;
    String currentDirectory = "home.user"; //
    String currentPath;
    String imagePath;
    String appName = "PEMUTAR MUSIK";
    // menyimpan judul
    ArrayList songName = new ArrayList();
    // current index song
    int indexSong = 0;
    // index musik yang telah dimasukkan
    ArrayList Song = new ArrayList();
    int i = 1;
    boolean repeat = false;
    boolean windowCollapsed = false;
    int xMouse, yMouse;
    
    
        public MP3Player mp3Player(){
        MP3Player mp3Player = new MP3Player();
        return mp3Player;
    }
    private void updatePl(){
        DefaultListModel model =  new DefaultListModel();
        for (int i = 0; i < songName.size(); i++) {
                model.addElement((i+1)+") " + songName.get(i));
            }
        jPlaylist.setModel(model);
    }
    
    
    
    private void prevSong(){
        if (player.isStopped()){
            player.skipBackward();
            player.stop();
        } else {
            player.skipBackward();
        }
        if(indexSong != 0){
            indexSong--;
            updateTextDisplay();
        } else {
            songNameDisplay.setText( "Diujung playlist | " + songName.get(indexSong));
        }
    }
    private void nextSong(){
        if (player.isStopped()){
            player.skipForward();
            player.stop();
        } else {
            player.skipForward();
        }
        try {
            indexSong++;
            updateTextDisplay();
            } catch(java.lang.IndexOutOfBoundsException s){
                indexSong--;
                songNameDisplay.setText("Diujung playlist | " + songName.get(indexSong));
            }
    }
    
    private void chooseSong(){
        int choosedSong;
        choosedSong = jPlaylist.getLeadSelectionIndex();
        if(indexSong < choosedSong){
            for(int i = indexSong;i < choosedSong;i++){
                player.skipForward();
            }
        } else {
            for(int i = indexSong;i > choosedSong;i--){
                player.skipBackward();
            }
        }
        indexSong = choosedSong;
        updateTextDisplay();
    }
    
    private void addSong(){
        JFileChooser openFileChooser = new JFileChooser(currentDirectory);
        openFileChooser.setFileFilter(new MP3Filter(".mp3", "Khusus file mp3"));
        int result = openFileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            songFile = openFileChooser.getSelectedFile();
            player.addToPlayList(songFile);
            currentDirectory = songFile.getAbsolutePath();
            songName.add(songFile.getName());
            i++;
        }
        updatePl();
        updateTextDisplay();
        
    }
    
    private void updateTextDisplay(){
        if(player.isStopped()){
                songNameDisplay.setText("Stopped | " + songName.get(indexSong));
        } else if (player.isPaused()){
                songNameDisplay.setText("Paused | " + songName.get(indexSong));
        } else {
                songNameDisplay.setText("Playing Now... | " + songName.get(indexSong));
    
    }
        
    }
    private void volumeDownControl(Double valueToPlusMinus){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for(Mixer.Info mixerInfo : mixers){
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
        // Mixer dari audio tipe boolean    
            for(Line.Info lineInfo : lineInfos){
                Line line = null;
                // instansiasi boolean buat string volume
                boolean opened = true;
                try{
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if(!opened){
                        line.open();
                    }
                    FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    float currentVolume = volControl.getValue();
                    Double volumeToCut = valueToPlusMinus;
                    float changedCalc = (float) ((float)currentVolume-(double)volumeToCut);
                    volControl.setValue(changedCalc);
                    
                }catch (LineUnavailableException lineException){
                }catch (IllegalArgumentException illException){
                }finally{
                    if(line != null && !opened){
                        line.close();
                    }
                }
            }
        }
    }
    private void volumeUpControl(Double valueToPlusMinus){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for(Mixer.Info mixerInfo : mixers){
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            for(Line.Info lineInfo : lineInfos){
                Line line = null;
                boolean opened = true;
                try{
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if(!opened){
                        line.open();
                    }
                    FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    float currentVolume = volControl.getValue();
                    Double volumeToCut = valueToPlusMinus;
                    float changedCalc = (float) ((float)currentVolume+(double)volumeToCut);
                    volControl.setValue(changedCalc);
                    
                }catch (LineUnavailableException lineException){
                }catch (IllegalArgumentException illException){
                }finally{
                    if(line != null && !opened){
                        line.close();
                    }
                }
            }
        }
    }
    private void volumeControl(Double valueToPlusMinus){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for(Mixer.Info mixerInfo : mixers){
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            for(Line.Info lineInfo : lineInfos){
                Line line = null;
                boolean opened = true;
                try{
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if(!opened){
                        line.open();
                    }
                    FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    float currentVolume = volControl.getValue();
                    Double volumeToCut = valueToPlusMinus;
                    float changedCalc = (float) ((double)volumeToCut);
                    volControl.setValue(changedCalc);
                    
                }catch (LineUnavailableException lineException){
                }catch (IllegalArgumentException illException){
                }finally{
                    if(line != null && !opened){
                        line.close();
                    }
                }
            }
        }
    }
}
