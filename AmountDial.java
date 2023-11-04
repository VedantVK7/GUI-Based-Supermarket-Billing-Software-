import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class AmountDial extends JDialog implements ActionListener
{
	//error flag for invalid amount
	private boolean errFlag = false; 

	private int AMT_PAID = 0;
	private JTextField AmountField;
	private JLabel AmountLabel;
	private JLabel amtErrLabel;
	private JButton okButton;
	private Container cp;
	private JFrame P;

	AmountDial(JFrame parent, String title, boolean flag, int amtPaid) throws Exception{
		super(parent, title, flag);
		P = parent;
		AMT_PAID = amtPaid;
		setSize(300, 250);
		setLocationRelativeTo(null);
		cp = getContentPane();
		cp.setLayout(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// truncate the bill table in database after cancelling amount paid
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");
					Statement st = con.createStatement();
					st.executeUpdate("truncate table bill");
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});

		//initializing adding components into content pane
		AmountLabel = new JLabel("Enter amount paid");
		AmountLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		AmountLabel.setBounds(75, 27, 131, 38);
		cp.add(AmountLabel);

		AmountField = new JTextField();
		AmountField.setBounds(72, 92, 148, 30);
		AmountField.setColumns(10);
		AmountField.addActionListener(this);
		cp.add(AmountField);

		amtErrLabel = new JLabel();
		amtErrLabel.setBounds(72, 117, 148, 30);
		amtErrLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		amtErrLabel.setForeground(new Color(255, 0, 0));
		cp.add(amtErrLabel);

		okButton = new JButton("OK");
		okButton.setBounds(156, 177, 57, 23);
		cp.add(okButton);
		okButton.addActionListener(this);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) { // action performed on OK button
		String alphabates = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,/'][]{}:?><|`~!@#$%^&*()_+=-";
		errFlag = false; // set default error flag as false
		String str = AmountField.getText();
		int amt = 0;
		try {
			amt = Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			errFlag = true; // set error flag as true if amount contains any alphabates
		}
		for (int i = 0; i < alphabates.length(); i++) {
			if (str.equals("") || amt < AMT_PAID) {
				errFlag = true; // set error flag as true if amount contains null value or less than actual amount
				break;
			}
		}
		if (errFlag)
			amtErrLabel.setText("*Invalid amount!!"); // set error text in label
		else
			amtErrLabel.setText("");

		if (!errFlag) {
			
			int amtPaid = Integer.parseInt(AmountField.getText());
			setVisible(false); // generate dialog box of bill dialog if amount paid >= actual amount
			new BillDialog(P, "Bill", true, amtPaid); // passes amount paid to constructor of BillDialog
		}
	}
}