// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    this.sendToServer("#login " + loginID);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try{
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else
    		sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command) {
	  if(command.equals("#quit")) {
		  clientUI.display("BYE!");
		  quit();
	  }
	  else if(command.toLowerCase().equals("#logoff")) {
		  try {
			  if(this.isConnected()) {
				  this.closeConnection();
			  }
			  else
				  clientUI.display("Client is already disconnected.");
			
		} catch (IOException e) {
			clientUI.display("An error has occured.");
		}
	  }
	  else if(command.toLowerCase().equals("#logoin")) {
		  try {
			  if(this.isConnected()) {
				  clientUI.display("Client is already disconnected.");
			  }
			  else
				  this.openConnection();
			
		} catch (IOException e) {
			clientUI.display("An error has occured.");
		}
	  }
	  else if(command.toLowerCase().startsWith("#sethost")) {
		  if (this.isConnected()) {
			  clientUI.display("Client needs to be disconected to set host.");
		  }
		  else {
			  this.setHost(command.substring(9));
			  clientUI.display("Host changed to: "+getHost());
		  }
	  }
	  else if(command.toLowerCase().startsWith("#setport")) {
		  if (this.isConnected()) {
			  clientUI.display("Client needs to be disconected to set port.");
		  }
		  else {
			  this.setPort(Integer.parseInt(command.substring(9)));
			  clientUI.display("Port changed to: "+getPort());
		  }
	  }
	  else if(command.toLowerCase().equals("#gethost")) {
		  clientUI.display("Current host is: " + getHost());
	  }
	  else if(command.toLowerCase().equals("#getport")) {
		  clientUI.display("Current port is: " + getPort());
	  }
	  else
		  clientUI.display("The command does not exist.");
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  @Override
  /**
	 * Implementation of hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  protected void connectionClosed() {
	  clientUI.display("The connection has been closed");
  }
  
  @Override
	/**
	 * Implementation of hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down");
	  System.exit(0);
  }
  
}
//End of ChatClient class
