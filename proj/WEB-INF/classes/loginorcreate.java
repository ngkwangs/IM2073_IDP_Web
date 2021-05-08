
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/logorcreate")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class loginorcreate extends HttpServlet {


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
         String[] id = request.getParameterValues("id");  // Returns an array of Strings
         // String logval = request.getParameter("dow");
         // save the username into a class so can use agn
         // user sname = new user();
         user.setUName(id[0]);
         


         // else { 
            if (id[0] == "") {
            out.println("<h2>No input! Please enter username and password! </h2><body></html>");
            return; // Exit doGet()
            } 
            
               String sqlStr = "SELECT password FROM accounts WHERE userName = '" + id[0] + "';";
               /* for (int i = 0; i < id.length; ++i) {
                  if (i < id.length - 1) {
                     sqlStr += "'" + id[i] + "', ";  // need a commas
                  } else {
                     sqlStr += "'" + id[i] + "'";    // no commas
                  }
               } */

              //  sqlStr += ") AND qty > 0 ORDER BY author ASC, title ASC";

               // out.println("<h3>Thank you for your query.</h3>");
               // out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
               ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
               rset.next();
               String rsetstring = rset.getString("password");

               if (rsetstring.equals(id[1])) {
                  /* 
                  out.println("<html><head><title>Wrong LogIn Info!</title></head>");
                  out.println("<body>");
                  out.println("<form method="get" action="logorcreate">");
                  out.println("<b>Wrong LogIn Info!</b><br/></br>");
                  out.println("<b>Key in correct details!</b><br/></br>");
                  out.println("<input type="submit" value="OK">");
                  out.println("</form></body></html>");
                  */

                  out.println("<h2>Log in successful! Welcome user " + id[0] + "! </h2>");
                  // out.println("<label for='usname'>Username: " + id[0] + " value=" + id[0] + "</label>");
                  
                  
                  if(id[0].equals("Admin")){
                  out.println("<form method='get' action='addgamef'>");
                  out.println("<input type='submit' value='Add Inventory'>");
                  out.println("</form>");

                  }

                  // Print the <form> start tag
                  out.println("<form method='get' action='viewgame'>");
                  out.println("<input type='submit' value='Buy Game'>");
                  out.println("</form>");

                  out.println("<form method='get' action='viewacc'>");
                  out.println("<input type='submit' value='View account details'>");
                  out.println("</form>");

                  
               

               }
               else {


               out.println("<h2>Wrong input! Please enter correct username or password! </h2><body></html>");
               return; // Exit doGet()
               
               // For each row in ResultSet, print one checkbox inside the <form>
              /*  while(rset.next()) {
                  out.println("<tr><td><p><input type='checkbox' name='id' value="
                        + "'" + rset.getString("id") + "' /> <td>"
                        + rset.getString("author") + " <td>"
                        + rset.getString("title") + " <td>$"
                        + rset.getString("price") + "</p></td></td></td></td></tr>");
               } */
               
            }

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