// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/createacc")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class accCreatebyUser extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      
      


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
            
            String chkaccid = "SELECT accID FROM accounts ORDER BY accID DESC LIMIT 1;";

            ResultSet rid = stmt.executeQuery(chkaccid);  // Send the query to the server
            rid.next();
            int raccid = rid.getInt("accID") + 1;

            String[] id = request.getParameterValues("id");
            String nauname = id[0];

            String chkusername = "select * from accounts where userName= '" + nauname + "';";
            ResultSet run = stmt.executeQuery(chkusername);  // Send the query to the server
            if(run.next()) {
               out.println(run.getString("userName"));

               if (run.getString("userName") != null){
                  String dbusn = run.getString("userName");

                  if(nauname.equals(dbusn)) {
                  out.println("<h2>Username already exists! Use another user! </h2><body></html>");
                  return; // Exit doGet()

                  }
               }
               
            }

            String sqlStr = "INSERT INTO accounts values (" + raccid + ", ";



            for (int i = 0; i < id.length; ++i) {
                  if (i < id.length - 1) {
                     sqlStr += "'" + id[i] + "', ";  // need a commas
                  } else {
                     sqlStr += "'" + id[i] + "'";    // no commas
                  }
            }

            sqlStr += ") ;";
            // out.println(sqlStr);
            stmt.executeUpdate(sqlStr);
            out.println("<h2>Successful! </h2><body></html>");

            out.println("<form method='get' action='/proj/login.html'><b>Input user details:</b><br/></br>");
            out.println("<input type='submit' value='Back to log in page'></form>");



         } catch(Exception ex) {
            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();
         }
           
      

      // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}