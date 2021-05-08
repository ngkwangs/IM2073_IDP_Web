
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class QueryServlet extends HttpServlet {

private String databaseURL, username, password;

@Override
public void init(ServletConfig config) throws ServletException {
   super.init(config);
   ServletContext context = config.getServletContext();
   databaseURL = context.getInitParameter("databaseURL");
   username = context.getInitParameter("username");
   password = context.getInitParameter("password");
}

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
   response.setContentType("text/html;charset=UTF-8");
   PrintWriter out = response.getWriter();

   Connection conn = null;
   Statement stmt = null;

   try {
      String genre = request.getParameter("genre");
      boolean hasGenreParam = genre != null && !genre.equals("Select...");
      String searchWord = request.getParameter("search");
      boolean hasSearchParam = searchWord != null && ((searchWord = searchWord.trim()).length() > 0);

      out.println("<html><head><title>Game the Core way</title></head><body>");
      out.println("<h2>Search Results</h2>");

      if (!hasGenreParam && !hasSearchParam) {  
         out.println("<h3>Please select a genre or enter a search term!</h3>");
         out.println("<p><a href='start'>Back to Select Menu</a></p>");
      } else {
         conn = DriverManager.getConnection(databaseURL, username, password);
         stmt = conn.createStatement();

         StringBuilder sqlStr = new StringBuilder();  
         sqlStr.append("SELECT * FROM games WHERE qty > 0 AND (");
         if (hasGenreParam) {
            sqlStr.append("genre = '").append(genre).append("'");
         }
         if (hasSearchParam) {
            if (hasGenreParam) {
               sqlStr.append(" OR ");
            }
            sqlStr.append("genre LIKE '%").append(searchWord)
                  .append("%' OR name LIKE '%").append(searchWord).append("%'");
         }
         sqlStr.append(") ORDER BY genre, name");
         ResultSet rset = stmt.executeQuery(sqlStr.toString());

         if (!rset.next()) {  // Check for empty Result
            out.println("<h3>No game found. Please try again!</h3>");
            out.println("<p><a href='start'>Back to Select Menu</a></p>");
         } else {
            out.println("<form method='get' action='cart'>");
            out.println("<input type='hidden' name='todo' value='add' />");
            out.println("<table border='1' cellpadding='6'>");
            out.println("<tr>");
            out.println("<th>&nbsp;</th>");
            out.println("<th>GENRE</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>PRICE</th>");
            out.println("<th>QTY</th>");
            out.println("</tr>");
            do {
               String id = rset.getString("id");
               out.println("<tr>");
               out.println("<td><input type='checkbox' name='id' value='" + id + "' /></td>");
               out.println("<td>" + rset.getString("genre") + "</td>");
               out.println("<td>" + rset.getString("name") + "</td>");
               out.println("<td>$" + rset.getString("price") + "</td>");
               out.println("<td><input type='text' size='3' value='1' name='qty" + id + "' /></td>");
               out.println("</tr>");
            } while (rset.next());
            out.println("</table><br />");
 
               // Submit and reset buttons
            out.println("<input type='submit' value='Add to My Shopping Cart' />");
            out.println("<input type='reset' value='CLEAR' /></form>");
 
               // Hyperlink to go back to search menu
            out.println("<p><a href='start'>Back to Select Menu</a></p>");
             // Show "View Shopping Cart" if cart is not empty
            HttpSession session = request.getSession(false); // check if session exists
            if (session != null) {
               Cart cart;
               synchronized (session) {
                  // Retrieve the shopping cart for this session, if any. Otherwise, create one.
                  cart = (Cart) session.getAttribute("cart");
                  if (cart != null && !cart.isEmpty()) {
                     out.println("<p><a href='cart?todo=view'>View Shopping Cart</a></p>");
                  }
               }
            } 

            out.println("</body></html>");
         }
      }
   } catch (SQLException ex) {
      out.println("<h3>Service not available. Please try again later!</h3></body></html>");
      Logger.getLogger(QueryServlet.class.getName()).log(Level.SEVERE, null, ex);
   } finally {
      out.close();
      try {
         if (stmt != null) stmt.close();
         if (conn != null) conn.close();
      } catch (SQLException ex) {
         Logger.getLogger(QueryServlet.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
   doGet(request, response);
}
}