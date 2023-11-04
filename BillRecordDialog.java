import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

class BillRecordDialog extends JDialog
{
    private JLabel HeadingLabel;
    private JTable billsTable;
    private DefaultTableModel billsTableModel;
    private JScrollPane billsPane;
    private Container contentPane;
    private String colHeads[]={"Bill no.","Date","Cust. name","Ph. No.","Items","Quantity","Amount"};

    void getRecords() //function to fetch billing history from database
    {
        int ID=0; 
		String query = "";
        String CUS_NAME ;
        String PH_NO ;
        String DATE;
        int ITEMS ;				
        int TOT_QTY ;
        int TOT_AMT;
		Statement st;
		Connection con;	

        try {
            //establish database connection
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill_DB", "root", "manager");
            st = con.createStatement();

            //fetching data into resultset
            query = "select * from bill_book";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                //get data of each row in variables
                ID = rs.getInt(1);
                DATE = rs.getString(2);
                CUS_NAME = rs.getString(3);
                PH_NO = rs.getString(4);
                ITEMS =rs.getInt(5);
                TOT_QTY =rs.getInt(6);
                TOT_AMT =rs.getInt(7);

                // adding the row into JTable
                Object row[] = {new Integer(ID),DATE,CUS_NAME,PH_NO,new Integer(ITEMS),new Integer(TOT_QTY),new Integer(TOT_AMT)};
                billsTableModel.addRow(row);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    BillRecordDialog(Frame parent,String title,boolean mode)
    {
        super(parent,title,mode);

        //initializing adding components into content pane
        setSize(650,500);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        contentPane = getContentPane();
        contentPane.setLayout(null);

        HeadingLabel = new JLabel("Previous Bills");
		HeadingLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		HeadingLabel.setBounds(230, 29, 151, 69);
		contentPane.add(HeadingLabel);
		
        billsTableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
            return false; //makes table cells uneditable 
            }
        };
        billsTableModel.setColumnIdentifiers(colHeads);
        billsTable = new JTable(billsTableModel);
        
		billsPane = new JScrollPane(billsTable);
		billsPane.setBounds(43, 110, 544, 329);
		contentPane.add(billsPane);

        getRecords();
        setVisible(true);
    }

}
