
 
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

 
public class OrderServlet extends HttpServlet {
 
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
 
      // Connection conn = null;
      // Statement  stmt = null;
      ResultSet  rset = null;
      String     sqlStr = null;
 
      try {
         out.println("<html><head><title>Order Confirmation</title></head><body>");
         out.println("<h2>Game Core - Order Confirmation</h2>");
 

         String[] ids = request.getParameterValues("id");  
         String custName = request.getParameter("cust_name");
         boolean hasCustName = custName != null && ((custName = custName.trim()).length() > 0);
         String custEmail = request.getParameter("cust_email").trim();
         boolean hasCustEmail = custEmail != null && ((custEmail = custEmail.trim()).length() > 0);
         String custPhone = request.getParameter("cust_phone").trim();
         boolean hasCustPhone = custPhone != null && ((custPhone = custPhone.trim()).length() > 0);
 
         // Inputs validation
         if (ids == null || ids.length == 0) {
            out.println("<h3>Please Select a Game!</h3>");
         } else if (!hasCustName) {
            out.println("<h3>Please Enter Your Name!</h3>");
         } else if (!hasCustEmail || (custEmail.indexOf('@') == -1)) {
            out.println("<h3>Please Enter Your e-mail (user@host)!</h3>");
         } else if (!hasCustPhone || (custPhone.length() != 8)) {
            out.println("<h3>Please Enter an 8-digit Phone Number!</h3>");
         } else {
            // Display the customer name, email and phone 
            out.println("<table>");
            out.println("<tr><td>Customer Name:</td><td>" + custName + "</td></tr>");
            out.println("<tr><td>Customer Email:</td><td>" + custEmail + "</td></tr>");
            out.println("<tr><td>Customer Phone Number:</td><td>" + custPhone + "</td></tr></table>");
 
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/projgameshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");;
            Statement stmt = conn.createStatement();
 
            // Print games ordered 
            out.println("<br />");
            out.println("<table border='1' cellpadding='6'>");
            out.println("<tr><th>GENRE</th><th>TITLE</th><th>PRICE</th><th>QTY</th></tr>");
 
            float totalPrice = 0f;
            for (String id : ids) {
               sqlStr = "SELECT * FROM games WHERE id = " + id;
               rset = stmt.executeQuery(sqlStr);
               rset.next();
               int qtyAvailable = rset.getInt("qty");
               String title = rset.getString("name");
               String genre = rset.getString("genre");
               float price = rset.getFloat("price");
 
               int qtyOrdered = Integer.parseInt(request.getParameter("qty" + id));
               sqlStr = "UPDATE games SET qty = qty - " + qtyOrdered + " WHERE id = " + id;
               stmt.executeUpdate(sqlStr);
 
               sqlStr = "INSERT INTO order_records values ("
                       + id + ", " + qtyOrdered + ", '" + custName + "', '"
                       + custEmail + "', '" + custPhone + "')";
               stmt.executeUpdate(sqlStr);
 
               // Display ordered games
               out.println("<tr>");
               out.println("<td>" + genre + "</td>");
               out.println("<td>" + title + "</td>");
               out.println("<td>" + price + "</td>");
               out.println("<td>" + qtyOrdered + "</td></tr>");
               totalPrice += price * qtyOrdered;
            }
 
            out.println("<tr><td colspan='4' align='right'>Total Price: $");
            out.printf("%.2f</td></tr>", totalPrice);
            out.println("</table>");
 
            out.println("<h3>Thank you.</h3>");
            out.println("<p><a href='viewgame'>Back to Select Menu</a></p>");
         }
         out.println("</body></html>");
      } catch (SQLException ex) {
         out.println("<h3>Service not available. Please try again later!</h3></body></html>");
         Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
      } /* finally {
         out.close();
         try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
         } catch (SQLException ex) {
            Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
      } */ 
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}