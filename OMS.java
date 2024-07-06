import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Home extends JFrame {
    private Container c;
    private JLabel labh, labN, labP, labE, labA, labM;
    private JTextField txtN, txtP, txtE;
    private JTextArea txtA;
    private JCheckBox cbT, cbC, cbJ, cbS, cbMs;
    private JButton btnSave, btnAdmin;

    Home() {
        c = getContentPane();
        c.setBackground(new Color(230, 240, 248));
        c.setLayout(new FlowLayout(FlowLayout.CENTER, 600, 5));
        Font f1 = new Font("Impact", Font.BOLD, 20);
        Font f = new Font("Arial", Font.BOLD, 20);

        labh = new JLabel("Welcome to My Cafe!!");
        btnAdmin = new JButton("Admin Login");
        labN = new JLabel("Name");
        txtN = new JTextField(20);
        labP = new JLabel("Phone no.");
        txtP = new JTextField(20);
        labE = new JLabel("Email Address");
        txtE = new JTextField(20);
        labA = new JLabel("Delivery Address");
        txtA = new JTextArea(3, 20);
        labM = new JLabel("Menu");
        cbT = new JCheckBox("Tea");
        cbC = new JCheckBox("Coffee");
        cbJ = new JCheckBox("Juice");
        cbS = new JCheckBox("Soda");
        cbMs = new JCheckBox("MilkShake");
        btnSave = new JButton("Place Order");

        labh.setFont(f1);
        btnAdmin.setFont(f);
        labN.setFont(f);
        txtN.setFont(f);
        labP.setFont(f);
        txtP.setFont(f);
        labE.setFont(f);
        txtE.setFont(f);
        labA.setFont(f);
        txtA.setFont(f);
        labM.setFont(f);
        cbT.setFont(f);
        cbC.setFont(f);
        cbJ.setFont(f);
        cbS.setFont(f);
        cbMs.setFont(f);
        btnSave.setFont(f);

        c.add(labh);
        c.add(btnAdmin);
        c.add(labN);
        c.add(txtN);
        c.add(labP);
        c.add(txtP);
        c.add(labE);
        c.add(txtE);
        c.add(labA);
        c.add(txtA);
        c.add(labM);
        c.add(cbT);
        c.add(cbC);
        c.add(cbJ);
        c.add(cbS);
        c.add(cbMs);
        c.add(btnSave);

        btnAdmin.addActionListener((ae) -> {
            AdminLoginFrame adminLoginFrame = new AdminLoginFrame(this);
            adminLoginFrame.setVisible(true);
        });

        btnSave.addActionListener((ae) -> {
            if (validateOrder()) {
                saveOrder();
            }
        });

this.addWindowListener(new java.awt.event.WindowAdapter() {
@Override
public void windowClosing(java.awt.event.WindowEvent windowEvent) {
if(JOptionPane.showConfirmDialog(c,
"Do u want to exit? ?", "Exit",
JOptionPane.YES_NO_OPTION,
JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

if(JOptionPane.showConfirmDialog(c,
"Are u sure?", "Exit",
JOptionPane.YES_NO_OPTION,
JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
System.exit(0);
}

}
}
});
        setSize(1300, 800);
        setTitle("Order Management System By Shruti ");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

private boolean validateOrder() {
    if (txtN.getText().isEmpty() || !txtN.getText().matches("[a-zA-Z]+")) {
        showError("Please enter a valid name with only alphabets.");
        return false;
    }

    String phoneText = txtP.getText();
    if (phoneText.isEmpty() || !phoneText.matches("\\d{10}")) {
        showError("Please enter a valid 10-digit phone number.");
        return false;
    }

    try {
        int phone = Integer.parseInt(phoneText);
    } catch (NumberFormatException e) {
        showError("Please enter a valid 10-digit phone number.");
        return false;
    }

    if (txtE.getText().isEmpty() || !txtE.getText().matches("[\\w.-]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
        showError("Please enter a valid email address.");
        return false;
    }

    if (txtA.getText().isEmpty()) {
        showError("Please enter a delivery address.");
        return false;
    }

    if (!(cbT.isSelected() || cbC.isSelected() || cbJ.isSelected() || cbS.isSelected() || cbMs.isSelected())) {
        showError("Please choose at least one item from the menu.");
        return false;
    }

    return true;
}


    private void saveOrder() {
    String name = txtN.getText();
    int phone = Integer.parseInt(txtP.getText());
    String email = txtE.getText();
    String address = txtA.getText();

    String choice = " ";
    if (cbT.isSelected()) choice += "Tea";
    if (cbC.isSelected()) choice += "Coffee";
    if (cbJ.isSelected()) choice += "Juice";
    if (cbS.isSelected()) choice += "Soda";
    if (cbMs.isSelected()) choice += "MilkShake";

    try {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

        String url = "jdbc:mysql://localhost:3306/cafe";
        Connection con = DriverManager.getConnection(url, "root", "abc456");

        String sql = "insert into customers values(?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, phone);
        pst.setString(3, email);
        pst.setString(4, address);
        pst.setString(5, choice);
        pst.executeUpdate();

        showThankYouDialog();

        txtN.setText("");
        txtP.setText("");
        txtE.setText("");
        txtA.setText("");
        txtN.requestFocus();
        cbT.setSelected(false);
        cbC.setSelected(false);
        cbJ.setSelected(false);
        cbS.setSelected(false);
        cbMs.setSelected(false);
        con.close();
    } catch (SQLException e) {
        showError("Database Issue: " + e.getMessage());
        e.printStackTrace();  // Print the stack trace for detailed error information
    }
}


    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(c, errorMessage, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showThankYouDialog() {
        JDialog dialog = new JDialog(this, "Thank You", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JLabel label = new JLabel("Thank You for Placing Order!");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        dialog.add(label);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.PLAIN, 18));

        okButton.addActionListener((e) -> {
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    class AdminLoginFrame extends JFrame {
        private Container c;
        private JLabel labheader;
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JButton loginButton;
        private Home parent;

        AdminLoginFrame(Home parent) {
            this.parent = parent;
            c = getContentPane();
            c.setBackground(new Color(230, 240, 248));
            Font f1 = new Font("Impact", Font.BOLD, 30);
            Font f = new Font("Arial", Font.BOLD, 30);
            setTitle("Admin Login");
            setSize(1300, 800);
            c.setLayout(new FlowLayout(FlowLayout.CENTER, 600, 5));

            labheader = new JLabel("Admin Login");
            usernameField = new JTextField(10);
            passwordField = new JPasswordField(10);
            loginButton = new JButton("Login");

            labheader.setFont(f1);
            usernameField.setFont(f);
            passwordField.setFont(f);
            loginButton.setFont(f);

            c.add(labheader);
            c.add(new JLabel("Username:"));
            c.add(usernameField);
            c.add(new JLabel("Password:"));
            c.add(passwordField);

            // Set a new FlowLayout for components below passwordField
            FlowLayout newLayout = new FlowLayout(FlowLayout.CENTER, 5, 5);
            JPanel buttonPanel = new JPanel(newLayout);
            buttonPanel.add(new JLabel("")); // Empty label for spacing
            buttonPanel.add(loginButton);
            c.add(buttonPanel);

            loginButton.addActionListener((ae) -> {
                if (validateAdminLogin()) {
                    JOptionPane.showMessageDialog(this, "Admin Login Successful!");
                    dispose();
                    OrderManagementFrame orderManagementFrame = new OrderManagementFrame(parent);
                    orderManagementFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Admin Credentials. Try Again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        private boolean validateAdminLogin() {
            String adminUsername = "shruti";
            String adminPassword = "shruti@123";

            String enteredUsername = usernameField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            return adminUsername.equals(enteredUsername) && adminPassword.equals(enteredPassword);
        }
    }
}

class OrderManagementFrame extends JFrame {
    private Container c;
    private JTextArea orderTextArea;
    private JButton btnDelete;
    private Home parent;

    OrderManagementFrame(Home parent) {
        this.parent = parent;
        c = getContentPane();
        c.setBackground(new Color(230, 240, 248));
        Font f = new Font("Arial", Font.BOLD, 20);
        setTitle("Order Management");
        setSize(1300, 800);
        c.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        orderTextArea = new JTextArea(20, 50);
        orderTextArea.setEditable(false);
        orderTextArea.setFont(f);

        btnDelete = new JButton("Delete Selected Order");
        btnDelete.setFont(f);

        c.add(new JScrollPane(orderTextArea));
        c.add(btnDelete);

        // Load and display orders
        displayOrders();

        btnDelete.addActionListener((ae) -> {
            deleteSelectedOrder();
            // Refresh the order list after deletion
            displayOrders();
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void displayOrders() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = "jdbc:mysql://localhost:3306/cafe";
            Connection con = DriverManager.getConnection(url, "root", "abc456");

            String sql = "select * from customers";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder orders = new StringBuilder();
            while (rs.next()) {
                String name = rs.getString("name");
                int phone = rs.getInt("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String choice = rs.getString("choice");

                orders.append("Name: ").append(name).append(", Phone: ").append(phone)
                        .append(", Email: ").append(email).append(", Address: ").append(address)
                        .append(", Choice: ").append(choice).append("\n\n");
            }

            orderTextArea.setText(orders.toString());

            con.close();
        } catch (SQLException e) {
            showError("Issue: " + e.getMessage());
        }
    }

   private void deleteSelectedOrder() {
        String selectedText = orderTextArea.getSelectedText();
        if (selectedText != null) {
            String[] lines = selectedText.split("\\n");
            String selectedName = lines[0].replace("Name: ", "");
            performDelete(selectedName);
        } else {
            showError("Please select an order to delete.");
        }
    }

    private void performDelete(String selectedName) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = "jdbc:mysql://localhost:3306/cafe";
            Connection con = DriverManager.getConnection(url, "root", "abc456");

            String sql = "DELETE FROM customers WHERE name=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, selectedName);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(c, "Order for " + selectedName + " deleted successfully.");

            con.close();
        } catch (SQLException e) {
            showError("Deletion Issue: " + e.getMessage());
        }
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(c, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    
}

class OMS {
    public static void main(String args[]) {
        Home h = new Home();
    }
}
