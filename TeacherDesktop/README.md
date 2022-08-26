# Teacher Desktop

### About

This Program will be used by end user(Teacher). This Program will allow user to create new grades, enter data of student,parents, enter data of exams. After the user enters this data in program the data will be sent to ServerProgram which will then store them into database.

### Authentication

To use this program users will have to enter their id(phone no) and password. There will be special user whose username will be "123456" which will be able to perform that are not available to other users.This user will be known as pricipal user.

### Principal User
The Principal user will be able to do following task
* Create Id for Student and their Parents
* Create Id for Teacher
* Create Grades
* Assign Teacher Incharge for a Grade
* Assign Subject and Subject Teacher to Grades

### Other User
* View Details of Student
* Create and Enter Score of Exams

### ServerConnection

[ServerConnection](./src/TeacherDesktop/Server/ServerConnection.java) is a class/module that allows this program to connect to serverSocket and request resources.Whenever a ServerConnection object is instantiated it creates a new receiver Thread. This receiver thread continously listen for incoming messages from server.When a message from server is received it first checks if client has to execute some module, otherwise it stores the message in messagepool. Whenever the program requires some data from server it first asks the serverConnection to send a message requesting the resource and when a response message is received from server it is stored in messagepool which is an arraylist of messages of String data type.From this messagepool we can get our response message which will contain requested data.All the communication with client program is handled by ServerConnection class. ServerConnection Class object is passed from class/function to other as and when required.

### Additional Resources Used

The Additional Resoruces that are required by the program are stored in Jar Files  directory.
* [JSON Parser](./Jar%20Files/JSON%20Parser.jar) files help to create,parse JSON Strings.[Download Link](https://repo1.maven.org/maven2/org/json/json/20220320/) 
* [JCalendar](./Jar%20Files/JCalendar.jar) has JDateChooser Class which helps in taking Date inputs.


### Java Main Method

[Application.java](./src/TeacherProgram/Application.java) contains the static main function to be called to start exection of Program

### Interface

Below are some snapshot of Interface of this program

![](./Images/Login%20Interface.PNG)
<p align="center">Fig 1. Login Interface</p>

![](./Images/Principal%20Classroom%20Interface.PNG)
<p align="center">Fig 2. Princiapal Classroom/p>

![](./Images/My%20Classroom%20Interface.PNG)
<p align="center">Fig 3. My Classroom Interface</p>

![](./Images/New%20Exam%20Interface.PNG)
<p align="center">Fig 4. New Exam Interface</p>