# Program Communication

---

Server program and Client program exchange data between each other using socket.Data is sent through sockets as JSON String, which helps in sending multiple data types.A JSON String sent from Client to Server is called as Message and an JSON String sent from Server to Client in response to Message is Called as Response Message.Every Message/Response Message has following 3 data

* Message Id

This is a unique identifier which is used to identify message.If a client sends message to server asking for some data with message id "x" then message id for response message sent by server to client will also be "x". It is used so that client can differntiate between 2 or more response messages.It is of type long and its value is fetched as milliseconds from Date.

* Action Code

It is an integer which specifies what client is requesting from Server.Details about diiferent action code and its detail is provided in [Action Codes](Action%20Codes.md)

* Info

This is an attribute containing data that will be required for processing. Data type and Size for this attribute is dependent on Action Code.[Action Codes](Action%20Codes.md) specifies what data each message/response message will contain depending on action code.

---

## Packets

As the largest amount of data we can send in one stream is 65535 bytes, we cannot send large data in one stream.For this the JSON String is divided into smaller string of size <= 65000 and a new JSON Object is created with following attributes

* Packet Id
Packet Id is used to identify all packets of same Message

* Packet No 
Unique identifier for packet that helps while forming original message

* Total Packets
Total packets that are being sent or will being sent by other program

* Data
This contains the smaller sub string of original message/response message of length 65000 <= 65000

After all packets has been received by program we can concatenate string from data attribute to get original message


**Example :** Consider that client want to send message to server but the length of JSON String is 80000,as wen can't send message in single stream, we will send message in 2 packets first packet's data attribute will contain JSON String from index 0 to 64999 and second packet will contain JSON String from index 65000 to 79999.Once both the packets are received by the Server Program it will then concatenate data attribute from both messages to get original message

---