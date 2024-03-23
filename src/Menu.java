import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(600,560);

        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);

        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;
    private Database database;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JComboBox keteranganComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel keteranganLabel;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

//        buat objek database
        database = new Database();



        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD,20f));

        // atur isi combo box
        String[] jenisKelaminData = {"","Laki-laki","Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        // atur isi keterangan combo box
        String[] keteranganData = {"","Lulus","Gagal"};
        keteranganComboBox.setModel(new DefaultComboBoxModel(keteranganData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1){
                    insertData();
                } else {
                    //selected indexnya mundur 1, jadi mesti ditambah 1
                    int id = getIdFromDatabase(selectedIndex);
                    updateData(id);
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0){
                    int choice = JOptionPane.showConfirmDialog(null, "Yakin mau dihapus?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        int id = getIdFromDatabase(selectedIndex);
                        deleteData(id);
                    }

                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0){
                    clearForm();
                }
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                    selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box

                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex,1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex,2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex,3).toString();
                String selectedKeterangan = mahasiswaTable.getModel().getValueAt(selectedIndex,4).toString();



                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                keteranganComboBox.setSelectedItem(selectedKeterangan);



                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No","NIM","Nama","Jenis Kelamin","Keterangan"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null,column);

        // isi tabel dengan listMahasiswa
        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");

            int i = 0;
            while (resultSet.next()){
                Object[] row = new Object[5];
                row[0] = i+1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenis_kelamin");
                row[4] = resultSet.getString("keterangan");


                temp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return temp; // return juga harus diganti
    }
    private int getIdFromDatabase(int selectedIndex) {
//        meng query untuk ngambil data id dari row yang di pilih
//        nge return data yang di inginkan
        try {
            ResultSet resultSet = database.selectQuery("SELECT id FROM mahasiswa LIMIT 1 OFFSET " + selectedIndex);
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return default value kalo semisal gagal
    }
    // Method untuk check apa semua fields telah di isi
    private boolean isFormFilled() {
        return !nimField.getText().isEmpty() &&
                !namaField.getText().isEmpty() &&
                jenisKelaminComboBox.getSelectedItem() != ""
                && keteranganComboBox.getSelectedItem() != "";
    }
    // Method to check if NIM already exists in the database
    private boolean isNIMExists(String nim) {
        try {
//            mengquery ke data base untuk nyari data dengan nim yang di inginkan
            ResultSet resultSet = database.selectQuery("SELECT COUNT(*) FROM mahasiswa WHERE nim = '" + nim + "'");
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false by default if there's an error or NIM doesn't exist
    }
    public void insertData() {
        if(isFormFilled()){
// ambil value dari textfield dan combobox
            String nim = nimField.getText();
            if(!isNIMExists(nim)){
                String nama = namaField.getText();
                String jeniskelamin = jenisKelaminComboBox.getSelectedItem().toString();
                String keterangan = keteranganComboBox.getSelectedItem().toString();

//        tambahkan data ke dalam database
                String sql = "INSERT INTO mahasiswa VALUES (null,'"+ nim +"','"+ nama +"','"+ jeniskelamin +"','"+ keterangan +"');";
                database.modifyQuery(sql);

                // update tabel
                mahasiswaTable.setModel((setTable()));

                // bersihkan form
                clearForm();

                // feedback
                System.out.println("Insert Berhasil");
                JOptionPane.showMessageDialog(null,"Data Berhasil Ditambahkan");
            }else {
                JOptionPane.showMessageDialog(null, "NIM sudah ada dalam database!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }else{
            JOptionPane.showMessageDialog(null, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void updateData(int id) {
        if(isFormFilled()){
            // ambil data dari form
            String nim = nimField.getText();

                String nama = namaField.getText();
                String jeniskelamin = jenisKelaminComboBox.getSelectedItem().toString();
                String keterangan = keteranganComboBox.getSelectedItem().toString();



//        ubah data kedalam database

                String sql = "UPDATE mahasiswa SET nim = '"+ nim + "',nama = '"+ nama + "',jenis_kelamin = '"+ jeniskelamin + "',keterangan = '"+ keterangan + "' WHERE id = '"+ id +"';";
                database.modifyQuery(sql);

                // update tabel
                mahasiswaTable.setModel(setTable());

                // bersihkan form
                clearForm();

                // feedback
                System.out.println("Update Berhasil");
                JOptionPane.showMessageDialog(null,"Data Berhasil Diubah!");


        }else {
            JOptionPane.showMessageDialog(null, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    public void deleteData(int id) {
        // hapus data dari database
        String sql = "DELETE FROM mahasiswa WHERE mahasiswa.id = '"+ id +"';";
        database.modifyQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete Berhasil");
        JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus!");

    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        keteranganComboBox.setSelectedItem("");



        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }


}
