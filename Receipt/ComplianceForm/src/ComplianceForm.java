import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class ComplianceForm extends JDialog {
    private JTextField tfName;
    private JTextField tfAddress;
    private JTextField tfCellPhone;
    private JTextField tdID;
    private JTextField tfITIN;
    private JTextField tfDoB;
    private JTextField tfCheckN;
    private JTextField tfCheckA;
    private JButton btnAdd;
    private JButton btnPrint;
    private JPanel compliancePanel;
    private JSpinner spinner1;

    public ComplianceForm(JFrame parent) {
        super(parent);
        setTitle("Create new Compliance Form");
        setContentPane(compliancePanel);
        setMinimumSize(new Dimension(450, 475));
        setModal(true);
        setLocationRelativeTo(parent);
        //setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printUser();
            }
        });
        setVisible(true);
    }

    private void printUser() {
    }

    private void addUser() {
        String name = tfName.getText();
        String address = tfAddress.getText();
        String cellphone = tfCellPhone.getText();
        String id = tdID.getText();
        String itin = tfITIN.getText();
        String dob = tfDoB.getText();

        if (name.isEmpty() || address.isEmpty() || cellphone.isEmpty() || id.isEmpty() || itin.isEmpty() || dob.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        user = addUserToDatabase(name,address,cellphone,id,itin,dob);
        if (user != null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;
    private User addUserToDatabase(String name, String address, String cellphone, String id, String itin, String dob){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306";
        final String USERNAME = "root";
        final String PASSWORD = "Pedro1997";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, address, cellphone, id, itin, dob)" +
                    "VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, cellphone);
            preparedStatement.setString(4,id);
            preparedStatement.setString(5, itin);
            preparedStatement.setString(6,dob);

            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0){
                user = new User();
                user.name = name;
                user.address = address;
                user.cellphone = cellphone;
                user.id = id;
                user.itin = itin;
                user.dob = dob;
            }
            stmt.close();
            conn.close();


        }   catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }
        public static void main (String[]args){
            ComplianceForm myForm = new ComplianceForm(null);
            User user = myForm.user;
            if(user != null){
                System.out.println("Successful registration of" + user.name);
            }
            else{
                System.out.println("Registration canceled");
            }
        }
    }

