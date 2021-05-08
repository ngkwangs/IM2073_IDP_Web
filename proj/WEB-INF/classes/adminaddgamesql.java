
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/addgamesql")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class adminaddgamesql extends HttpServlet {


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

         String gid = "SELECT id FROM games ORDER BY id DESC LIMIT 1;";

         ResultSet rid = stmt.executeQuery(gid);  // Send the query to the server
         rid.next();
         int gamid = rid.getInt("id") + 1;

         String[] ag = request.getParameterValues("addg");
         
         String sqlStr = "insert into games values ('" + gamid + "', ";

         
         for (int i = 0; i < ag.length; ++i) {
                  if (i < ag.length - 1) {
                     sqlStr += "'" + ag[i] + "', ";  // need a commas
                  } else {
                     sqlStr += "'" + ag[i] + "'";    // no commas
                  }
            }

            sqlStr += ") ;";
            // out.println(sqlStr);
            stmt.executeUpdate(sqlStr);
            out.println("<h2>Successful! </h2>");
            // out.println("<P><a href='cart?todo=view'>View Shopping Cart</a></p>");



         

         // }

         } catch(Exception ex) {
            

            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();

            return;
         }
           
      

      // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   

   }

}