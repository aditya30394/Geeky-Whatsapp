import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient{

    // The client socket
    private static Socket clientSocket = null;
    // The output stream
    private static ObjectOutputStream os = null;
    // The input stream
    private static ObjectInputStream is = null;

    private static Scanner scan = null;
    
    private static String username = null;
    /*
     * To start the ChatClient in console mode use one of the following command
     * > java ChatClient username
     * > java ChatClient username portNumber
     * > java ChatClient username portNumber serverAddress
     * at the console prompt
     * If the portNumber is not specified 5000 is used
     * If the serverAddress is not specified "localHost" is used
     */
    public static void main(String[] args) {

        // The default port.
        int portNumber = 5000;
        // The default host.
        String host = "localhost";
        // different case according to the length of the arguments.
        switch(args.length) 
        {
        case 3:
            // for > javac ChatClient username portNumber serverAddr
            host = args[2];
        case 2:
            // for > javac ChatClient username portNumber
            try 
            {
                portNumber = Integer.parseInt(args[1]);
            }
            catch(Exception e) 
            {
                System.out.println("Invalid port number.");
                System.out.println("Usage is: > java Client username [portNumber] [serverAddress]");
                return;
            }
        case 1: 
            // for > javac ChatClient username
            username = args[0];
            break;
        default:
            // if number of arguments are invalid
            System.out.println("Usage is: > java Client username [portNumber] [serverAddress]");
        return;
        }

        System.out.printf("Username : %s \t Port Number: %d \t serverAddress: %s", username,  portNumber, host);
    
        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try 
        {
            clientSocket = new Socket(host, portNumber);
            scan = new Scanner(System.in);
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            is = new ObjectInputStream(clientSocket.getInputStream());
            String msg = "Connection accepted " + clientSocket.getInetAddress() + ":" + clientSocket.getPort();
            display(msg);
    
        } 
        catch (UnknownHostException e) 
        {
            System.err.println("Don't know about host " + host);
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't get I/O for the connection to the host "
                    + host);
        }

    /*
     * If everything has been initialized then we want to write some data to the
     * socket we have opened a connection to on the port portNumber.
     */
        if (clientSocket != null && os != null && is != null) {
            try 
            {
                start();//reads and prints on client side
                
                System.out.println("\nHello.! Welcome to the chatroom.");
                System.out.println("Instructions:");
                System.out.println("1. Simply type the message to send broadcast to all active clients");
                System.out.println("2. Type '@username<space>yourmessage' to send message to desired client");
                System.out.println("3. Type 'GETUSERS' without quotes to see list of active clients");
                System.out.println("4. Type 'SIGNOUT' without quotes to logoff from server");
                while(true) {
                    System.out.print("> ");
                    // read message from user
                    String msg = scan.nextLine();
                    // logout if message is SIGNOUT
                    if(msg.equalsIgnoreCase("SIGNOUT")) 
                    {
                        sendMessage(new Message(MessageType.SIGNOUT, ""));
                        Thread.sleep(2000);
                        break;
                    }
                    // message to check who are present in chatroom
                    else if(msg.equalsIgnoreCase("GETUSERS")) 
                    {
                        sendMessage(new Message(MessageType.GETUSERS, ""));               
                    }
                    // regular text message
                    else 
                    {
                        sendMessage(new Message(MessageType.MESSAGE, msg));
                    }
                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
                CloseAll();
            } 
            catch (Exception e) 
            {
                System.err.println("IOException:  " + e);
            }
        }
    }

    /* To start the chat client
     */
    public static void start() {        
        // creates the Thread to listen from the server 
        new MessageListener(is).start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be Message objects
        try
        {
            os.writeObject(username);
        }
        catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            CloseAll();
        }
    }

    /*
     * To send a message to the console
     */
    private static void display(String msg) {
        System.out.println(msg);
    }
    
    /*
     * To send a message to the server
     */
    static void sendMessage(Message msg) {
        try 
        {
            os.writeObject(msg);
        }
        catch(IOException e) 
        {
            display("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect
     */
    static private void CloseAll() {
        try 
        { 
            if(is != null) is.close();
            if(os != null) os.close();
            if(scan != null) scan.close();
            if(clientSocket != null) clientSocket.close();
        }
        catch(Exception e) {}            
    }
}

/*
 * a class that waits for the message from the server
 */
class MessageListener extends Thread {
    private ObjectInputStream is;

    MessageListener(ObjectInputStream is)
    {
        this.is = is;
    }

    public void run() {
        while(true) {
            try {
                // read the message form the input datastream
                String msg = (String) is.readObject();
                // print the message
                System.out.println(msg);
                System.out.print("> ");
            }
            catch(IOException IOE) {
                System.out.println(" *** " + "Server has closed the connection: " + IOE + " *** ");
                break;
            }
            catch(ClassNotFoundException e) {
            }
        }
    }
}