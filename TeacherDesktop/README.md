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

### Additional Resources Used

The Additional Resoruces that are required by the program are stored in Jar Files  directory.
* JSON Parser files help to create,parse JSON Strings. 


##### Main Function

[Application.java](./src/TeacherProgram/Application.java) contains the static main function to be called to start exection of Program