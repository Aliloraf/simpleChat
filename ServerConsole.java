import java.io.*;
import java.util.Scanner;
import client.*;
import common.*;

public class ServerConsole implements ChatIF{
	  //Class variables 
	  final public static int DEFAULT_PORT = 5555;
	  EchoServer server;
	  Scanner fromConsole;
	  
	  //Constructors 
	  public ServerConsole(int port) {
		  try 
		    {
		      server = new EchoServer(port, this);
		    } 
		    catch(IOException exception) 
		    {
		      System.out.println("Error: Can't setup connection!"
		                + " Terminating client.");
		      System.exit(1);
		    }
		    
		    try
		    {
		      server.listen(); //Start listening for connections
		    }
		    catch (Exception ex)
		    {
		      System.out.println("Could not listen for clients!");
		    }
		    fromConsole = new Scanner(System.in);
	  }
	  
	  public void accept() {
		    try
		    {

		      String message;

		      while (true) 
		      {
		        message = fromConsole.nextLine();
		        server.handleMessageFromServerUI(message);
		      }
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println("Unexpected error while reading from console!");
		    }
	  }
	  
	  public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	  
	  public static void main(String[] args) 
	  {
	    String host = "";
	    int port = 0;

	    try{
	    	host = args[0];
	    	port = Integer.parseInt(args[1]);
	    }
	    catch(Throwable e)
	    {
	      port = DEFAULT_PORT;
	    }
	    ServerConsole server = new ServerConsole(port);
	    server.accept();
	  }
}
