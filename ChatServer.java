import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.test.SimpleDateFormat;

/**
 * A chat server that delivers public and private messages.
*/
public class ChatServer {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;
    //An ArrayList to keep list of clients
    private static ArrayList<ClientThread> clients;
	// to display time
	private SimpleDateFormat sdf;

    public static void main(String args[]) {
        
        // The default port number.
        int portNumber = 5000 ;
        switch(args.length)
        {
        	case 0: {
						System.out.println("Usage: java ChatServer <portNumber>\n"
                    	+ "By default using port number=" + portNumber);
                    	break
					}

			case 1: {
						try 
						{
							portNumber = Integer.parseInt(args[0]);
							break;
						} 
						catch(Exception e) 
						{
							System.out.println("Invalid port number.");
							System.out.println("Usage is: > java Server [portNumber]");
							return;
						}
					}

			default:{
						System.out.println("Usage: java ChatServer <portNumber>")
						return;
					}			        
		}
        
	    /*
	     * Open a server socket on the portNumber (default 5000). Note that we can
	     * not choose a port less than 1023 if we are not privileged users (root).
	     */
        try 
        {
            serverSocket = new ServerSocket(portNumber);
        } 
        catch (IOException e) 
        {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }

	    /*
	     * Create a client socket for each connection and pass it to a new client
	     * thread.
	     */
	    bool ContinueRunningServer = true;
        while (ContinueRunningServer)
        {
            try 
            {
            	display("Server waiting for Clients on port " + portNumber + ".");
                clientSocket = serverSocket.accept();
                // break if server stoped
                if(!ContinueRunningServer)
                	break;
                
                // if client is connected, create its thread
				ClientThread t = new ClientThread(clientSocket, clients);
				
				//add this client to arraylist
				clients.add(t);
				
				t.start(); 

            } 
            catch (IOException e) 
            {
                String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
				display(msg);
            }
        }

        // try to stop the server
		try 
		{
				serverSocket.close();
				for(ClientThread tc : clients) 
				{
					try 
					{
						tc.CloseAll();
					}
					catch(IOException ioE)
					{
						display(ioE);			
					}
				}
		}
		catch(Exception e)
		{
			display("Exception closing the server and clients: " + e);
		}
    }

    // Display a message on console
	private void display(String msg){
		String time = sdf.format(new Date()) + " " + msg;
		System.out.println(time);
	}
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. The thread broadcast the incoming messages to all clients and
 * routes the private message to the particular client. When a client leaves the
 * chat room this thread informs also all the clients about that and terminates.
 */
class ClientThread extends Thread {

    private String clientName = null;
    private ObjectInputStream  is = null;
    private ObjectOutputStream  os = null;
    private Socket clientSocket = null;
    private final ArrayList<ClientThread> threads;
    
    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        /*
		 * Create input and output streams for this client.
		 */
        try
        {
        	is = new ObjectInputStream(clientSocket.getInputStream());
            os = new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch (IOException e) 
        {
			display("Exception creating new Input/output Streams: " + e);
			return;
		}            
    }

    // Display a message on console
	private void display(String msg){
		String time = sdf.format(new Date()) + " " + msg;
		System.out.println(time);
	}

    //Close everything
    public void CloseAll()
    {
    	try 
    	{
			if(is != null) is.close();
			if(os != null) os.close();
			if(clientSocket != null) clientSocket.close();
		}
		catch(Exception e)
		{
		}
    }

    public String GetClientName()
    {
    	return clientName;
    }
    
}
