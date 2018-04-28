# Geeky-Whatsapp

A multithreaded client-server architecture based Chat Application using Java Socket programming. 
A server continuously listens for connection requests from clients across the network or even from the same machine.
Clients connect to the server using an IP address and port number. The client needs to provide a unique username while 
connecting to the server. This username is treated as the unique identifier for that client.

All the messages from client are sent to the server using ObjectOutputStream in java. After receiving the message 
from the client, the server broadcasts the message if it is not a private message. And if it is a private message, which is 
detect using ‘@’ followed by a valid username, sends the message only to that user.

All the messages sent from various clients can be seen on the server console.

## Instructions

**To compile the Programs**

```
javac ChatServer.java
javac ChatClient.java
```

**Server**

To start the server
 ```
java ChatServer
java ChatServer [portNumber]
```
If the port number is not specified, port 5000 is used by default
<p align="center">
  <figure align="center">
  <img src="https://raw.githubusercontent.com/aditya30394/geeky-whatsapp/master/images/starting-the-server.png">
  <figcaption>Starting the Chat Server</figcaption>
  </figure>
  <br/>
</p>

**Client**

To start the Client in console mode use one of the following command
 ```
java ChatClient username
java ChatClient username portNumber
java ChatClient username portNumber serverAddress
```
At the console prompt
* If the portNumber is not specified 5000 is used
* If the serverAddress is not specified "localHost" is used

<p align="center">
  <figure align="center">
  <img src="https://raw.githubusercontent.com/aditya30394/geeky-whatsapp/master/images/starting-the-client.png">
  <figcaption>Starting the Chat Client</figcaption>
  </figure>
  <br/>
</p>

**Chat**

On the client console:
1. Simply type the message to send broadcast to all the active users
2. Type '@username<space>yourmessage' without quotes to send message to desired client
3. Type 'GETUSERS' without quotes to see list of active clients
4. Type 'SIGNOUT' without quotes to logoff from server

## Screenshots

In the screenshots below, three clents namely "Aditya", "Abhishek" and "Himanshu" are active on the chat server.

<p align="center">
<figure align="center">
  <img src="https://raw.githubusercontent.com/aditya30394/geeky-whatsapp/master/images/chat-server-console.png">
  <figcaption>Console of the Chat Server</figcaption>
</figure>  
  <br/>
</p>

<p align="center">
<figure align="center">
  <img src="https://raw.githubusercontent.com/aditya30394/geeky-whatsapp/master/images/console-aditya.png">
  <figcaption>Console of user Aditya</figcaption>
</figure>  
  <br/>
</p>

<p align="center">
<figure align="center">
  <img src="https://raw.githubusercontent.com/aditya30394/geeky-whatsapp/master/images/console-abhishek.png">
  <figcaption>Console of user Abhishek</figcaption>
</figure>  
  <br/>
</p>

<p align="center">
<figure align="center">
  <img src="https://raw.githubusercontent.com/aditya30394/geeky-whatsapp/master/images/console-himanshu.png">
  <figcaption>Console of user Himanshu</figcaption>
</figure>  
  <br/>
</p>

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
