package Form;

import koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormJuri extends javax.swing.JFrame {
    private int idJuri;
    private int idPesertaDipilih = 0;

    public FormJuri(int idJuri) {
        this.idJuri = idJuri;
        initComponents();
        setLocationRelativeTo(null);
        loadDataPeserta();
    }

    private void loadDataPeserta() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Pendaftaran");
        model.addColumn("Username Peserta");
        model.addColumn("Nama Lomba");
        model.addColumn("Status Pendaftaran");
        model.addColumn("Status Nilai");
        model.addColumn("Catatan Juri");

        // Query JOIN untuk menggabungkan data dari 3 tabel (pendaftaran, users, lomba)
        String sql = "SELECT p.id, u.username, l.nama_lomba, p.status, p.status_nilai, p.catatan " +
                     "FROM pendaftaran p " +
                     "JOIN users u ON p.user_id = u.id " +
                     "JOIN lomba l ON p.lomba_id = l.id";

        try (Connection c = Koneksi.configDB();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {
            
            while (r.next()) {
                model.addRow(new Object[]{
                    r.getInt("id"),
                    r.getString("username"),   // Diambil dari tabel users
                    r.getString("nama_lomba"), // Diambil dari tabel lomba
                    r.getString("status"),
                    r.getString("status_nilai"),
                    r.getString("catatan")
                });
            }
            jTablePeserta.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data peserta: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePeserta = new javax.swing.JTable();
        cmbStatus = new javax.swing.JComboBox<>();
        txtCatatan = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel1.setText("PANEL PENILAIAN JURI - BI GOT TALENT");

        jTablePeserta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {}
        ));
        jTablePeserta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePesertaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTablePeserta);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lolos", "Tidak Lolos" }));

        btnSimpan.setText("Simpan Penilaian");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel2.setText("Status Nilai:");

        jLabel3.setText("Catatan / Alasan:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCatatan)))
                    .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(btnSimpan)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    private void jTablePesertaMouseClicked(java.awt.event.MouseEvent evt) {
        int baris = jTablePeserta.getSelectedRow();
        if (baris != -1) {
            idPesertaDipilih = Integer.parseInt(jTablePeserta.getValueAt(baris, 0).toString());
            
            Object valStatus = jTablePeserta.getValueAt(baris, 4);
            Object valCatatan = jTablePeserta.getValueAt(baris, 5);

            if (valStatus != null && !valStatus.toString().equalsIgnoreCase("null")) {
                cmbStatus.setSelectedItem(valStatus.toString());
            }
            if (valCatatan != null && !valCatatan.toString().equalsIgnoreCase("null")) {
                txtCatatan.setText(valCatatan.toString());
            } else {
                txtCatatan.setText("");
            }
        }
    }

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        if (idPesertaDipilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih peserta terlebih dahulu dari tabel!");
            return;
        }

        String status = cmbStatus.getSelectedItem().toString();
        String catatan = txtCatatan.getText().trim();

        // Update penilaian ke tabel pendaftaran
        String sql = "UPDATE pendaftaran SET status_nilai = ?, catatan = ? WHERE id = ?";

        try (Connection c = Koneksi.configDB();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, catatan);
            ps.setInt(3, idPesertaDipilih);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil menyimpan penilaian!");
            loadDataPeserta(); // Refresh isi tabel
            
            // Reset input
            idPesertaDipilih = 0;
            txtCatatan.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
        }
    }

    // Variables declaration
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePeserta;
    private javax.swing.JTextField txtCatatan;
    // End of variables declaration
}