# Socket Communication

Client Program interact with each other using Sockets.Whenever a Client wants to request some data it will pass an JSON String. This JSON will contain following attributes.

* Message Id

This is a uniques identifier which is used to identify message.If a client sends message to server asking for some data with message id "x" then message id for response message sent by server to client will also be "x". It is used so that client can differntiate between 2 or more response messages.It is of type long and its value is fetched as milliseconds from Date.


* Action Code

It is an integer which specifies what task must be performed. For Exampe ther might be actioncode 1 which means client is requesting some data.

* Info

This is an  JSON Object.If a client is sending Message then this might contain some data which will be needed by server program to execute task.Otherwise if server program is sending the message then this might contain response data(data asked by client).

### Socket at Server Side

When a new client connection is accepted, this newly connected client starts executing on new thread and the main thread continues to listen for other connections.New Client thread is continously listening for new messages.When a message is received by socket appropriate action is taken place by server program.This execution takes place on new thread and the original client thread continues to listen for more messages.


### Socket at Client Side

When a client connects to server it creates a new thread and starts to listen for messages continously.When a message is received and apprioate action needs to be taken place then it will start executing certain code otherwise messages get placed in a message pool where other classes can access it.


### Action Codes

The action needed to perform when a message with certain action code is recived.

* Action Code 1

The Client is asking server to check crendential of teacher user.Info attribute contains phone of teacher. Response Info contains  password which is set "null" (String) if user does not exist and contains password if user exists.

* Action Code 2

The Client is asking server to add the details of New teacher id to Database.Info attribute contains an JSON object with all the attributes of teacher id.

* Action Code 3

The Client is asking server to give a list of all teachers in database. info contains nothing and response info contains JSON array where each JSON object contains firstname,lastname and phone number of teacher.

* Action Code 99

The Client is informing the server to close the socket.Info contains nothing