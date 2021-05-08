
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/checkout")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class checkout extends HttpServlet {


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

      String[] id = request.getParameterValues("gamecho");

      String[] gnameincart = new String[25];
      int[] priceincart = new int[25];

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/projgameshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         out.println("<h>Items in cart </h>");
         

         // Print the <form> start tag
         out.println("<form method='get' action=''>");
         // out.println("<input type='submit' value='Filter Price'>");
         out.println("<button type='submit' formaction = 'checkout'> Check out </button>");
         // out.println("<button type='submit' formaction = 'checkout'> Checkout </button>");
         out.println("</form>");
            
            
            String sqlStr = "SELECT * FROM availgames WHERE gameID in( ";
                for (int i = 0; i < id.length; ++i) {
                  if (i < id.length - 1) {
                     sqlStr += "'" + id[i] + "', ";  // need a commas
                  } else {
                     sqlStr += "'" + id[i] + "'";    // no commas
                  }
               }

               sqlStr += ");";



            ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
            while(rset.next()) {
               out.println("<form method='get' action=''>");
               // out.println("<tr> <th> </th> <th>Game ID</th> <th>Game Name</th> <th>Game Genre</th> <th>Game Developer</th> <th>Price</th> </tr>");
               out.println("<tr><td><p><label for='cart'>"
                     + "" + rset.getString("gameID") + " </label> </p> </td> </tr> <tr><p>Name: "
                     
                     + rset.getString("name") + " </p></tr> <tr><p>Price: $"
                     + rset.getString("price") + "</p></tr><br>");
               
            }

            // out.println("<input type='submit' value='Add to cart'></form>");
            out.println("</form>");


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