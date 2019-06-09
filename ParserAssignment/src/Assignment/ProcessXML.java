package Assignment;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.nio.file.StandardWatchEventKinds;
public class ProcessXML {

	
	
	public synchronized void processXMLFile()
	{
		System.out.println("Called XML File");
		
		try
		{
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
	
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");
		ResultSet rs = null;
		ResultSet rs1= null;

		final Path path = FileSystems.getDefault().getPath(("C:/banking/input"));
		//System.out.println(path);
		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
		    final WatchKey watchKey = path.register(watchService,StandardWatchEventKinds.ENTRY_MODIFY);			   
		    while (true) {
		        final WatchKey wk = watchService.take();
		        for (WatchEvent<?> event : wk.pollEvents()) {
		            final Path changed = (Path) event.context();
		            String fileString = changed.toString();
		            //if (changed.endsWith("Emp.xml")) {
		               // System.out.println("The file "+changed+" has changed");
		            //}
		            //System.out.println("The file ends with EMP.xml enters "+changed);
		            System.out.println(" Path :"+path+ "\\"+ changed);
		            File fXmlFile = new File(path+"/"+changed);
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(fXmlFile);
					List<Employee> employees = new ArrayList<Employee>();
					//System.out.println("Root element :" + document.getDocumentElement().getNodeName());
					NodeList nodeList = document.getDocumentElement().getChildNodes();
					//System.out.println("INside the nodelist"+nodeList.getLength());
					
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element elem = (Element) node;
							String ID = node.getAttributes().getNamedItem("No").getNodeValue();
							String accId = elem.getElementsByTagName("ACCID").item(0).getChildNodes().item(0).getNodeValue();
							Integer accNo = Integer
									.parseInt(elem.getElementsByTagName("AccNum").item(0).getChildNodes().item(0).getNodeValue());

							String accType = elem.getElementsByTagName("ACCTYPE").item(0).getChildNodes().item(0).getNodeValue();
							String bankName = elem.getElementsByTagName("BankName").item(0).getChildNodes().item(0).getNodeValue();

							String firstName = elem.getElementsByTagName("FirstName").item(0).getChildNodes().item(0)
									.getNodeValue();
							String lastName = elem.getElementsByTagName("LastName").item(0).getChildNodes().item(0).getNodeValue();
							Integer balance = Integer
									.parseInt(elem.getElementsByTagName("BALANCE").item(0).getChildNodes().item(0).getNodeValue());
							employees.add(new Employee(accId, accNo, accType, bankName, firstName, lastName, balance));
							
							//System.out.println("Inside the nodelist");
							

						}//end of if
					}//end of for nodelist

					// Print all users.
					for (Employee empl : employees) {
						System.out.println("Employee obj:");
						System.out.println(empl.toString());
						Statement st = con.createStatement();
						String s1= "select userid,accNumber from Account where userId ="+empl.getAccId();
						rs = st.executeQuery(s1);
						String userId=null;
						int accountNo=0;
						while(rs.next()) {
							 userId = rs.getString("userid");
							 accountNo=rs.getInt("accNumber");
						}							
						//System.out.println("user Id :"+userId);
						//System.out.println("empl.getAccId() :"+empl.getAccId());
						if (userId==null || !userId.equals(empl.getAccId()))
						{
						
						if (accountNo!=empl.getAccNo())
						{
							String insertString1 = "Insert into  Account_Temp(UserId,accNumber,acctype,bankname,firstname,lastname,balance) values ('";
							String endString1 = ")";
							String queryString1 = insertString1 + empl.getAccId() + "','" + empl.getAccNo() + "','" + empl.getAccType()
								+ "','" + empl.getBankName() + "','" + empl.getFirstName() + "','" + empl.getLastName() + "',"
								+ empl.getBalance() + endString1;
						
							String s2= "select userid,accNumber from Account where userId ="+empl.getAccId();
							rs = st.executeQuery(s2);
							String userId1=null;
							int accountNo1=0;
							while(rs.next()) {
								 userId1 = rs.getString("userid");
								 accountNo1=rs.getInt("accNumber");
						}	
							if(userId1 !=empl.getAccId() && accountNo1 != empl.getAccNo())
								rs1 = st.executeQuery(queryString1);							
						}
						
						}
					}
					

				}
		     // reset the key
		        boolean valid = wk.reset();
		        if (!valid) {
		            System.out.println("Key has been unregistered");
		        }
		        
		        String insertString1 = "INSERT INTO ACCOUNT(USERID,ACCNUMBER,ACCTYPE,BANKNAME,FIRSTNAME,LASTNAME,BALANCE) SELECT DISTINCT USERID,ACCNUMBER,ACCTYPE,BANKNAME,FIRSTNAME,LASTNAME,BALANCE FROM ACCOUNT_TEMP WHERE USERID NOT IN(SELECT USERID FROM ACCOUNT)";
				Statement st1 = con.createStatement();
				ResultSet rs3 = st1.executeQuery(insertString1);
		        
			}//end of while
		    
			
		 }//end of try
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (rs1 != null)
				{
					rs1.close();
				}
				if (con != null)
					con=null;
			}
			catch (Exception e)			
			{
				System.out.println("Exception occured"+e);
				
			}
		
		}
		
		}
		catch(Exception e)
		{
			System.out.println("Exception Occurred: while processing XML File -"+e.getMessage());
		}
		
		
	}

}
