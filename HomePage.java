import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.time.*;
import java.time.format.*;

class HomePage extends JFrame implements ActionListener, FocusListener, ItemListener 
{
	static HomePage obj; // static object for passing it as parent parameter in dialogs

	// flags to show any error
	private boolean NameErrFlag = false;
	private boolean ProductNameErrFlag = false;
	private boolean QuantityErrFlag = false;
	private boolean Ph_NoErrFlag = false;

	// menu bar & menuItem objects
	private MenuBar mbr;
	private Menu historyMenu;
	private MenuItem previosBillsMItem;
	private Menu filMenu;
	private MenuItem newMItem;

	private JLabel nameErrLabel;
	private JLabel phNoErrLabel;
	private JLabel quantityErrLabel;
	private JLabel productNameErrLabel;
	private JLabel varientLabel;
	private JLabel quantityLabel;
	private JLabel nameLabel;
	private JLabel phNoLabel;
	private JLabel productNameLabel;
	private JLabel timeLabel;
	private JLabel lblNewLabel;

	// textfields for user data input
	private JTextField name;
	private JTextField phNo;
	private JTextField quantity;

	// Buttons for different actions
	private JButton generateBillBtn;
	private JButton changeQuantityBtn;
	private JButton addProductBtn;
	private JButton RemoveSelectedBtn;

	// Tables, table models & scrollpanes
	private JTable productTable;
	private JTable totalTable;
	private DefaultTableModel productTableModel;
	private DefaultTableModel totalTableModel;
	private JScrollPane tableScrollPane;
	private JScrollPane totalScrollPane;
	// choice list for product names & varient
	private Choice productName;
	private Choice varient;

	// column heading
	private String colHeads[] = { "Sr. No.", "Product ID", "Product Name", "Varient", "Quantity", "Price /item", "Amount" };
	private Container c;

