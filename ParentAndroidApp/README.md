# Parent Android App

### About

The App will be used by end user(parents).This App will let parents see marks of their kids.

### Authentication

To use this app, users i.e parent will have to enter their phone no and password to login. For more info refer [ParentAndroidApp Authentication](../Documentation/ParentAndroidApp%20Authentication.md)

### ServerConnection

[ServerConnection](./app/src/main/java/com/school/parentandroidapp/Server/ServerConnection.java) is a class/module that allows this program to connect to serverSocket and request resources.Whenever a ServerConnection object is instantiated it creates a new receiver Thread. This receiver thread continously listen for incoming messages from server.When a message from server is received it first checks if client has to execute some module, otherwise it stores the message in messagepool. Whenever the program requires some data from server it first asks the serverConnection to send a message requesting the resource and when a response message is received from server it is stored in messagepool which is an arraylist of messages of String data type.From this messagepool we can get our response message which will contain requested data.All the communication with client program is handled by ServerConnection class. As ServerConnection Object cannot be Passed between activities we have created a static reference for this Object in [MainActivity](./app/src/main/java/com/school/parentandroidapp/Activities/MainActivity.java) when a new actiivity is started it must obtain serverconnection reference from MainActivity Class

### Additional Resources Used

The Additional Resoruces that are required by the program are stored in Jar Files  directory.
* [JSON Parser](./app/libs/JSON%20Parser.jar) files help to create,parse JSON Strings.Package name has been changed from org.json to parser.json to avoid naming conflict with android's org.json package.[Download Link](https://repo1.maven.org/maven2/org/json/json/20220320/)