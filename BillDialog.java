import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.table.*;
import java.time.*;
import java.time.format.*;


class BillDialog extends JDialog implements ActionListener,Printable
{	
	//private variable declarations
	private int AMT_PAID = 0;
	private JLabel Heading;
	private JLabel BillNo;
	private JLabel BillNoLabel;
	private JLabel lblDate;
	private JLabel dateField;
	private JLabel horizontalLine1;
	private JLabel NameLabel;
	private JLabel NameField;
	private JLabel PhNoLabel;
	private JLabel PhNoField;
	private JLabel TotalItemLabel;
	private JLabel TotalItemCount;
	private JLabel TotalQtyLabel ;
	private JLabel TotalQtyCount;
	private JLabel TotalPriceLabel;
	private JLabel TotalPriceCount;
	private JLabel AmountPaidLabel;
	private JLabel ChangeRecievedField;
	private JLabel ChangeRecievedLabel;
	private JLabel AmountPaidField;
    private JTable billTable;
	private JScrollPane ItemsTablePane ;
    private DefaultTableModel billTableModel;
	private JButton ThanksBtn ;
	private Container contentPane;
    private String colHeads[] = { "Product ID", "Name","Varient",  "Price /item","Quantity", "Amount" }; 

    public void putRows() //function to put values in bill JTable
    {
        int id=0, qty=0,price=0,amount=0;
		String product_name="",query = "",var="";
		Statement st;
		Connection con;	

        try {
            Class.forName("com.mysql.jdbc.Driver"); //establishing database connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager"); 
            st = con.createStatement();
            query = "select * from bill";

            ResultSet rs = st.executeQuery(query); //result set will contain all result from bill table(database table)
            while(rs.next()) //traverse through each row of resultset
            {
				//get values from result set & store in variables
                id = rs.getInt(1);
                product_name = rs.getString(2);
				var = rs.getString(3);
                price = rs.getInt(4);
                qty = rs.getInt(5);
                amount =rs.getInt(6);

				// insert the row fetched from database into bill JTable
                Object row[] = {new Integer(id),product_name,var,new Integer(price),new Integer(qty),new Integer(amount)};
                billTableModel.addRow(row); //adds row
            }
        
			con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setValues() // function to set values of fields/labels
    {
        String query;
        int BILL_NO;
        String CUS_NAME ;
        String PH_NO ;
        int ITEMS ;
        int TOT_QTY =0 ;
        int TOT_AMT=0;
		int GIVEN_AMT;
		Statement st;
		Connection con;	
        try {
			//establishing database connection
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");
            st = con.createStatement();

			//fetch records from bill_book table for customer information
            query = "select * from bill_book ";
            ResultSet rs = st.executeQuery(query);
            rs.last(); //point to last record (most recent transaction)
            BILL_NO = rs.getInt(1);
            CUS_NAME = rs.getString(3);
            PH_NO = rs.getString(4);
            ITEMS = rs.getInt(5);
            TOT_QTY = rs.getInt(6);
            TOT_AMT = rs.getInt(7);
			GIVEN_AMT = AMT_PAID - TOT_AMT; 

			//set the details fetched from bill_book into labels
            BillNo.setText(String.valueOf(BILL_NO));
            NameField.setText(CUS_NAME);
            PhNoField.setText(PH_NO);
            TotalItemCount.setText(String.valueOf(ITEMS));
            TotalQtyCount.setText(String.valueOf(TOT_QTY));
            TotalPriceCount.setText(String.valueOf(TOT_AMT)+" Rs.");
			AmountPaidField.setText(String.valueOf(AMT_PAID)+" Rs." );
			ChangeRecievedField.setText(String.valueOf(GIVEN_AMT)+" Rs.");
            LocalDateTime myObj = LocalDateTime.now();
		    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss"); 
		    dateField.setText(String.valueOf(myObj.format(myFormatObj)));

			//update the bill_book table with the amount paid & change returned values
			query = "update bill_book set amount_paid ="+AMT_PAID+",change_returned ="+GIVEN_AMT+" where bill_no = "+BILL_NO+"";
			st.executeUpdate(query);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }       
    }

	BillDialog(JFrame parent,String title,boolean flag,int amtPaid)
	{
        super(parent,title,flag);    
		
		AMT_PAID = amtPaid;  
		setSize(450,700);		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// truncate the bill table in database after fetching records
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
		setLocationRelativeTo(null);
		contentPane = getContentPane();			
		contentPane.setLayout(null);

		//initializing adding components into content pane
        billTableModel = new DefaultTableModel()
		{
			@Override
            public boolean isCellEditable(int row, int column) {
            return false; //make all cells uneditable in JTable
            }
		};
        billTableModel.setColumnIdentifiers(colHeads);
        billTable = new JTable(billTableModel);
        ItemsTablePane = new JScrollPane(billTable);
		ItemsTablePane.setBounds(37, 182, 364, 333);
		contentPane.add(ItemsTablePane);        

		Heading = new JLabel("Dmart");
		Heading.setFont(new Font("Tahoma", Font.BOLD, 30));
		Heading.setBounds(168, 11, 108, 66);
		contentPane.add(Heading);
		
		BillNoLabel = new JLabel("Bill No.");
		BillNoLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		BillNoLabel.setBounds(38, 84, 55, 14);
		contentPane.add(BillNoLabel);
		
		BillNo = new JLabel("");
		BillNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		BillNo.setBounds(92, 84, 79, 14);
		contentPane.add(BillNo);
		
		lblDate = new JLabel("Date & Time :-");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(128, 84, 98, 14);
		contentPane.add(lblDate);
		
		dateField = new JLabel("");
		dateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateField.setBounds(223, 84, 201, 14);
		contentPane.add(dateField);
		
		horizontalLine1 = new JLabel("___________________________________________________________");
		horizontalLine1.setBounds(10, 95, 414, 14);
		contentPane.add(horizontalLine1);
		
		NameLabel = new JLabel("Customer Name :-");
		NameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		NameLabel.setBounds(38, 120, 113, 18);
		contentPane.add(NameLabel);
		
		NameField = new JLabel("");
		NameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		NameField.setBounds(165, 120, 246, 18);
		contentPane.add(NameField);
		
		PhNoLabel = new JLabel("Customer PhNo :-");
		PhNoLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		PhNoLabel.setBounds(38, 145, 113, 18);
		contentPane.add(PhNoLabel);
		
		PhNoField = new JLabel("");
		PhNoField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		PhNoField.setBounds(165, 145, 246, 18);
		contentPane.add(PhNoField);			
		
		TotalItemLabel = new JLabel("Items :");
		TotalItemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		TotalItemLabel.setBounds(48, 534, 51, 25);
		contentPane.add(TotalItemLabel);
			
		TotalItemCount = new JLabel("");
		TotalItemCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		TotalItemCount.setBounds(104, 534, 25, 25);
		contentPane.add(TotalItemCount);
			
		TotalQtyLabel = new JLabel("Qty :");
		TotalQtyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		TotalQtyLabel.setBounds(137, 534, 41, 25);
		contentPane.add(TotalQtyLabel);
			
		TotalQtyCount = new JLabel("");
		TotalQtyCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		TotalQtyCount.setBounds(177, 534, 50, 25);
		contentPane.add(TotalQtyCount);
			
		TotalPriceLabel = new JLabel("Total :");
		TotalPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		TotalPriceLabel.setBounds(235, 534, 59, 25);
		contentPane.add(TotalPriceLabel);
			
		TotalPriceCount = new JLabel("");
		TotalPriceCount.setFont(new Font("Tahoma", Font.PLAIN, 20));
		TotalPriceCount.setBounds(304, 534, 120, 25);
		contentPane.add(TotalPriceCount);

		AmountPaidLabel = new JLabel("Amount Paid :");
		AmountPaidLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		AmountPaidLabel.setBounds(74, 564, 80, 25);
		contentPane.add(AmountPaidLabel);

		AmountPaidField = new JLabel("");
		AmountPaidField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		AmountPaidField.setBounds(155, 564, 70, 25);
		contentPane.add(AmountPaidField);

		ChangeRecievedLabel = new JLabel("Change Recieved :");
		ChangeRecievedLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ChangeRecievedLabel.setBounds(221, 564, 105, 25);
		contentPane.add(ChangeRecievedLabel);

		ChangeRecievedField = new JLabel("");
		ChangeRecievedField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ChangeRecievedField.setBounds(327, 564, 105, 25);
		contentPane.add(ChangeRecievedField);

		ThanksBtn = new JButton("Thanks! Visit Againn!!");
		ThanksBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		ThanksBtn.setBounds(72, 596, 308, 50);
		ThanksBtn.addActionListener(this);
		contentPane.add(ThanksBtn);

        putRows();
        setValues();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		PrinterJob printerJob = PrinterJob.getPrinterJob();

        if (printerJob.printDialog()) {
			Paper p = new Paper();
			p.setSize(450,700);
            PageFormat pageFormat = new PageFormat();
			pageFormat.setPaper(p);
            printerJob.setPrintable(this);

            try {
                printerJob.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
	}

	public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        this.paint(g2d);     		
		
        return PAGE_EXISTS;
    }
	

}