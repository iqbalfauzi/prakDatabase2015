package prak_dbd;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author cuplis
 */
public class prak9 extends javax.swing.JFrame {

    private String username, password, url;
    public Connection conn;
    String gender1 = "";
    Map param = new HashMap();

    public prak9() {
        super("Data Mahasiswa Baru");
        initComponents();
        koneksi();
        combo();
    }

    void combo() {
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT*FROM fakultas";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                kf.addItem(rs.getInt(1));
            }
            stat.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erorr" + ex);
        }

    }

    void koneksi() {
        try {
            Class.forName("org.mysql.Driver").newInstance();
            url = "jdbc:mysql://localhost:3306/mahasiswa";
            username = "root";
            password = "";
            try {
                conn = DriverManager.getConnection(url, username, password);
                JOptionPane.showMessageDialog(null, "Berhasil");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.exit(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void cari() {

        String a = String.valueOf(txtnim.getText());
        if (a == "") {
            JOptionPane.showMessageDialog(null, "Masukkan Nim");
        }
        int nim = Integer.parseInt(txtnim.getText());
        String nama = txtnama.getText();
        String alamat = txtalamat.getText();
        int kdfak = Integer.parseInt(String.valueOf(kf.getSelectedItem()));
        String gender = "";
        if (laki.isSelected()) {
            gender = laki.getText();
        } else {
            gender = perempuan.getText();
        }
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/akademik", "postgres", "postgres");
            Statement statement = conn.createStatement();
            String cari = "select * from mahasiswa where nim_mah ='" + nim + "'";
            ResultSet rs = statement.executeQuery(cari);

            if (rs.next()) {
                nim = Integer.parseInt(rs.getString("nim_mah"));
                nama = rs.getString("nama_mah");
                alamat = rs.getString("alamat_mah");
                kdfak = Integer.parseInt(rs.getString("id_fak"));
                gender = rs.getString("gender");
                ta.setText("Nim : " + nim + "\n Nama : " + nama + "\n Alamat :" + alamat
                        + "\n Kode Fakultas : " + kdfak + "\n Gender : " + gender + "");
                txtnama.setText(nama);
                txtalamat.setText(alamat);
                kf.setSelectedItem(kdfak);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nim yang Dicari tidak ada" + e);
        }
//        try{
//        Statement stat=conn.createStatement();
//        String sql="SELECT*FROM mahasiswa where nim_mah like'"+txtnim.getText()+"'";
//        ResultSet isi = stat.executeQuery(sql);
//        if(isi.next()){
//            txtnama.setText(isi.getString(2));
//            txtalamat.setText(isi.getString(3));
//            kf.setSelectedItem(isi.getInt(4));
//            if(isi.getString(5).equals("L")){
//                laki.setSelected(true);
//                //perempuan.setSelected(false);
//            }else if(isi.getString(5).equals("P")){
//                perempuan.setSelected(true);
//                //laki.setSelected(false);
//            }
//        }else{
//            JOptionPane.showMessageDialog(null, "Nim yang Anda Cari tidak ada!!");
//        }
//        stat.close();
//    }catch(Exception ex){
//        JOptionPane.showMessageDialog(null, "Error :"+ex);
//    }
    }

    void simpan() {
        int nim = Integer.parseInt(txtnim.getText());
        String nama = txtnama.getText();
        String alamat = txtalamat.getText();
        int kf1 = (int) kf.getSelectedItem();
        if (laki.isSelected() == true) {
            gender1 = "L";
        } else if (perempuan.isSelected() == true) {
            gender1 = "P";
        }
        try {
            Statement st = conn.createStatement();
            String sql = "insert into mahasiswa values('" + nim + "','" + nama + "','" + alamat + "','" + kf1 + "','" + gender1 + "');";
            st.executeUpdate(sql);
            st.close();
            JOptionPane.showMessageDialog(null, "Berhasil Disimpan");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "nim data sudah ada..!!");
            System.err.print(ex);
        }
    }

    void edit() {
        int nim = Integer.parseInt(txtnim.getText());
        String nama = txtnama.getText();
        String alamat = txtalamat.getText();
        int kf1 = (int) kf.getSelectedItem();
        if (laki.isSelected() == true) {
            gender1 = "L";
        } else if (perempuan.isSelected() == true) {
            gender1 = "P";
        }
        try {
            Statement stm = conn.createStatement();
            String sql = "update mahasiswa set nama_mah='" + nama + "',alamat_mhs='" + alamat + "',id_fak='" + kf1 + "',gender='" + gender1 + "'where nim_mah='" + nim + "'";

            stm.executeUpdate(sql);
            stm.close();
            JOptionPane.showMessageDialog(null, "Berhasil Di Update");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error :" + ex);
        }
    }

    void hapus() {
        int nim = Integer.parseInt(txtnim.getText());
        try {
            Statement st = conn.createStatement();
            String sql = "delete from mahasiswa where nim_mah='" + nim + "'";
            st.executeUpdate(sql);
            st.close();
            JOptionPane.showMessageDialog(null, "Data telah di hapus");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error :" + ex);
        }
    }

    void tampil() {
        if (laki.isSelected() == true) {
            gender1 = "L";
        } else if (perempuan.isSelected() == true) {
            gender1 = "P";
        }
        ta.setText("1. NIM = " + txtnim.getText() + "\n 2. Nama = " + txtnama.getText() + "\n 3. Alamat = " + txtalamat.getText() + "\n 4. Kode Fakultas = " + kf.getSelectedItem() + "\n 5. Gender = " + gender1);
    }

    void reset() {
        txtnim.setText("");
        txtnama.setText("");
        txtalamat.setText("");
        kf.setSelectedItem(1);
        laki.setSelected(false);
        perempuan.setSelected(false);
        ta.setText("");
        reset_T();
    }

    void tampil2() {
        Object[] baris = {"NIM", "Nama", "Alamat", "Kode Fakultas", "Gender"};
        DefaultTableModel tm = new DefaultTableModel(null, baris);
        tabel.setModel(tm);
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT*FROM mahasiswa order by nim_mah";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                tm.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)});
            }
            tabel.setModel(tm);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error :" + ex);
        }
    }

    void reset_T() {
        Object[] baris = {"NIM", "Nama", "Alamat", "Kode Fakultas", "Gender"};
        DefaultTableModel tm = new DefaultTableModel(null, baris);
        tabel.setModel(tm);
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT*FROM mahasiswa where nim_mah='" + 9999999 + "'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                tm.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)});
            }
            tabel.setModel(tm);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error :" + ex);
        }
    }

    void F_Cetak() {
        try {
            File reprt = new File("E:/STATE ISLAMIC UNIV/3 Semester/2. PRAKTIKUM DESAIN BASIS DATA/14650013_(KELAS F)_ _ Iqbal fauzi/no 9 & 10/dbd/prak_dbd/src/oke/report1.jrxml");
            JasperDesign JasDes = JRXmlLoader.load(reprt);
            param.clear();
            JasperReport JasRep = JasperCompileManager.compileReport(JasDes);
            JasperPrint JasPri = JasperFillManager.fillReport(JasRep, param, conn);
            JasperViewer.viewReport(JasPri, true);
            //JasperPrintManager.printReport(JasPri, true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    void cetak2() {
        try {
            File reprt = new File("E:/STATE ISLAMIC UNIV/3 Semester/2. PRAKTIKUM DESAIN BASIS DATA/14650013_(KELAS F)_ _ Iqbal fauzi/no 9 & 10/dbd/prak_dbd/src/oke1/report1.jrxml");
            JasperDesign JasDes = JRXmlLoader.load(reprt);
            param.put("nim_mah", Integer.parseInt(txtnim.getText()));
            JasperReport JasRep = JasperCompileManager.compileReport(JasDes);
            JasperPrint JasPri = JasperFillManager.fillReport(JasRep, param, conn);
            JasperViewer.viewReport(JasPri, false);
            //JasperPrintManager.printReport(JasPri, true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gender = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Kode_Fakultas = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtnim = new javax.swing.JTextField();
        txtnama = new javax.swing.JTextField();
        txtalamat = new javax.swing.JTextField();
        perempuan = new javax.swing.JRadioButton();
        laki = new javax.swing.JRadioButton();
        cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta = new javax.swing.JTextArea();
        simpan = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        tampil = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        kf = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();
        cetak = new javax.swing.JButton();
        cetak_nim = new javax.swing.JButton();

        jLabel5.setText("jLabel5");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("NIM");

        jLabel2.setText("Nama");

        jLabel3.setText("Alamat");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        Kode_Fakultas.setText("Kode_Fakultas");

        jLabel4.setText("Gender");

        gender.add(perempuan);
        perempuan.setText("Perempuan");

        gender.add(laki);
        laki.setText("Laki-Laki");

        cari.setText("Cari");
        cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariActionPerformed(evt);
            }
        });

        ta.setColumns(20);
        ta.setRows(5);
        jScrollPane1.setViewportView(ta);

        simpan.setText("Simpan");
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        hapus.setText("Hapus");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        tampil.setText("Tampil");
        tampil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilActionPerformed(evt);
            }
        });

        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NIM", "NAMA", "ALAMAT", "Kode Fakultas", "Gender"
            }
        ));
        jScrollPane4.setViewportView(tabel);

        cetak.setText("Cetak");
        cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakActionPerformed(evt);
            }
        });

        cetak_nim.setText("Cetak NIM");
        cetak_nim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetak_nimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Kode_Fakultas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(simpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(laki)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(perempuan))
                            .addComponent(txtalamat)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtnim, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addComponent(cari, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtnama)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cetak))
                            .addComponent(kf, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cetak_nim)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(316, 316, 316)
                        .addComponent(tampil, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(273, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cari)
                    .addComponent(cetak_nim))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtalamat, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Kode_Fakultas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(perempuan)
                        .addComponent(laki)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpan)
                    .addComponent(edit)
                    .addComponent(hapus)
                    .addComponent(tampil)
                    .addComponent(cetak))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(reset)
                        .addGap(140, 140, 140))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        cari();
    }//GEN-LAST:event_cariActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        simpan();
    }//GEN-LAST:event_simpanActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        edit();
    }//GEN-LAST:event_editActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        hapus();
    }//GEN-LAST:event_hapusActionPerformed

    private void tampilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilActionPerformed

        tampil2();
    }//GEN-LAST:event_tampilActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        reset();
    }//GEN-LAST:event_resetActionPerformed

    private void cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakActionPerformed
        F_Cetak();
    }//GEN-LAST:event_cetakActionPerformed

    private void cetak_nimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetak_nimActionPerformed
        cetak2();
    }//GEN-LAST:event_cetak_nimActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(prak9.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(prak9.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(prak9.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(prak9.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new prak9().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Kode_Fakultas;
    private javax.swing.JButton cari;
    private javax.swing.JButton cetak;
    private javax.swing.JButton cetak_nim;
    private javax.swing.JButton edit;
    private javax.swing.ButtonGroup gender;
    private javax.swing.JButton hapus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox kf;
    private javax.swing.JRadioButton laki;
    private javax.swing.JRadioButton perempuan;
    private javax.swing.JButton reset;
    private javax.swing.JButton simpan;
    private javax.swing.JTextArea ta;
    private javax.swing.JTable tabel;
    private javax.swing.JButton tampil;
    private javax.swing.JTextField txtalamat;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtnim;
    // End of variables declaration//GEN-END:variables
}
