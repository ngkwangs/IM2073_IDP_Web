

public class CartItem {
 
   private int id;
   private String title;
   private String genre;
   private float price;
   private int qtyOrdered;
 
   // Getter Setter

   public CartItem(){

   }

   public CartItem(int id, String title,
           String genre, float price, int qtyOrdered) {
      this.id = id;
      this.title = title;
      this.genre = genre;
      this.price = price;
      this.qtyOrdered = qtyOrdered;
   }
 
   public int getId() {
      return id;
   }
 
   public String getGenre() {
      return genre;
   }
 
   public String getTitle() {
      return title;
   }
 
   public float getPrice() {
      return price;
   }
 
   public int getQtyOrdered() {
      return qtyOrdered;
   }
 
   public void setQtyOrdered(int qtyOrdered) {
      this.qtyOrdered = qtyOrdered;
   }
}