	HomePage(String t) {
		super(t);
		setSize(1080, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(null);

		// initializing adding components into content pane
		newMItem = new MenuItem("New");
		newMItem.addActionListener(this);
		filMenu = new Menu("File");
		filMenu.add(newMItem);

		previosBillsMItem = new MenuItem("Previous Bills");
		previosBillsMItem.addActionListener(this);
		historyMenu = new Menu("History");
		historyMenu.add(previosBillsMItem);

		mbr = new MenuBar();
		mbr.add(filMenu);
		mbr.add(historyMenu);
		setMenuBar(mbr);

		name = new JTextField(10);
		name.setText("");
		name.setFont(new Font("Tahoma", Font.PLAIN, 14));
		name.setBounds(161, 84, 397, 27);
		name.addFocusListener(this);
		c.add(name);

		nameErrLabel = new JLabel();
		nameErrLabel.setBounds(161, 110, 100, 15);
		nameErrLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		nameErrLabel.setForeground(new Color(255, 0, 0));
		c.add(nameErrLabel);

		phNo = new JTextField(10);
		phNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phNo.setText("");
		phNo.setBounds(819, 84, 196, 27);
		phNo.addFocusListener(this);
		c.add(phNo);

		phNoErrLabel = new JLabel();
		phNoErrLabel.setBounds(819, 109, 100, 15);
		phNoErrLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		phNoErrLabel.setForeground(new Color(255, 0, 0));
		c.add(phNoErrLabel);

		nameLabel = new JLabel("Customer name : ");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(43, 84, 111, 27);
		c.add(nameLabel);

		phNoLabel = new JLabel("Phone Number :");
		phNoLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phNoLabel.setBounds(703, 84, 111, 27);
		c.add(phNoLabel);

		productNameLabel = new JLabel("Product Name :");
		productNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		productNameLabel.setBounds(703, 143, 111, 27);
		c.add(productNameLabel);

		productNameErrLabel = new JLabel();
		productNameErrLabel.setBounds(819, 170, 100, 15);
		productNameErrLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		productNameErrLabel.setForeground(new Color(255, 0, 0));
		c.add(productNameErrLabel);

		productName = new Choice();
		productName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		productName.setBounds(819, 145, 196, 25);
		productName.addItemListener(this);

		// to insert product names inside the product_name choice control from database
		Statement st;
		Connection con;
		try {
			// database connection
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");
			st = con.createStatement();
			// fetching product names from database
			ResultSet rs = st.executeQuery("select Product_name from Products");
			productName.addItem("<--Select Product-->");
			while (rs.next()) {
				// adds product name to choice control(product name) one by one
				productName.addItem(rs.getString(1));
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		c.add(productName);

		varientLabel = new JLabel("Product Varient :");
		varientLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		varientLabel.setBounds(703, 199, 111, 27);
		varientLabel.setVisible(false);
		c.add(varientLabel);

		varient = new Choice();
		varient.setFont(new Font("Tahoma", Font.PLAIN, 14));
		varient.setBounds(819, 199, 196, 25);
		varient.setVisible(false);
		c.add(varient);

		quantityLabel = new JLabel("Quantity : ");
		quantityLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		quantityLabel.setBounds(703, 260, 111, 27);
		c.add(quantityLabel);

		quantity = new JTextField(10);
		quantity.setText("0");
		quantity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		quantity.setBounds(819, 260, 196, 27);
		quantity.addFocusListener(this);
		quantity.addActionListener(this);
		c.add(quantity);

		quantityErrLabel = new JLabel("");
		quantityErrLabel.setBounds(819, 290, 100, 15);
		quantityErrLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		quantityErrLabel.setForeground(new Color(255, 0, 0));
		c.add(quantityErrLabel);

		addProductBtn = new JButton("Add Product");
		addProductBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addProductBtn.setBounds(790, 317, 174, 48);
		addProductBtn.addActionListener(this);
		c.add(addProductBtn);

		changeQuantityBtn = new JButton("Change Quantity");
		changeQuantityBtn.setBounds(790, 387, 174, 53);
		changeQuantityBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		changeQuantityBtn.addActionListener(this);
		c.add(changeQuantityBtn);

		RemoveSelectedBtn = new JButton("Remove Selected Product");
		RemoveSelectedBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		RemoveSelectedBtn.setBounds(779, 459, 196, 48);
		RemoveSelectedBtn.addActionListener(this);
		c.add(RemoveSelectedBtn);

		generateBillBtn = new JButton("Generate Bill");
		generateBillBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		generateBillBtn.setBounds(790, 523, 174, 48);
		generateBillBtn.addActionListener(this);
		c.add(generateBillBtn);

		// overriding isCellEditable() method for making cells uneditable
		productTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == 4); // makes only column no. 4 editable
			}
		};
		productTableModel.setColumnIdentifiers(colHeads);
		productTable = new JTable(productTableModel);

		String data[] = { "", "", "", "", "", "Total : ", "0" };
		String col[] = { "", "", "", "", "", "", "" };
		totalTableModel = new DefaultTableModel();
		totalTable = new JTable(totalTableModel);
		totalTableModel.setColumnIdentifiers(col);
		totalTableModel.addRow(data);

		tableScrollPane = new JScrollPane(productTable);
		tableScrollPane.setBounds(43, 143, 628, 454);
		c.add(tableScrollPane);

		totalScrollPane = new JScrollPane(totalTable);
		totalScrollPane.setBounds(43, 590, 628, 23);
		c.add(totalScrollPane);

		lblNewLabel = new JLabel("Bill Generation Software");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(427, 24, 279, 35);
		c.add(lblNewLabel);

		// get local date & time
		LocalDateTime myObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
		timeLabel = new JLabel(String.valueOf(myObj.format(myFormatObj)));
		timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		timeLabel.setBounds(790, 610, 200, 27);
		c.add(timeLabel);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		int id = 0, qty = 0, price = 0, amount = 0;
		String var = "", product_name = "", query = "";
		Statement st;
		PreparedStatement ps;
		Connection con;
		boolean found = false;
		String alphabates = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,/'][]{}:?><|`~!@#$%^&*()_+=-";
		String str = "";

		// checks for any constraint voilation

		//for quantity field
		QuantityErrFlag = false;
		ProductNameErrFlag = false;

		str = quantity.getText();
		for (int i = 0; i < alphabates.length(); i++) {
			if (str.indexOf(alphabates.charAt(i)) >= 0 || str.equals("") || str.equals("0")) {
				QuantityErrFlag = true; // set error flag as true if quantity contains any alphabates or null value or zero 
				break;
			}
		}

		//for product name
		if (productName.getSelectedIndex() == 0)
			ProductNameErrFlag = true; // set error flag as true if product is not selected			

		// action performed for Add Product Button
		if (e.getSource() == addProductBtn || e.getSource() == quantity) 
		{
			// sets error text if any constraint violates
			if (QuantityErrFlag)
				quantityErrLabel.setText("*Invalid quantity!!"); // set error text in label
			else
				quantityErrLabel.setText("");
			if (ProductNameErrFlag)
				productNameErrLabel.setText("*select a product!!"); // set error text in label
			else
				productNameErrLabel.setText("");

			// check constraint violation after setting flags
			if (!QuantityErrFlag && !ProductNameErrFlag) {
				try {
					// establish database connection
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");
					st = con.createStatement();
					// get selected product & ID
					product_name = (String) productName.getSelectedItem();
					qty = Integer.parseInt(quantity.getText());

					for (int i = 0; i < productTableModel.getRowCount(); i++) {

						// checks for the product already exists in list or not
						if (product_name.equals(productTableModel.getValueAt(i, 2))
								&& varient.getSelectedItem().equals(productTableModel.getValueAt(i, 3))) {
							found = true; // if present, set the flag & update its quantity
							int newQty = Integer.valueOf(String.valueOf(productTableModel.getValueAt(i, 4))) + qty;
							int pr = Integer.valueOf(String.valueOf(productTableModel.getValueAt(i, 5)));
							productTableModel.setValueAt(String.valueOf(newQty), i, 4);
							productTableModel.setValueAt(String.valueOf(pr * newQty), i, 6);
						}
					}
					// if product not present, adds it in a new row
					if (!found) {
						// fetch all details of the selected product
						query = "select * from Products where Product_name = '" + product_name + "'";
						ResultSet rs = st.executeQuery(query);
						rs.next();
						id = rs.getInt(1);
						var = varient.getSelectedItem();

						if (rs.getString(3).equals(var)) // if varient 1 selected, then price 1
							price = rs.getInt(4);
						if (rs.getString(5).equals(var)) // if varient 2 selected, then price 2
							price = rs.getInt(6);
						if (rs.getString(7).equals(var)) // if varient 3 selected, then price 3
							price = rs.getInt(8);

						amount = qty * price;

						Object row[] = { null, new Integer(id), product_name, var, new Integer(qty),
								new Integer(price), new Integer(amount) };
						productTableModel.addRow(row); // adds new row
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
				quantity.setText("0");
			}
		}

		// action performed for Generate bill button
		if (e.getSource() == generateBillBtn) 
		{ 
			// checks constraint voilation & product table is not empty
			if (!NameErrFlag && !Ph_NoErrFlag && productTableModel.getRowCount() != 0)
			{
				int TOT_AMT = 0;
				try {
					// establish database connection
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");

					// updation in bill table (from database) for bill generation
					query = "insert into bill values(?,?,?,?,?,?)";
					ps = con.prepareStatement(query);

					// updates each row of product table in database
					for (int i = 0; i < productTableModel.getRowCount(); i++) // loop for each product in table
					{
						//gets value from cells in product JTable
						int ID = Integer.parseInt(String.valueOf(productTableModel.getValueAt(i, 1)));
						String P_NAME = String.valueOf(productTableModel.getValueAt(i, 2));
						String VARI = String.valueOf(productTableModel.getValueAt(i, 3));
						int QTY = Integer.parseInt(String.valueOf(productTableModel.getValueAt(i, 4)));
						int PRC = Integer.parseInt(String.valueOf(productTableModel.getValueAt(i, 5)));
						int AMT = Integer.parseInt(String.valueOf(productTableModel.getValueAt(i, 6)));

						// set parameters in prapared statement query
						ps.setInt(1, ID);
						ps.setString(2, P_NAME);
						ps.setString(3, VARI);
						ps.setInt(4, PRC);
						ps.setInt(5, QTY);
						ps.setInt(6, AMT);
						ps.executeUpdate();
					}

					// updation in Bill_book table in database
					query = "insert into bill_book(bill_date,cus_name,ph_no,items,quantity,tot_amount) values(?,?,?,?,?,?)";

					// get values from input fields
					String CUS_NAME = name.getText();
					String PH_NO = phNo.getText();
					int ITEMS = productTableModel.getRowCount();
					int TOT_QTY = 0;
					TOT_AMT = 0;
					LocalDateTime myObj = LocalDateTime.now();
					DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
					String BILL_DATE = String.valueOf(myObj.format(myFormatObj));
					ps = con.prepareStatement(query);
					for (int i = 0; i < productTableModel.getRowCount(); i++) 
					{
						// calculate total quantity and amount 
						TOT_QTY += Integer.valueOf(String.valueOf(productTableModel.getValueAt(i, 4)));
						TOT_AMT += Integer.valueOf(String.valueOf(productTableModel.getValueAt(i, 6)));
					}

					// set parameters in prapared statement query & finally executes the query
					ps.setString(1, BILL_DATE);
					ps.setString(2, CUS_NAME);
					ps.setString(3, PH_NO);
					ps.setInt(4, ITEMS);
					ps.setInt(5, TOT_QTY);
					ps.setInt(6, TOT_AMT);
					ps.executeUpdate();
					new AmountDial(obj, "Amount", true, TOT_AMT); // generate dialog box of AmountDial dialog after bill generation
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		// action performed for Remove Product button
		if (e.getSource() == RemoveSelectedBtn) 
		{ 
			// removes the selected row from JTable
			int row_no = productTable.getSelectedRow();
			productTableModel.removeRow(row_no);
		}

		// action performed for Change Quantity button
		if (e.getSource() == changeQuantityBtn) { 
			try {
				//database connection
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");
				st = con.createStatement();

				//gets the new quantity & price of selected product from JTable
				int row_no = productTable.getSelectedRow();
				int newQty = Integer.valueOf(String.valueOf(productTableModel.getValueAt(row_no, 4)));
				int pr = Integer.valueOf(String.valueOf(productTableModel.getValueAt(row_no, 5)));
				// updates quantity in JTable
				productTableModel.setValueAt(String.valueOf(newQty * pr), row_no, 6);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

		}

		// action performed for new bill menu item
		if (e.getSource() == newMItem)
		{ 
			name.setText("");
			phNo.setText("");
			nameErrLabel.setText("");
			quantityErrLabel.setText("");
			phNoErrLabel.setText("");
			productNameErrLabel.setText("");
			quantity.setText("");
			productName.select(0);
			varient.setVisible(false);
			varientLabel.setVisible(false);
			int limit = productTableModel.getRowCount();
			for (int i = limit - 1; i >= 0; i--) {
				productTableModel.removeRow(i); // removes each row from JTabel one by one
			}
		}
		if (e.getSource() == previosBillsMItem) {// action performed for previous bill menu item
			new BillRecordDialog(obj, "Previous Bills", true); // generate dialog box of bill records dialog
		}
		for (int i = 0; i < productTableModel.getRowCount(); i++) {
			productTableModel.setValueAt(String.valueOf(i + 1), i, 0); // sets serial number for each record in JTable
		}

		int total = 0;
		if (productTableModel.getRowCount() == 0) {
			totalTableModel.setValueAt(0, 0, 6); // if JTable is empty, set total amount as 0
		} else {
			for (int i = 0; i < productTableModel.getRowCount(); i++) 
			{
				// calculates total amount	
				total = total + Integer.valueOf(String.valueOf(productTableModel.getValueAt(i, 6))); 																									
			}
			totalTableModel.setValueAt((String.valueOf(total)), 0, 6); // updates total amount in JTable
		}
	}

	public void focusLost(FocusEvent e) { // focus lost event handler for checking constraints of name,phNo,quantity
		String numbers = "0123456789";
		String alphabates = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,/'][]{}:?><|`~!@#$%^&*()_+=-";
		String str ="";
		if (e.getSource() == name) { // focus lost for Textfield name
			NameErrFlag = false;
			str = name.getText();
			for (int i = 0; i < 10; i++) {
				if (str.indexOf(numbers.charAt(i)) >= 0 || str.equals("")) {
					NameErrFlag = true; // set error flag as true if name contains number or null value
					break;
				}
			}
			if (NameErrFlag)
				nameErrLabel.setText("*Invalid name!!"); // set error text in label
			else
				nameErrLabel.setText("");
		}
		if (e.getSource() == phNo) {// focus lost for phone number Textfield
			Ph_NoErrFlag = false;
			str = phNo.getText();
			for (int i = 0; i < alphabates.length(); i++) {
				if (str.indexOf(alphabates.charAt(i)) >= 0 || str.equals("") || str.length() != 10) {
					Ph_NoErrFlag = true; // set error flag as true if phone no. contains any alphabates or null value or length not equal to 10											
					break;
				}
			}
			if (Ph_NoErrFlag)
				phNoErrLabel.setText("*Invalid number!!"); // set error text in label
			else
				phNoErrLabel.setText("");
		}
		if (e.getSource() == quantity) { // focus lost for quantity Textfield
			QuantityErrFlag = false;
			str = quantity.getText();
			for (int i = 0; i < alphabates.length(); i++) {
				if (str.indexOf(alphabates.charAt(i)) >= 0 ||  str.equals("0"))
				{
					QuantityErrFlag = true; // set error flag as true if quantity contains any alphabates or null value or zero
					break;
				}
			}
			if (QuantityErrFlag)
				quantityErrLabel.setText("*Invalid quantity!!"); // set error text in label
			else
				quantityErrLabel.setText("");

			if (str.equals("")) 
				quantity.setText("0");			
		}
	}

	public void focusGained(FocusEvent e) {
		if (e.getSource() == quantity)
			quantity.setText(""); // if quantity field gains focus then clear the data in textfield
	}

	public void itemStateChanged(ItemEvent e) 
	{
		//shows varient choice list & its contents if some product is selected 
		if (productName.getSelectedIndex() != 0) 
		{
			varient.setVisible(true);
			varientLabel.setVisible(true);
			Statement st;
			Connection con;
			try {
				//database connection
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager"); 
				st = con.createStatement();

				//get selected item from product name choice list & fetches varient names of selected product
				String item = productName.getSelectedItem();
				ResultSet rs = st.executeQuery(
						"select varient_1, varient_2, varient_3 from Products where product_name = '" + item + "'");
				rs.next();
				varient.removeAll(); // remove older varients
				
				for (int i = 1; i <= 3; i++) 
				{
					//adds the varient if it's not null
					if (!rs.getString(i).equals("null")) {
						varient.add(rs.getString(i));
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		} else {
			//hides the varient choice list if no product is selected
			varient.setVisible(false);
			varientLabel.setVisible(false);
		}

	}
	public static void main(String[] args) {
		new HomePage("Dmart Billing System");
	}
}
