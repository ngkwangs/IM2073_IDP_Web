// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/viewacc")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class seeacc extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      
      // user suname = loginorcreate.sname;
      String userid = user.getUName();
      // String userid2 = loginorcreate.getUserN;


      // Print an HTML page as the output of the query
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/projgameshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
            // String una = request.getValue("usname");

            
            String sqlStr = "SELECT * FROM accounts WHERE userName = '" + userid + "';";

            ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
            rset.next();
            String rsetuname = rset.getString("userName");
            String rsetname = rset.getString("Name");
            String rsetdob = rset.getString("DOB");
            String rsetnric = rset.getString("NRIC");
            String rsetadd = rset.getString("address");
            String rsetccno = rset.getString("creditCardNo");

            out.println("<form method='get' action=''>");
            out.println("<b>User details:</b><br/></br>");

            out.println("<label for='uname'>Username: " + rsetuname + "</label>");
            out.println("<br><br>");

            out.println("<label for='name'>Name: " + rsetname + "</label>");
            out.println("<br><br>");

            out.println("<label for='dob'>Date of Birth: " + rsetdob + "</label>");
            out.println("<br><br>");

            out.println("<label for='nric'>NRIC: " + rsetnric + "</label>");
            out.println("<br><br>");

            out.println("<label for='address'>Address: " + rsetadd + "</label>");
            out.println("<br><br>");

            out.println("<label for='cc_no'>Credit Card number: xxxx-xxxx-xxxx-" + rsetccno.charAt(12) + rsetccno.charAt(13) + rsetccno.charAt(14) + rsetccno.charAt(15) + "</label>");
            out.println("<br><br>");

            out.println("</form>");



         } catch(Exception ex) {
            out.println("<p>" + userid + "</p>");
            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();
         }
           
      

      // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}