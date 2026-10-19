package Form;

import koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class FormSiswa extends javax.swing.JFrame {

    int idLoginSiswa;

    // Constructor bawaan NetBeans / GUI Builder
    public FormSiswa() {
        initComponents();
        setLocationRelativeTo(null);
    }

    // Constructor utama yang dipanggil dari FormLogin
    public FormSiswa(int idUser) {
        initComponents();
        setLocationRelativeTo(null);
        this.idLoginSiswa = idUser;
        loadPilihanLomba();
        cekStatusSiswa();
    }

    private void loadPilihanLomba() {
        cmbLomba.removeAllItems();
        String sql = "SELECT id, nama_lomba FROM lomba";

        try (Connection c = Koneksi.configDB();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {

            while (r.next()) {
                cmbLomba.addItem(r.getString("id") + " - " + r.getString("nama_lomba"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat lomba: " + e.getMessage());
        }
    }

    private void cekStatusSiswa() {
        String sql = "SELECT status FROM pendaftaran WHERE user_id = ?";

        try (Connection c = Koneksi.configDB();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idLoginSiswa);
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) {
                    String status = r.getString("status");
                    lblStatus.setText("Status Tahapan Anda: " + status);
                } else {
                    lblStatus.setText("Status Tahapan Anda: Belum Mendaftarkan Diri");
                }
            }
        } catch (Exception e) {
            lblStatus.setText("Status: Gagal memuat data");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbLomba = new javax.swing.JComboBox<>();
        btnDaftar = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        jLabel1.setText("HALAMAN SISWA - BI GOT TALENT");

        jLabel2.setText("Pilih Mata Lomba :");

        btnDaftar.setText("Daftar Lomba");
        btnDaftar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarActionPerformed(evt);
            }
        });

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblStatus.setText("Status Tahapan Anda: -");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatus)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbLomba, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDaftar)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLomba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDaftar))
                .addGap(25, 25, 25)
                .addComponent(lblStatus)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void btnDaftarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (cmbLomba.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilihan lomba tidak tersedia/kosong!");
            return;
        }

        try {
            String selectedItem = cmbLomba.getSelectedItem().toString();
            String idLomba = selectedItem.split(" - ")[0]; // Ambil ID lomba di depan

            // Cek apakah sudah pernah daftar
            String sqlCek = "SELECT id FROM pendaftaran WHERE user_id = ?";
            try (Connection c = Koneksi.configDB();
                 PreparedStatement psCek = c.prepareStatement(sqlCek)) {

                psCek.setInt(1, idLoginSiswa);
                try (ResultSet r = psCek.executeQuery()) {
                    if (r.next()) {
                        JOptionPane.showMessageDialog(this, "Anda sudah terdaftar di lomba!");
                        return;
                    }
                }

                // Simpan pendaftaran baru
                String sqlInsert = "INSERT INTO pendaftaran (user_id, lomba_id, status) VALUES (?, ?, 'Dokumen dalam Tinjauan')";
                try (PreparedStatement psInsert = c.prepareStatement(sqlInsert)) {
                    psInsert.setInt(1, idLoginSiswa);
                    psInsert.setInt(2, Integer.parseInt(idLomba));
                    psInsert.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Pendaftaran Berhasil! Menunggu verifikasi admin.");
                    cekStatusSiswa();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mendaftar: " + e.getMessage());
        }
    }                                         

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FormSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormSiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnDaftar;
    private javax.swing.JComboBox<String> cmbLomba;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration                   
}