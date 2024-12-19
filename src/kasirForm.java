import java.sql.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
            total = totalPrice - (totalPPN + totalService);
            
            priceTotalValue.setText(String.valueOf(total));
            subTotalValue.setText(String.valueOf(totalPrice));
            totalPPNValue.setText(String.valueOf(totalPPN));
            serviceTotalValue.setText(String.valueOf(totalService));
            totalItemsValue.setText(String.valueOf(totalItem));
        } catch(SQLException e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }
    
    
    
    public kasirForm() {
        initComponents();
        pegawaiCombo();
        menuCombo();
        loadTabel();
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
        employeeValue = new javax.swing.JComboBox<>();
        employeeLabel = new javax.swing.JLabel();
        custLabel = new javax.swing.JLabel();
        custValue = new javax.swing.JTextField();
        menuValue = new javax.swing.JComboBox<>();
        menuLabel = new javax.swing.JLabel();
        qtyInputLabel = new javax.swing.JLabel();
        qtyInputValue = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        ppnLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        serviceLabel = new javax.swing.JLabel();
        discLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        totalItemsValue = new javax.swing.JTextField();
        subTotalValue = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        buttonProses = new javax.swing.JButton();
        totalPPNValue = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        serviceTotalValue = new javax.swing.JTextField();
        priceTotalValue = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        invoiceLabel = new javax.swing.JLabel();
        mejaValue = new javax.swing.JTextField();
        qtyInputLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        discValue = new javax.swing.JTextField();
        idTransaksi = new javax.swing.JTextField();
        custLabel1 = new javax.swing.JLabel();
        jenisValue = new javax.swing.JComboBox<>();
        custLabel2 = new javax.swing.JLabel();
        jumlahCust = new javax.swing.JTextField();
        tanggalValue = new javax.swing.JTextField();
        jamValue = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jButton4.setText("jButton4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        employeeValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        employeeValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeValueActionPerformed(evt);
            }
        });

        employeeLabel.setText("Employee");

        custLabel.setText("Customer");

        custValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custValueActionPerformed(evt);
            }
        });

        menuValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        menuValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuValueActionPerformed(evt);
            }
        });

        menuLabel.setText("Menu");

        qtyInputLabel.setText("Quantity");

        qtyInputValue.setText("0");
        qtyInputValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyInputValueActionPerformed(evt);
            }
        });

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

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

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

        ppnLabel.setText("PPN:");

        jLabel6.setText("10%");

        jLabel7.setText("5%");

        serviceLabel.setText("Service:");

        discLabel.setText("Discount:");

        jLabel10.setText("%");

        jLabel11.setText("Total Items");

        totalItemsValue.setText("0");
        totalItemsValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalItemsValueActionPerformed(evt);
            }
        });

        subTotalValue.setText("0");

        jLabel12.setText("Sub Total");

        buttonProses.setText("Process");
        buttonProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonProsesActionPerformed(evt);
            }
        });

        totalPPNValue.setText("0");

        jLabel15.setText("Total PPN");

        jLabel16.setText("Total Service");

        serviceTotalValue.setText("0");

        priceTotalValue.setText("0");

        jLabel17.setText("Total Price");

        invoiceLabel.setText("ID Transaksi:");

        mejaValue.setText("0");
        mejaValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mejaValueActionPerformed(evt);
            }
        });

        qtyInputLabel1.setText("No Meja");

        jButton1.setText("Lihat Riwayat Transaksi");

        discValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discValueKeyReleased(evt);
            }
        });

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

        custLabel1.setText("Jenis Pemesanan:");

        jenisValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dine In", "Take Away"}));

        custLabel2.setText("Jml Cust");

        jumlahCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlahCustActionPerformed(evt);
            }
        });

        jLabel1.setText("Tanggal:");

        jLabel2.setText("Jam");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(invoiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(tanggalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(removeAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(buttonProses)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(discLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ppnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(serviceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(discValue, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(166, 166, 166)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(26, 26, 26)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(totalItemsValue, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(subTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(totalPPNValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(serviceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(priceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(menuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(menuValue, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(employeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(employeeValue, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(custLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(custValue, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(custLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jumlahCust, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(qtyInputLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(qtyInputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(qtyInputValue, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(mejaValue, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(custLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jenisValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(15, 15, 15)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jamValue, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(invoiceLabel)
                            .addComponent(idTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(employeeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(employeeLabel)
                            .addComponent(custLabel1)
                            .addComponent(jenisValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custLabel)
                            .addComponent(custValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(qtyInputLabel1)
                            .addComponent(mejaValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custLabel2)
                            .addComponent(jumlahCust, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(menuValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(menuLabel)
                            .addComponent(qtyInputLabel)
                            .addComponent(qtyInputValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(tanggalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jamValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllButton)))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(ppnLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(totalItemsValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serviceLabel)
                    .addComponent(jLabel7)
                    .addComponent(jLabel12)
                    .addComponent(subTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discLabel)
                    .addComponent(jLabel10)
                    .addComponent(discValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(totalPPNValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serviceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(buttonProses, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void custValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_custValueActionPerformed

    private void qtyInputValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyInputValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyInputValueActionPerformed

    private void buttonProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonProsesActionPerformed
        // TODO add your handling code here:
        try {
            String id_trans = idTransaksi.getText().trim();
            
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat sdfTanggal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//            String tanggal = sdfTanggal.format(cal.getTime()); 
//            
//            SimpleDateFormat sdfWaktu = new SimpleDateFormat("HH:mm:ss");
//            tanggalValue.setText(sdfWaktu.format(cal.getTime()));
           
            String tanggal = tanggalValue.getText().trim();
            String waktu_pesan = jamValue.getText().trim();;
//            Locale locale = new Locale("fr", "FR");
//            DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale); // Menggunakan locale Prancis
//            waktu_pesan = dateFormat.format(new Date());
            
            String waktu_bayar = null;
            String customer = custValue.getText().trim();
            String metode = null;
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
            
            if ((no_meja.equals("0")&&jenis_pesan.equals("Take Away")))  {
                JOptionPane.showMessageDialog(this, "Harap isi No Meja!");
                return;
            } 
            
            if (jumlah_cust.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi Jumlah Customer!");
                return;
            }


            pst = getKoneksi().prepareStatement("INSERT INTO transaksi (no_transaksi, tanggal_transaksi, waktu_pemesanan, waktu_pembayaran, nama_customer, metode_pembayaran, jenis_pemesanan, nomor_meja, jumlah_customer, total_transaksi, total_ppn, total_service, status_transaksi, id_pegawai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, id_trans);
            pst.setString(2, tanggal);
            pst.setString(3, waktu_pesan);
            pst.setNull(4, java.sql.Types.TIMESTAMP);
            pst.setString(5, customer);
            pst.setNull(6, java.sql.Types.VARCHAR);
            pst.setString(7, jenis_pesan);
            pst.setString(8, no_meja);
            pst.setString(9, jumlah_cust);
            pst.setString(10, total);
            pst.setString(11, ppn);
            pst.setString(12, service);
            pst.setString(13, status);
            pst.setString(14, pegawai);

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
    }//GEN-LAST:event_buttonProsesActionPerformed

    private void menuValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuValueActionPerformed

    private void mejaValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mejaValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mejaValueActionPerformed

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

    private void idTransaksiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idTransaksiFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_idTransaksiFocusLost

    private void idTransaksiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idTransaksiFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_idTransaksiFocusGained

    private void idTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idTransaksiKeyReleased
        // TODO add your handling code here:
        loadTabel();
    }//GEN-LAST:event_idTransaksiKeyReleased

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

    private void employeeValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeValueActionPerformed

    private void jumlahCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlahCustActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahCustActionPerformed

    private void totalItemsValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalItemsValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalItemsValueActionPerformed

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
    private javax.swing.JButton buttonProses;
    private javax.swing.JLabel custLabel;
    private javax.swing.JLabel custLabel1;
    private javax.swing.JLabel custLabel2;
    private javax.swing.JTextField custValue;
    private javax.swing.JLabel discLabel;
    private javax.swing.JTextField discValue;
    private javax.swing.JLabel employeeLabel;
    private javax.swing.JComboBox<String> employeeValue;
    private javax.swing.JTextField idTransaksi;
    private javax.swing.JLabel invoiceLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jamValue;
    private javax.swing.JComboBox<String> jenisValue;
    private javax.swing.JTextField jumlahCust;
    private javax.swing.JTextField mejaValue;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JComboBox<String> menuValue;
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
    private javax.swing.JTable tabelTransaksi;
    private javax.swing.JTextField tanggalValue;
    private javax.swing.JTextField totalItemsValue;
    private javax.swing.JTextField totalPPNValue;
    // End of variables declaration//GEN-END:variables
}
