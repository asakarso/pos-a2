import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class kasirForm extends javax.swing.JFrame {

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
    
    private void pegawaiCombo(){
        try {
            Connection c = getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM pegawai";
            ResultSet r = s.executeQuery(sql);
            while(r.next()){
                employeeValue.addItem(r.getString("nama_pegawai"));
            }
        } catch(SQLException e){
            System.out.println("Error Memuat Pegawai");
        }
    }
    
    private void menuCombo(){
        try {
            Connection c = getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM menu";
            ResultSet r = s.executeQuery(sql);
            while(r.next()){
                menuValue.addItem(r.getString("nama_menu"));
            }
        } catch(SQLException e){
            System.out.println("Error Memuat Menu");
        }
    }
    
    private void loadTabel(){
        DefaultTableModel kasirForm = (DefaultTableModel)tabelTransaksi.getModel();
        kasirForm.getDataVector().removeAllElements();
        kasirForm.fireTableDataChanged();
        
        String id_trans = idTransaksi.getText().trim();

        try {
            Connection c = getKoneksi();
            String sql = "SELECT * FROM detail_transaksi WHERE Nomor_transaksi = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, id_trans);  
            ResultSet r = pst.executeQuery();
            double totalPrice = 0;
            double totalItem = 0;
            double totalPPN = 0;
            double totalService = 0;
            double total = 0;

            while (r.next()) {
                String id_menu = r.getString("ID_Menu");
                String jumlah_beli_str = r.getString("Jumlah_beli");

                String queryMenu = "SELECT nama_menu, jenis_menu, harga_menu FROM menu WHERE id_menu = ?";
                pst = c.prepareStatement(queryMenu);
                pst.setString(1, id_menu);
                ResultSet rs = pst.executeQuery();

                String nama_menu = "";
                String jenis_menu = "";
                String harga_menu = "";

                if (rs.next()) {
                    nama_menu = rs.getString("nama_menu");
                    jenis_menu = rs.getString("jenis_menu");
                    harga_menu = rs.getString("harga_menu");
                } else {
                    JOptionPane.showMessageDialog(this, "Menu tidak ditemukan untuk ID_Menu: " + id_menu);
                    continue;
                }

                double harga = 0;
                try {
                    harga = Double.parseDouble(harga_menu);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Format harga tidak valid!");
                    continue;
                }

                int jumlah_beli = 0;
                try {
                    jumlah_beli = Integer.parseInt(jumlah_beli_str);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Jumlah beli tidak valid!");
                    continue;
                }

                double total_harga = harga * jumlah_beli;

                Object[] o = new Object[6];
                o[0] = id_menu;
                o[1] = nama_menu;
                o[2] = jenis_menu;
                o[3] = harga_menu;
                o[4] = jumlah_beli;
                o[5] = total_harga;
                
                totalPrice += total_harga;
                totalItem += jumlah_beli;
                

                kasirForm.addRow(o);
                
                rs.close();
            }

            r.close();
            pst.close();
            
            totalPPN = totalPrice*0.10;
            totalService = totalPrice*0.05;
            total = totalPrice + (totalPPN + totalService);
            
            priceTotalValue.setText(String.valueOf(total));
            subTotalValue.setText(String.valueOf(totalPrice));
            totalPPNValue.setText(String.valueOf(totalPPN));
            serviceTotalValue.setText(String.valueOf(totalService));
            totalItemsValue.setText(String.valueOf(totalItem));
        } catch(SQLException e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }
    
    public void loadRiwayat(){
        DefaultTableModel kasirForm = (DefaultTableModel)tabelRiwayat.getModel();
        kasirForm.getDataVector().removeAllElements();
        kasirForm.fireTableDataChanged();
        
        LocalTime now = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        waktuBayar.setText(now.format(timeFormatter));
        
        try{
            Connection c = getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM transaksi";
            ResultSet r = s.executeQuery(sql);
            while(r.next()){
                Object[] o = new Object [14];
                o[0] = r.getInt("no_transaksi");
                o[1] = r.getDate("tanggal_transaksi");
                o[2] = r.getTime("waktu_pemesanan");
                o[3] = "";
                o[4] = r.getString("nama_customer");
                o[5] = "";
                o[6] = r.getString("jenis_pemesanan");
                o[7] = r.getInt("jumlah_customer");
                o[8] = r.getDouble("total_transaksi");
                o[9] = r.getDouble("total_ppn");
                o[10] = r.getDouble("total_service");
                o[11] = r.getInt("nomor_meja");
                o[12] = r.getString("status_transaksi");
                o[13] = r.getInt("id_pegawai");
                kasirForm.addRow(o);
            }
            
            r.close();
            s.close();
        } catch(SQLException e){
            e.printStackTrace(); 
            System.out.println("Terjadi error saat memuat data: " + e.getMessage());
        }
        
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

    public void resetFormKasir() {
        idTransaksi.setText("");
        mejaValue.setText("0");
        jamValue.setText("");
        custValue.setText("");
        jumlahCust.setText("0");
        discValue.setText("0");
        qtyInputValue.setText("0");

        if (jenisValue != null) {
            jenisValue.setSelectedIndex(0); 
        }

        LocalDate today = LocalDate.now();
        Date tanggalHariIni = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        tanggalValue.setDate(tanggalHariIni);

        LocalTime now = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        jamValue.setText(now.format(timeFormatter)); 
    }


    public kasirForm() {
        initComponents();
        pegawaiCombo();
        menuCombo();
        loadTabel();
        loadRiwayat();
        loadMenu();
        resetFormKasir();
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        popupMenu1 = new java.awt.PopupMenu();
        popupMenu2 = new java.awt.PopupMenu();
        popupMenu3 = new java.awt.PopupMenu();
        popupMenu4 = new java.awt.PopupMenu();
        popupMenu5 = new java.awt.PopupMenu();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        custLabel2 = new javax.swing.JLabel();
        jumlahCust = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jamValue = new javax.swing.JTextField();
        totalItemsValue = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        subTotalValue = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        employeeValue = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        employeeLabel = new javax.swing.JLabel();
        buttonProses = new javax.swing.JButton();
        custLabel = new javax.swing.JLabel();
        totalPPNValue = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        custValue = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        menuValue = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        menuLabel = new javax.swing.JLabel();
        serviceTotalValue = new javax.swing.JTextField();
        qtyInputLabel = new javax.swing.JLabel();
        qtyInputValue = new javax.swing.JTextField();
        priceTotalValue = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        invoiceLabel = new javax.swing.JLabel();
        mejaValue = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        qtyInputLabel1 = new javax.swing.JLabel();
        removeButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        discValue = new javax.swing.JTextField();
        ppnLabel = new javax.swing.JLabel();
        idTransaksi = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        custLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jenisValue = new javax.swing.JComboBox<>();
        serviceLabel = new javax.swing.JLabel();
        discLabel = new javax.swing.JLabel();
        tanggalValue = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelRiwayat = new javax.swing.JTable();
        buttonUbahRiwayat = new javax.swing.JButton();
        buttonPrint = new javax.swing.JButton();
        buttonHapusRiwayat = new javax.swing.JButton();
        waktuBayar = new javax.swing.JTextField();
        jumlahBayar = new javax.swing.JTextField();
        metodeBayar = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        kembalianBayar = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        totalHarga = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableMenu = new javax.swing.JTable();
        jenisMenu = new javax.swing.JComboBox<>();
        idMenu = new javax.swing.JTextField();
        namaMenu = new javax.swing.JTextField();
        hargaMenu = new javax.swing.JTextField();
        deskripsiMenu = new javax.swing.JTextField();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();

        jButton4.setText("jButton4");

        popupMenu1.setLabel("popupMenu1");

        popupMenu2.setLabel("popupMenu2");

        popupMenu3.setLabel("popupMenu3");

        popupMenu4.setLabel("popupMenu4");

        popupMenu5.setLabel("popupMenu5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        custLabel2.setText("Jml Cust");

        jumlahCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlahCustActionPerformed(evt);
            }
        });

        jLabel10.setText("%");

        jLabel11.setText("Total Items");

        totalItemsValue.setText("0");
        totalItemsValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalItemsValueActionPerformed(evt);
            }
        });

        jLabel1.setText("Tanggal:");

        subTotalValue.setText("0");

        jLabel2.setText("Jam");

        employeeValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        employeeValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeValueActionPerformed(evt);
            }
        });

        jLabel12.setText("Sub Total");

        employeeLabel.setText("Employee");

        buttonProses.setText("Process");
        buttonProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonProsesActionPerformed(evt);
            }
        });

        custLabel.setText("Customer");

        totalPPNValue.setText("0");

        custValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custValueActionPerformed(evt);
            }
        });

        jLabel15.setText("Total PPN");

        menuValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        menuValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuValueActionPerformed(evt);
            }
        });

        jLabel16.setText("Total Service");

        menuLabel.setText("Menu");

        serviceTotalValue.setText("0");

        qtyInputLabel.setText("Quantity");

        qtyInputValue.setText("0");
        qtyInputValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyInputValueActionPerformed(evt);
            }
        });

        priceTotalValue.setText("0");

        jLabel17.setText("Total Price");

        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Menu", "Name", "Type", "Unit Price", "Qty", "Total Price"
            }
        ));
        jScrollPane1.setViewportView(tabelTransaksi);

        invoiceLabel.setText("ID Transaksi:");

        mejaValue.setText("0");
        mejaValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mejaValueActionPerformed(evt);
            }
        });

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        qtyInputLabel1.setText("No Meja");

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        removeAllButton.setText("Remove All");
        removeAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllButtonActionPerformed(evt);
            }
        });

        discValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discValueKeyReleased(evt);
            }
        });

        ppnLabel.setText("PPN:");

        idTransaksi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                idTransaksiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                idTransaksiFocusLost(evt);
            }
        });
        idTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idTransaksiKeyReleased(evt);
            }
        });

        jLabel6.setText("10%");

        custLabel1.setText("Jenis Pemesanan:");

        jLabel7.setText("5%");

        jenisValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dine In", "Take Away"}));

        serviceLabel.setText("Service:");

        discLabel.setText("Discount:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(209, 209, 209)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(invoiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(custLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(custValue, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(custLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jumlahCust, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(84, 84, 84)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(qtyInputLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mejaValue))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(custLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jenisValue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(menuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(menuValue, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(81, 81, 81)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(qtyInputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(qtyInputValue, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jamValue, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(tanggalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(employeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(employeeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(discLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ppnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(serviceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(discValue, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(131, 131, 131)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(totalItemsValue, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(subTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(totalPPNValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(serviceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(buttonProses)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(priceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(removeAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(511, 511, 511))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap(1505, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(67, 67, 67)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invoiceLabel)
                    .addComponent(idTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeLabel))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custLabel)
                            .addComponent(custValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custLabel2)
                            .addComponent(jumlahCust, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custLabel1)
                            .addComponent(jenisValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qtyInputLabel1)
                            .addComponent(mejaValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(tanggalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jamValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(menuValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(menuLabel)
                    .addComponent(qtyInputLabel)
                    .addComponent(qtyInputValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(ppnLabel))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(serviceLabel)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(discLabel)
                                    .addComponent(jLabel10)
                                    .addComponent(discValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(totalItemsValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(subTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(totalPPNValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(priceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(serviceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonProses, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(148, 148, 148))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(651, 651, 651)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jTabbedPane2.addTab("KASIR", jPanel1);

        tabelRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Transaksi", "Tanggal", "Waktu Pemesanan", "Waktu Pembayaran", "Nama Customer", "Metode Pembayaran", "Jenis Pemesanan", "Jumlah Customer", "Total Harga", "PPN", "Service", "No Meja", "Status", "Id Pegawai"
            }
        ));
        tabelRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelRiwayatMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelRiwayat);

        buttonUbahRiwayat.setText("Ubah");
        buttonUbahRiwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUbahRiwayatActionPerformed(evt);
            }
        });

        buttonPrint.setText("Bayar & Print");
        buttonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintActionPerformed(evt);
            }
        });

        buttonHapusRiwayat.setText("Hapus");
        buttonHapusRiwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHapusRiwayatActionPerformed(evt);
            }
        });

        jumlahBayar.setText("0");
        jumlahBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jumlahBayarKeyReleased(evt);
            }
        });

        metodeBayar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "E-Wallet", "Debit", "Credit", "Qris" }));
        metodeBayar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                metodeBayarItemStateChanged(evt);
            }
        });
        metodeBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodeBayarActionPerformed(evt);
            }
        });

        jLabel13.setText("Waktu Bayar:");

        jLabel14.setText("Metode Bayar:");

        jLabel18.setText("Jumlah Bayar:");

        kembalianBayar.setText("0");

        jLabel19.setText("Kembalian:");

        jLabel20.setText("Total Harga:");

        totalHarga.setText("0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonUbahRiwayat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonHapusRiwayat)
                        .addGap(778, 778, 778)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel14)
                                                .addGap(18, 18, 18)
                                                .addComponent(metodeBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel20)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(totalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(waktuBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(kembalianBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(buttonPrint, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)))
                .addContainerGap(165, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonUbahRiwayat)
                            .addComponent(buttonHapusRiwayat)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(waktuBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(kembalianBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(metodeBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addComponent(buttonPrint)))
                .addContainerGap(228, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("RIWAYAT TRANSAKSI", jPanel2);

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
        jScrollPane3.setViewportView(tableMenu);

        jenisMenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Signature", "Add On", "Drink"}));
        jenisMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jenisMenuActionPerformed(evt);
            }
        });

        namaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaMenuActionPerformed(evt);
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

        jLabel5.setText("Id Menu");

        jLabel3.setText("Nama Menu");

        jLabel4.setText("Jenis");

        jLabel8.setText("Harga");

        jLabel9.setText("Deskripsi");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(221, 221, 221)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(btnTambah)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnUbah)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnHapus))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jenisMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hargaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deskripsiMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(205, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(idMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(namaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jenisMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(hargaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(deskripsiMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnUbah)
                            .addComponent(btnHapus)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(348, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("MENU", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1546, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintActionPerformed
        // TODO add your handling code here:
        int selectedRow = tabelRiwayat.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diproses!");
            return;
        }
        
        String id_transaksi = tabelRiwayat.getValueAt(selectedRow, 0).toString();
        String metode_bayar = (String) metodeBayar.getSelectedItem();
        String jumlah_bayar = jumlahBayar.getText();
        
        String total_harga = totalHarga.getText();
        Double kembalian = Double.parseDouble(total_harga) - Double.parseDouble(jumlah_bayar);
        
        kembalianBayar.setText(kembalian.toString());
        
        
        if (jumlah_bayar.equals("0") && metode_bayar.equals("Cash"))  {
            JOptionPane.showMessageDialog(this, "Harap isi Jumlah Bayar!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin memproses transaksi dengan ID: " + id_transaksi + "?", 
                "Konfirmasi Penghapusan", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                String id_trans = idTransaksi.getText().trim();

                SimpleDateFormat sdfTanggal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tanggal = sdfTanggal.format(tanggalValue.getDate());

                String waktu_pesan = jamValue.getText().trim();
                String customer = custValue.getText().trim();
                String jenis_pesan = (String) jenisValue.getSelectedItem();
                String no_meja = mejaValue.getText().trim();
                String jumlah_cust = jumlahCust.getText().trim();
                String total = priceTotalValue.getText().trim();
                String ppn = totalPPNValue.getText().trim();
                String service = serviceTotalValue.getText().trim();
                String status = "Unpaid";
                String pegawai = (String) employeeValue.getSelectedItem();

                
                pst = getKoneksi().prepareStatement("INSERT INTO transaksi (no_transaksi, tanggal_transaksi, waktu_pemesanan,nama_customer, jenis_pemesanan, nomor_meja, jumlah_customer, total_transaksi, total_ppn, total_service, status_transaksi, id_pegawai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pst.setString(1, id_trans);
                pst.setString(2, tanggal);
                pst.setString(3, waktu_pesan);
                pst.setString(4, customer);
                pst.setString(5, jenis_pesan);
                pst.setString(6, no_meja);
                pst.setString(7, jumlah_cust);
                pst.setString(8, total);
                pst.setString(9, ppn);
                pst.setString(10, service);
                pst.setString(11, status);



                String sql = "SELECT id_pegawai FROM pegawai WHERE nama_pegawai = ?";
                PreparedStatement p = getKoneksi().prepareStatement(sql);
                p.setString(1, pegawai);
                ResultSet rs = p.executeQuery();

                String id_pegawai = "";

                if (rs.next()) {
                    id_pegawai = rs.getString("id_pegawai");
                } else {
                    JOptionPane.showMessageDialog(this, "Pegawai ditemukan untuk : " + pegawai);
                }
                rs.close();
                p.close();

                pst.setString(12, id_pegawai);

                int k = pst.executeUpdate();
                if (k == 1) {
                    JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                    resetFormKasir();
                    loadRiwayat();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan data!");
                }
                pst.close();
                loadTabel();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
            }
        }    
    }//GEN-LAST:event_buttonPrintActionPerformed

    private void buttonUbahRiwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUbahRiwayatActionPerformed
     int selectedRow = tabelRiwayat.getSelectedRow();
        int selectedColumn = tabelRiwayat.getSelectedColumn();

        if (selectedRow == -1 || selectedColumn == -1) {
           JOptionPane.showMessageDialog(this, "Pilih baris dan kolom yang ingin diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
           return;
        }

        if (selectedColumn == 0) {
           JOptionPane.showMessageDialog(this, "Kolom Nomor Transaksi tidak dapat diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
           return;
        }

        int nomorTransaksi = (int) tabelRiwayat.getValueAt(selectedRow, 0);
        String columnName = tabelRiwayat.getColumnName(selectedColumn);


        String newValue = JOptionPane.showInputDialog(this, "Masukkan nilai baru untuk " + columnName + ":", tabelRiwayat.getValueAt(selectedRow, selectedColumn));

        if (newValue == null) {
           JOptionPane.showMessageDialog(this, "Proses perubahan dibatalkan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           return;
        }

        try {
            Connection c = getKoneksi();

            String[] dbColumns = {"nomor_transaksi", "tanggal_transaksi", "waktu_pemesanan", "waktu_pembayaran", "nama_customer", 
                                   "metode_pembayaran", "jenis_pemesanan", "jumlah_customer", "total_transaksi", "total_ppn", 
                                   "total_service", "nomor_meja", "status_transaksi", "id_pegawai"};

            String dbColumnName = dbColumns[selectedColumn];

            String sql = "UPDATE transaksi SET " + dbColumnName + " = ? WHERE nomor_transaksi = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            if (selectedColumn == 1) {
                ps.setDate(1, java.sql.Date.valueOf(newValue)); 
            } else if (selectedColumn == 2 || selectedColumn == 3) {
                ps.setTime(1, java.sql.Time.valueOf(newValue)); 
            } else if (selectedColumn == 7 || selectedColumn == 11 || selectedColumn == 13) {
                ps.setInt(1, Integer.parseInt(newValue)); 
            } else if (selectedColumn == 8 || selectedColumn == 9 || selectedColumn == 10) {
                ps.setDouble(1, Double.parseDouble(newValue)); 
            } else {
                ps.setString(1, newValue);
            }

            ps.setInt(2, nomorTransaksi);

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal diubah.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            ps.close();
            c.close();

            loadRiwayat();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonUbahRiwayatActionPerformed

    private void idTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idTransaksiKeyReleased
        // TODO add your handling code here:
        loadTabel();
    }//GEN-LAST:event_idTransaksiKeyReleased

    private void idTransaksiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idTransaksiFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_idTransaksiFocusLost

    private void idTransaksiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idTransaksiFocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_idTransaksiFocusGained

    private void discValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discValueKeyReleased
        // TODO add your handling code here:
        int diskon;
        if(discValue.getText().trim().equals("")){
            diskon = 0;
        } else {
            diskon = Integer.parseInt(discValue.getText().trim());
        }

        double total = Double.parseDouble(priceTotalValue.getText().trim());
        double totalDiskon = total*(diskon/100.0);
        total = total - totalDiskon;
        priceTotalValue.setText(String.valueOf(total));
    }//GEN-LAST:event_discValueKeyReleased

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed
        // TODO add your handling code here:
        String id_trans = idTransaksi.getText().trim();

        if (id_trans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Transaksi terlebih dahulu.", "Perhatian!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus semua data untuk ID Transaksi: " + id_trans + "?",
            "Konfirmasi Hapus Semua",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection c = getKoneksi();
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Koneksi ke database gagal!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String sql = "DELETE FROM detail_transaksi WHERE Nomor_transaksi = ?";
                PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, id_trans);

                int rowsAffected = p.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Semua data untuk ID Transaksi: " + id_trans + " berhasil dihapus.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

                    loadTabel();
                    //                resetForm();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Tidak ada data ditemukan untuk ID Transaksi: " + id_trans,
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE);
                }

                p.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan saat menghapus data: " + e.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = tabelTransaksi.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus!");
            return;
        }

        try {
            String id_trans = idTransaksi.getText().trim();
            String id_menu = (String) tabelTransaksi.getValueAt(selectedRow, 0);

            if (id_trans.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi ID Transaksi terlebih dahulu!");
                return;
            }

            Connection c = getKoneksi();
            String sql = "DELETE FROM detail_transaksi WHERE Nomor_Transaksi = ? AND ID_Menu = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, id_trans);
            pst.setString(2, id_menu);

            int k = pst.executeUpdate();

            if (k > 0) {
                DefaultTableModel model = (DefaultTableModel) tabelTransaksi.getModel();
                model.removeRow(selectedRow);

                JOptionPane.showMessageDialog(this, "Item berhasil dihapus dari transaksi!");

                loadTabel();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus item!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        try {
            String id_trans = idTransaksi.getText().trim();
            String nama_menu = (String) menuValue.getSelectedItem();
            String jumlah_item = qtyInputValue.getText().trim();

            if (jumlah_item.equals("0")||id_trans.isEmpty())  {
                JOptionPane.showMessageDialog(this, "Harap isi ID Transaksi dan jumlah item menu!");
                return;
            }

            String id_menu = "";
            pst = getKoneksi().prepareStatement("SELECT id_menu FROM menu WHERE nama_menu = ?");
            pst.setString(1, nama_menu);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id_menu = rs.getString("id_menu");
            } else {
                JOptionPane.showMessageDialog(this, "Menu tidak ditemukan!");
                return;
            }

            pst = getKoneksi().prepareStatement("INSERT INTO detail_transaksi (Nomor_Transaksi, ID_Menu, Jumlah_Beli) VALUES (?, ?, ?)");
            pst.setString(1, id_trans);
            pst.setString(2, id_menu);
            pst.setString(3, jumlah_item);

            int k = pst.executeUpdate();
            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data!");
            }
            loadTabel();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void mejaValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mejaValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mejaValueActionPerformed

    private void qtyInputValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyInputValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyInputValueActionPerformed

    private void menuValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuValueActionPerformed

    private void custValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_custValueActionPerformed

    private void buttonProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonProsesActionPerformed
        // TODO add your handling code here:
        try {
            String id_trans = idTransaksi.getText().trim();

            SimpleDateFormat sdfTanggal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tanggal = sdfTanggal.format(tanggalValue.getDate());
            
            String waktu_pesan = jamValue.getText().trim();
            String customer = custValue.getText().trim();
            String jenis_pesan = (String) jenisValue.getSelectedItem();
            String no_meja = mejaValue.getText().trim();
            String jumlah_cust = jumlahCust.getText().trim();
            String total = priceTotalValue.getText().trim();
            String ppn = totalPPNValue.getText().trim();
            String service = serviceTotalValue.getText().trim();
            String status = "Unpaid";
            String pegawai = (String) employeeValue.getSelectedItem();

            if (id_trans.isEmpty())  {
                JOptionPane.showMessageDialog(this, "Harap isi ID Transaksi!");
                return;
            }

            if ((no_meja.equals("0")&&jenis_pesan.equals("Dine In")))  {
                JOptionPane.showMessageDialog(this, "Harap isi No Meja!");
                return;
            }
            
            if (customer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi Nama Customer!");
                return;
            }
            
            if (jumlah_cust.equals("0")&&jenis_pesan.equals("Dine In")) {
                JOptionPane.showMessageDialog(this, "Harap isi Jumlah Customer!");
                return;
            }

            pst = getKoneksi().prepareStatement("INSERT INTO transaksi (no_transaksi, tanggal_transaksi, waktu_pemesanan,nama_customer, jenis_pemesanan, nomor_meja, jumlah_customer, total_transaksi, total_ppn, total_service, status_transaksi, id_pegawai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, id_trans);
            pst.setString(2, tanggal);
            pst.setString(3, waktu_pesan);
            pst.setString(4, customer);
            pst.setString(5, jenis_pesan);
            pst.setString(6, no_meja);
            pst.setString(7, jumlah_cust);
            pst.setString(8, total);
            pst.setString(9, ppn);
            pst.setString(10, service);
            pst.setString(11, status);
            
            

            String sql = "SELECT id_pegawai FROM pegawai WHERE nama_pegawai = ?";
            PreparedStatement p = getKoneksi().prepareStatement(sql);
            p.setString(1, pegawai);
            ResultSet rs = p.executeQuery();

            String id_pegawai = "";

            if (rs.next()) {
                id_pegawai = rs.getString("id_pegawai");
            } else {
                JOptionPane.showMessageDialog(this, "Pegawai ditemukan untuk : " + pegawai);
            }
            rs.close();
            p.close();
            
            pst.setString(12, id_pegawai);

            int k = pst.executeUpdate();
            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                resetFormKasir();
                loadRiwayat();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data!");
            }
            pst.close();
            loadTabel();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
        }
    }//GEN-LAST:event_buttonProsesActionPerformed

    private void employeeValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeValueActionPerformed

    private void totalItemsValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalItemsValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalItemsValueActionPerformed

    private void jumlahCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlahCustActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahCustActionPerformed

    private void buttonHapusRiwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHapusRiwayatActionPerformed
        // TODO add your handling code here:
        int selectedRow = tabelRiwayat.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!");
            return;
        }

        String id_transaksi = tabelRiwayat.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus transaksi dengan ID: " + id_transaksi + "?", 
                "Konfirmasi Penghapusan", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection c = getKoneksi();
                String sql = "DELETE FROM transaksi WHERE no_transaksi = ?";
                PreparedStatement pst = c.prepareStatement(sql);
                pst.setString(1, id_transaksi);

                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
                    loadRiwayat(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Transaksi gagal dihapus!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_buttonHapusRiwayatActionPerformed

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

    private void namaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaMenuActionPerformed

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

    private void tabelRiwayatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelRiwayatMouseClicked
        // TODO add your handling code here:
        int selectedRow = tabelRiwayat.getSelectedRow();
        String total_bayar = tabelRiwayat.getValueAt(selectedRow, 8).toString();

        totalHarga.setText(total_bayar);
        
        String metode_bayar = (String) metodeBayar.getSelectedItem();
        if (!metode_bayar.equals("Cash")) {
            jumlahBayar.setText(total_bayar);
        }
    }//GEN-LAST:event_tabelRiwayatMouseClicked

    private void jumlahBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahBayarKeyReleased
        // TODO add your handling code here:
        int selectedRow = tabelRiwayat.getSelectedRow();
        
        String metode_bayar = (String) metodeBayar.getSelectedItem();
        
        String jumlah_bayar = jumlahBayar.getText();
        
        String total_harga = totalHarga.getText();
        Double kembalian = Double.parseDouble(total_harga) - Double.parseDouble(jumlah_bayar);
        
        kembalianBayar.setText(kembalian.toString());
    }//GEN-LAST:event_jumlahBayarKeyReleased

    private void metodeBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodeBayarActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_metodeBayarActionPerformed

    private void metodeBayarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_metodeBayarItemStateChanged
        // TODO add your handling code here:
        String metode_bayar = (String) metodeBayar.getSelectedItem();
        
        if(!metode_bayar.equals("Cash")){
            String total_harga = totalHarga.getText();
            jumlahBayar.setText(total_harga);
        } else {
            jumlahBayar.setText("0");
        }
    }//GEN-LAST:event_metodeBayarItemStateChanged
                                                      
    
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
            java.util.logging.Logger.getLogger(kasirForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(kasirForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(kasirForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(kasirForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new kasirForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JButton buttonHapusRiwayat;
    private javax.swing.JButton buttonPrint;
    private javax.swing.JButton buttonProses;
    private javax.swing.JButton buttonUbahRiwayat;
    private javax.swing.JLabel custLabel;
    private javax.swing.JLabel custLabel1;
    private javax.swing.JLabel custLabel2;
    private javax.swing.JTextField custValue;
    private javax.swing.JTextField deskripsiMenu;
    private javax.swing.JLabel discLabel;
    private javax.swing.JTextField discValue;
    private javax.swing.JLabel employeeLabel;
    private javax.swing.JComboBox<String> employeeValue;
    private javax.swing.JTextField hargaMenu;
    private javax.swing.JTextField idMenu;
    private javax.swing.JTextField idTransaksi;
    private javax.swing.JLabel invoiceLabel;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jamValue;
    private javax.swing.JComboBox<String> jenisMenu;
    private javax.swing.JComboBox<String> jenisValue;
    private javax.swing.JTextField jumlahBayar;
    private javax.swing.JTextField jumlahCust;
    private javax.swing.JTextField kembalianBayar;
    private javax.swing.JTextField mejaValue;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JComboBox<String> menuValue;
    private javax.swing.JComboBox<String> metodeBayar;
    private javax.swing.JTextField namaMenu;
    private java.awt.PopupMenu popupMenu1;
    private java.awt.PopupMenu popupMenu2;
    private java.awt.PopupMenu popupMenu3;
    private java.awt.PopupMenu popupMenu4;
    private java.awt.PopupMenu popupMenu5;
    private javax.swing.JLabel ppnLabel;
    private javax.swing.JTextField priceTotalValue;
    private javax.swing.JLabel qtyInputLabel;
    private javax.swing.JLabel qtyInputLabel1;
    private javax.swing.JTextField qtyInputValue;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JLabel serviceLabel;
    private javax.swing.JTextField serviceTotalValue;
    private javax.swing.JTextField subTotalValue;
    private javax.swing.JTable tabelRiwayat;
    private javax.swing.JTable tabelTransaksi;
    private javax.swing.JTable tableMenu;
    private com.toedter.calendar.JDateChooser tanggalValue;
    private javax.swing.JTextField totalHarga;
    private javax.swing.JTextField totalItemsValue;
    private javax.swing.JTextField totalPPNValue;
    private javax.swing.JTextField waktuBayar;
    // End of variables declaration//GEN-END:variables
}
