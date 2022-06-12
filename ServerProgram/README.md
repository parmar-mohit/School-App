# Server Program

### About
The aim of this server program is to connect with database and provide data to client programs/app as and when requested.This will be a multithreaded program.Whenever a new connection request comes from client a new thread will be created for that thread. Another important task of the program is to maintain a log of all records.

### Additional Resources Used

The Additional Resoruces that are required by the program are stored in Jar Files  directory.
* MySQL JDBC Connector files help the java program to communicate with MySQl.
* JSON Parser files help to create,parse JSON Strings.

#### Main Function

[Application.java](./src/ServerProgram/Application.java) contains the static main function to be called to start exection of Program