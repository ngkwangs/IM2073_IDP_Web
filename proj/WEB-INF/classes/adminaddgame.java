
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/addgamef")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class adminaddgame extends HttpServlet {


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
         
         String sqlStr = "SELECT DISTINCT genre FROM games WHERE qty > 0";
         ResultSet rset = stmt.executeQuery(sqlStr);

         out.println("<html><head><title>Welcome to GC-shop</title></head><body>");
         out.println("<h2>Welcome Admin! Add new game into db.</h2>");
         out.println("<form method='get' action='addgamesql'>");

         out.println("<label for='name'>Name:</label><input type='text' name='addg'required><br><br>");
 
         // A pull-down menu of all the genres
         out.println("Choose the Game Genre: <select name='addg' size='1'>");
         out.println("<option value=''>Select...</option>");  
         while (rset.next()) {  // list all the genres
            String genre = rset.getString("genre");
            out.println("<option value='" + genre + "'>" + genre + "</option>");
         }
         out.println("</select><br />");


         out.println("<br><label for='price'>Price:</label><input type='text' name='addg'required><br><br>");
         out.println("<label for='qty'>Quantity:</label><input type='text' name='addg'required><br><br>");

         out.println("<input type='submit' value='Add game'>");

         /* String gid = "SELECT id FROM games ORDER BY id DESC LIMIT 1;";

         ResultSet rid = stmt.executeQuery(gid);  // Send the query to the server
         rid.next();
         int raccid = rid.getInt("id") + 1;
         */
         out.println("</form>");

         

         // }

         } catch(Exception ex) {
            out.println("<h2>Wrong input! Please enter correct username or password! </h2><body></html>");

            // out.println("<p>Error: " + ex.getMessage() + "</p>");
            // out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();

            return;
         }
           
      

      // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   

   }

}