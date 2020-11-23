/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import View.UI;
import Model.Song;
import javax.swing.DefaultListModel;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author Liezl
    */
public class Koneksi {
        
    //=============================== MODEL DATABASE===========================
    
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    String lagu;
    ArrayList laguFav = new ArrayList();
        
    public void connect(){
        
        String url = "jdbc:mysql://localhost/playlist";
        String user = "root";
        String pass = "";
        
        try {
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            System.out.println("connecting");
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        System.out.println("connected");
    }    
     
    public void disconnect(){
    try {
        conn.close();
        stmt.close();
    } catch (SQLException ex) {
        }
    }
    public void uploadLaguFav(){
        try {
            connect();
            Statement smt = null;
            int indeks = jPlaylist.getSelectedIndex();
            
//            for (i=0; i < songName.size();i++){
//                    String sql = "insert into pbo(Lagu)" 
//                    + "values" + "('"+songName.get(i)+"')";
//                    smt = conn.createStatement();
//                    int Simpan=smt.executeUpdate(sql);  
//            }
            if (indeks == -1){
                songNameDisplay.setText(" - Pilih dulu lagu didalam playlist - ");
                
            } else{
                String sql = "insert into pbo(Lagu)" 
                    + "values" + "('"+songName.get(jPlaylist.getLeadSelectionIndex())+"')";
                smt = conn.createStatement();
                int Simpan=smt.executeUpdate(sql);
                songNameDisplay.setText( "Lagu : " + songName.get(indexSong)+" berhasil dijadikan favorit");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        showLaguFav();
        disconnect();
    }
    
    public void showLaguFav(){
            connect();
            
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("Select * from pbo");
                int i = 0;
                laguFav.clear();
                while (rs.next()){
                    laguFav.add(rs.getString(1));
                    i++;
                }
            } catch (Exception e){
                System.out.println(e.getMessage()+"we");
            }
            DefaultListModel model =  new DefaultListModel();
            for (int i = 0; i < laguFav.size(); i++) {
                model.addElement((i+1)+") " + laguFav.get(i));
            }
            if (laguFav.isEmpty()){
                songNameDisplay.setText("List lagu favorit masih kosong ...");
            }
            FavList.setModel(model);
            updatePl();
            disconnect();
    }
    
    public void deleteFav(){
        connect();
        Statement smt = null;
            try {
                String sql = "truncate pbo;"; 
                smt = conn.createStatement();
                int Simpan=smt.executeUpdate(sql);
                View.UI.songNameDisplay.setText( "berhasil delete favorit list");
            } catch (Exception e){
                printStackTrace(e);
            }
            showLaguFav();
    }
        
}
        
   
