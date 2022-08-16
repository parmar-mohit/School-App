# Server Program

### About
The aim of this server program is to connect with database and provide data to client programs/app as and when requested.This will be a multithreaded program.Whenever a new connection request comes from client a new thread will be created for that thread. Another important task of the program is to maintain a log of all records.

### Accepting Client Connections
When the program is executed it start to continously listen for incoming connection.If a connection request of a client is accepted it create a new Thread for this client and continous listening for another connection on main thread.
![](./Images/Client%20Connections.png)
[Application](./src/ServerProgram/Application.java) Class handles all the incoming Client Connection.


### Working of Client Module
When a new Client module or thread is created it in turns creates another thread called as receiver thread.Receiver thread continously listens for incoming messages from client. If a message is recived from client then it first retrieves action_code from client and then passes it to appropritate Module which provides necessary resources/data asked by client.This module works creates another thread and works on its own thread.The main client thread continues to listen for more incoming messages.
![](./Images/Client%20Module%20Working.png)
[Client](./src/ServerProgram/Client/Client.java) Class handles incoming messages from class and passes it to another thread/module/class for execution of task


### Additional Resources Used

The Additional Resoruces that are required by the program are stored in Jar Files  directory.
* [MySQL JDBC Connector](./Jar%20Files/MySQL%20JDBC%20Connector.jar) files help the java program to communicate with MySQl.
* [JSON Parser files](./Jar%20Files/JSON%20Parser.jar) help to create,parse JSON Strings.[Download Link](https://repo1.maven.org/maven2/org/json/json/20220320/)

### Java Main Method 

[Application.java](./src/ServerProgram/Application.java) contains the static main function to be called to start exection of Program