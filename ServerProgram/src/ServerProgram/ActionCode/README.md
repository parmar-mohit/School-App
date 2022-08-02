# Action Codes

This directory contains all the modules/classes that handle incoming request from client messages.All the Classes in this directory extends Thread class and have 2 common fields.
* JSONObject : A JSONObject is used to store the incoming messages and get the data from that will be required to process from this JSON Object
* Client : Client Object is refered to send response messages to client and get ip address of client while writing log4

Above Fields are also the parameter that are used to instantiate the Object of Classes.

Other than the above mentioned fields majority of Classes have another DatabaseCon field used to connect and get data from database.