package ObjectHandling;

public class LibrarayHub {
	  public static void main(String[]args){
		    Book b=new Book("1");
		    Book b1=new Book("1");
		    Book b2=new Book("2");
		    Book b3=new Book("3");
		   if( b.equals(b1)){
		    System.out.println("The two books have same bookID");
		   }
		    if(b2.equals(b3)){
		    System.out.println("The two books have same bookID");
		   }
		    else{
		          System.out.println("The two books have unique bookID");

		    }
		   }

		}

		 class Book{
		   public String bookID;
		   public String title;
		   public String author;
		   public Book(String bookID)
		   {
		    this.bookID=bookID;
		   
		   }
		   @Override
		   public boolean equals(Object obj)
		   {
		    Book b=(Book) obj;
		    return this.bookID.equals(b.bookID);
		   }
		}
