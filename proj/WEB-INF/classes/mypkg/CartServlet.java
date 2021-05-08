
 
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
 
public class CartServlet extends HttpServlet {
 
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
 
      // Retrieve current HTTPSession object. If none, create one.
      HttpSession session = request.getSession(true);
      Cart cart;
      synchronized (session) {  // synchronized to prevent concurrent updates
         // Retrieve the shopping cart for this session, if any. Otherwise, create one.
         cart = (Cart) session.getAttribute("cart");
         if (cart == null) {  // No cart, create one.
            cart = new Cart();
            session.setAttribute("cart", cart);  // Save it into session
         }
      }
 
      Connection conn   = null;
      Statement  stmt   = null;
      ResultSet  rset   = null;
      String     sqlStr = null;
 
      try {
 
         out.println("<html><head><title>Shopping Cart</title></head><body>");
         out.println("<h2>GC - Your Shopping Cart</h2>");

         conn = DriverManager.getConnection(databaseURL, username, password);
         stmt = conn.createStatement();
 
         String todo = request.getParameter("todo");
         if (todo == null) todo = "view";  // to prevent null pointer
 
         if (todo.equals("add") || todo.equals("update")) {
            String[] ids = request.getParameterValues("id");
            if (ids == null) {
               out.println("<h3>Please Select a Game!</h3></body></html>");
               return;
            }
            for (String id : ids) {
               sqlStr = "SELECT * FROM games WHERE id = " + id;
               rset = stmt.executeQuery(sqlStr);
               rset.next(); // Expect only one row in ResultSet
               String title = rset.getString("name");
               String genre = rset.getString("genre");
               float price = rset.getFloat("price");
 
               int qtyOrdered = Integer.parseInt(request.getParameter("qty" + id));
               int idInt = Integer.parseInt(id);
               if (todo.equals("add")) {
                  cart.add(idInt, title, genre, price, qtyOrdered);
               } else if (todo.equals("update")) {
                  cart.update(idInt, qtyOrdered);
               }
            }
 
         } else if (todo.equals("remove")) {
            String id = request.getParameter("id");  
            cart.remove(Integer.parseInt(id));
         }
 
         //  display the shopping cart
         if (cart.isEmpty()) {
            out.println("<p>Your shopping cart is empty</p>");
         } else {
            out.println("<table border='1' cellpadding='6'>");
            out.println("<tr>");
            out.println("<th>GENRE</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>PRICE</th>");
            out.println("<th>QTY</th>");
            out.println("<th>REMOVE</th></tr>");
 
            float totalPrice = 0f;
            for (CartItem item : cart.getItems()) {
               int id = item.getId();
               String genre = item.getGenre();
               String title = item.getTitle();
               float price = item.getPrice();
               int qtyOrdered = item.getQtyOrdered();
 
               out.println("<tr>");
               out.println("<td>" + genre + "</td>");
               out.println("<td>" + title +  "</td>");
               out.println("<td>" + price +  "</td>");
 
               out.println("<td><form method='get'>");
               out.println("<input type='hidden' name='todo' value='update' />");
               out.println("<input type='hidden' name='id' value='" + id + "' />");
               out.println("<input type='text' size='3' name='qty"
                       + id + "' value='" + qtyOrdered + "' />" );
               out.println("<input type='submit' value='Update' />");
               out.println("</form></td>");
 
               out.println("<td><form method='get'>");
               out.println("<input type='submit' value='Remove'>");
               out.println("<input type='hidden' name='todo' value='remove'");
               out.println("<input type='hidden' name='id' value='" + id + "'>");
               out.println("</form></td>");
               out.println("</tr>");
               totalPrice += price * qtyOrdered;
            }
            out.println("<tr><td colspan='5' align='right'>Total Price: $");
            out.printf("%.2f</td></tr>", totalPrice);
            out.println("</table>");
         }
 
         out.println("<p><a href='start'>Select More Games...</a></p>");
 
         // Display the Checkout
         if (!cart.isEmpty()) {
            out.println("<br /><br />");
            out.println("<form method='get' action='checkout'>");
            out.println("<input type='submit' value='CHECK OUT'>");
            out.println("<p>Please fill in your particular before checking out:</p>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<td>Enter your Name:</td>");
            out.println("<td><input type='text' name='cust_name' /></td></tr>");
            out.println("<tr>");
            out.println("<td>Enter your Email:</td>");
            out.println("<td><input type='text' name='cust_email' /></td></tr>");
            out.println("<tr>");
            out.println("<td>Enter your Phone Number:</td>");
            out.println("<td><input type='text' name='cust_phone' /></td></tr>");
            out.println("</table>");
            out.println("</form>");
         }
 
         out.println("</body></html>");
 
      } catch (SQLException ex) {
         out.println("<h3>Service not available. Please try again later!</h3></body></html>");
         Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         out.close();
         try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();  
         } catch (SQLException ex) {
            Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}