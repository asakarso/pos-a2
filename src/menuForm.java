import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class menuForm extends javax.swing.JFrame {

    static Connection koneksi;
    static PreparedStatement pst;
    
    public static Connection getKoneksi(){
        try {
            String url = "jdbc:mysql://localhost/restoran";
            String user = "root";
            String password = "";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            koneksi = DriverManager.getConnection(url, user, password);
        } catch (SQLException t){
            System.out.println("Error Membuat Koneksi");
            
        }
        return koneksi;
    }
    
    public void loadMenu(){
        DefaultTableModel menuForm = (DefaultTableModel)tableMenu.getModel();
        menuForm.getDataVector().removeAllElements();
        menuForm.fireTableDataChanged();
        try{
            Connection c = getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM menu";
            ResultSet r = s.executeQuery(sql);
            while(r.next()){
                Object[] o = new Object [5];
                o[0] = r.getString("id_menu");
                o[1] = r.getString("nama_menu");
                o[2] = r.getString("jenis_menu");
                o[3] = r.getString("harga_menu");
                o[4] = r.getString("deskripsi_menu");
                menuForm.addRow(o);
            }
            
            r.close();
            s.close();
        } catch(SQLException e){
            System.out.println("terjadi Error");
        }
        
    }
    
    private void clearFields() {
        idMenu.setText("");
        namaMenu.setText("");
        jenisMenu.setSelectedIndex(0);
        hargaMenu.setText("");
        deskripsiMenu.setText("");
    }
    
    public menuForm() {
        initComponents();
        getKoneksi();
        loadMenu();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        namaMenu = new javax.swing.JTextField();
        hargaMenu = new javax.swing.JTextField();
        deskripsiMenu = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableMenu = new javax.swing.JTable();
        jenisMenu = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        idMenu = new javax.swing.JTextField();

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
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nama Menu");

        jLabel2.setText("Jenis");

        jLabel3.setText("Harga");

        jLabel4.setText("Deskripsi");

        namaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaMenuActionPerformed(evt);
            }
        });

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        tableMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Menu", "Nama Menu", "Jenis Menu", "Harga", "Deskripsi"
            }
        ));
        tableMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMenuMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableMenu);

        jenisMenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Signature", "Add On", "Drink"}));
        jenisMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jenisMenuActionPerformed(evt);
            }
        });

        jLabel5.setText("Id Menu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnTambah)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnUbah)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnHapus))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jenisMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hargaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deskripsiMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idMenu)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(idMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(namaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jenisMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(hargaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(deskripsiMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnUbah)
                            .addComponent(btnHapus))))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
// TODO add your handling code here:
        try {
            String id_menu = idMenu.getText().trim();
            String jenis_menu = (String) jenisMenu.getSelectedItem();
            String nama_menu = namaMenu.getText().trim();
            String harga_menu = hargaMenu.getText().trim();
            String deskripsi_menu = deskripsiMenu.getText().trim();

            if (id_menu.isEmpty() || nama_menu.isEmpty() || harga_menu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom wajib!");
                return;
            }

            pst = getKoneksi().prepareStatement("INSERT INTO menu (id_menu, jenis_menu, nama_menu, harga_menu, deskripsi_menu) VALUES (?, ?, ?, ?, ?)");
            pst.setString(1, id_menu);
            pst.setString(2, jenis_menu);
            pst.setString(3, nama_menu);
            pst.setString(4, harga_menu);
            pst.setString(5, deskripsi_menu);

            int k = pst.executeUpdate();
            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data!");
            }
            loadMenu();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
// TODO add your handling code here:
        try {
            String id_menu = idMenu.getText().trim();
            String jenis_menu = (String) jenisMenu.getSelectedItem();
            String nama_menu = namaMenu.getText().trim();
            String harga_menu = hargaMenu.getText().trim();
            String deskripsi_menu = deskripsiMenu.getText().trim();

            if (id_menu.isEmpty() || nama_menu.isEmpty() || harga_menu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom wajib!");
                return;
            }

            pst = getKoneksi().prepareStatement("UPDATE menu SET jenis_menu = ?, nama_menu = ?, harga_menu = ?, deskripsi_menu = ? WHERE id_menu = ?");
            pst.setString(1, jenis_menu);
            pst.setString(2, nama_menu);
            pst.setString(3, harga_menu);
            pst.setString(4, deskripsi_menu);
            pst.setString(5, id_menu);

            int k = pst.executeUpdate();
            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
            }
            loadMenu();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
        }    
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        try {
            String id_menu = idMenu.getText().trim();

            if (id_menu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                pst = getKoneksi().prepareStatement("DELETE FROM menu WHERE id_menu = ?");
                pst.setString(1, id_menu);

                int k = pst.executeUpdate();
                if (k == 1) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
                }
                loadMenu();
                clearFields();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void namaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaMenuActionPerformed

    private void tableMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMenuMouseClicked
        // TODO add your handling code here:
   
            DefaultTableModel menuForm = (DefaultTableModel)tableMenu.getModel();
        
            String id = menuForm.getValueAt(tableMenu.getSelectedRow(), 0).toString();
            String nama = menuForm.getValueAt(tableMenu.getSelectedRow(), 1).toString();
            String jenis = menuForm.getValueAt(tableMenu.getSelectedRow(), 2).toString();
            String harga = menuForm.getValueAt(tableMenu.getSelectedRow(), 3).toString();
            String deskripsi = menuForm.getValueAt(tableMenu.getSelectedRow(), 4).toString();

            
            idMenu.setText(id);
            namaMenu.setText(nama);
            jenisMenu.setSelectedItem(jenis);
            hargaMenu.setText(harga);
            deskripsiMenu.setText(deskripsi);
        
    }//GEN-LAST:event_tableMenuMouseClicked

    private void jenisMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jenisMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jenisMenuActionPerformed

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
            java.util.logging.Logger.getLogger(menuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JTextField deskripsiMenu;
    private javax.swing.JTextField hargaMenu;
    private javax.swing.JTextField idMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jenisMenu;
    private javax.swing.JTextField namaMenu;
    private javax.swing.JTable tableMenu;
    // End of variables declaration//GEN-END:variables
}
