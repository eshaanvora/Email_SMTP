import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
*  Client-Server Email Program.
*  Receives email information via lines of input from the keyboard and sends input to the server
*  Server sends email based on information from client
*
*  @author: Eshaan Vora
*      Email:  EshaanVora@gmail.com
*      Date:  2/20/2021
*  @version: 3.2
*/

class Email {
  public static void main(String[] argv) throws Exception {

    //INITIALIZE INPUT VARIABLES
    String messageLine;
    String messageBody = "";

    Socket clientSocket = null;

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    //RECEIVE INPUT FROM USER
    System.out.println("Enter FROM ADDRESS: ");
    final String fromAddress = inFromUser.readLine();
    System.out.println("Enter TO ADDRESS: ");
    final String toAddress = inFromUser.readLine();
    System.out.println("Enter FROM NAME: ");
    final String fromUsername = inFromUser.readLine();
    System.out.println("Enter TO NAME: ");
    final String toUsername = inFromUser.readLine();
    System.out.println("Enter SUBJECT: ");
    final String emailSubject = inFromUser.readLine();
    System.out.println("Enter MESSAGE BODY");
    //COMBINE USER INPUT LINES INTO ONE STRING FOR MESSAGEBODY
    //MESSAGE BODY ENDS IF USER INPUTS A PERIOD.
    while (true) {
      messageLine = inFromUser.readLine();
      if (messageLine.equals(".")) {
        messageBody += messageLine;
        break;
      } else {
        messageBody += messageLine;
        messageBody += "\n";
      }
    }

    //ESTABLISH SOCKET CONNECTION TO SERVER
    try {
      clientSocket = new Socket("smtp.chapman.edu", 25);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }

    BufferedReader inFromServer =  new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));

    PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);

    //WELCOME MESSAGE
    String welcomeMessage = inFromServer.readLine();
    System.out.println("FROM SERVER: " + welcomeMessage);

    //SEND CLIENT'S MESSAGE TO SERVER
    //ALSO DISPLAY SENT COMMANDS
    outToServer.println("HELO icd.chapman.edu");
    System.out.println("FROM CLIENT: HELO icd.chapman.edu");
    String serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER: " + serverResponse);

    outToServer.println("MAIL FROM: " + fromAddress);
    System.out.println("FROM CLIENT: MAIL FROM: " + fromAddress);
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER: " + serverResponse);

    outToServer.println("RCPT TO: " + toAddress);
    System.out.println("FROM CLIENT: RCPT TO: " + toAddress);
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER: " + serverResponse);

    outToServer.println("DATA");
    System.out.println("FROM CLIENT: DATA");
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER: " + serverResponse);

    outToServer.println("From: " + fromUsername);
    System.out.println("FROM CLIENT: " + fromUsername);

    outToServer.println("To : " + toUsername);
    System.out.println("FROM CLIENT: " + toUsername);

    outToServer.println("Subject : " + emailSubject);
    System.out.println("FROM CLIENT: " + emailSubject);

    System.out.println("FROM CLIENT: " + messageBody);
    outToServer.println(messageBody);
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER: " + serverResponse);

    //QUIT EMAIL CLIENT/SERVER
    outToServer.println("QUIT");
    System.out.println("FROM CLIENT: QUIT");
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER: " + serverResponse);

    //CLOSE SOCKET
    clientSocket.close();
  }
}
