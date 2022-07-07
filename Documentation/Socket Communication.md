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

The Client is asking server to check crendential of teacher user.Info attribute of message contains phone of teacher.
Example of message is as follows
```
{
    "id" : $messageId,
    "action_code" : 1,
    "info" : {
        "phone" : $phone_no_of_teacher
    }
}
```
Info attribute of response message contains "null" if user does not exist or it contains hashed password saved in database.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "password" : $null_or_password
    }
}
```

* Action Code 2

The Client is asking server to add the details of New teacher id to Database.Info attribute of message contains an JSON object with all the attributes of teacher id.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 2,
    "info" : {
        "phone" : $phone,
        "firsname" : $firstname,
        "lastname" : $lastname,
        "email" : $email,
        "gender: : $gender,
        "password" : $hashed_password
    }
}
```
Info attribute of response message contains responseCode which is set to 0 if teacher id was successfully added to database, Or else it is set to 1 if teacher id was not added because a teacher with same phone exist in database.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0 or 1
    }
}
```

* Action Code 3

The Client is asking server to give a list of all teachers in database. There is no Info attribute of message.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 3
}
``` 
Info attribute of response message contains an array of JSONObject where each Object contains attribuutes of teacher.Example is as follows
```
{
    "id" : $messageId,
    "info" : [
        {
            "firstname" : $firstname_of_teacher1,
            "lastname" : $lastname_of_teacher1,
            "phone" : $phone_of_teacher1,
            "email" : $email_of_teacher1,
            "gender" : $gender_of_teacher1
        },
        {
            "firstname" : $firstname_of_teacher2,
            "lastname" : $lastname_of_teacher2,
            "phone" : $phone_of_teacher2,
            "email" : $email_of_teacher2,
            "gender" : $gender_of_teacher2
        },
        {
            "firstname" : $firstname_of_teacher3,
            "lastname" : $lastname_of_teacher3,
            "phone" : $phone_of_teacher3,
            "email" : $email_of_teacher3,
            "gender" : $gender_of_teacher3
        }
    ]
}
```

* Action Code 4

The Client is requesting server to create a new classrooom. Info attribute of message contains all the details of classroom to be created in JSON format.Example message is as follows
```
{
    "id" : $messageId,
    "action_code" : 4,
    "info" : {
                "standard" : $standard,
                "division" : $division,
                "teacher_incharge" : $phone_no_of_teacher_incharge,
                "subject_list" : [
                    {
                        "subject_name" : $subject_name_1,
                        "subject_teacher" : $phone_no_of_subject_teacher,
                    },
                    {
                        "subject_name" : $subject_name_2,
                        "subject_teacher" : $phone_no_of_subject_teacher,
                    },
                    {
                        "subject_name" : $subject_name_3,
                        "subject_teacher" : $phone_no_of_subject_teacher,
                    }
                ]
            }
}
```

Info Attribute of Response Message will contain response code
indicating status of the request. Response code is set to 0 if request was fulfilled successfully, or is set to 1 is a grade with same standard and division exist.Example is given below
```
{
    "id" : $messageid,
    "info" : {
        "response_code" : 0 or 1
    }
}
```

* Action Code 5

The Client is requesting server to change password of a user.Info attribute of message contains phone no of user whoose password is to be changed and new password.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 5,
    "info" : {
        "phone" : $phone_of_teacher,
        "password" : $new_hashed_password
    }
}
```
Info Attribute of response message contains response code which is set to 0 when passsword is changed successfully.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0
    }
}
```

* Action Code 6

The Client is requesting server to update Details of Teacher.Info attribute of message contains all attributes of teacher.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 5,
    "info" : {
        "phone" : $phone,
        "firstnamee" : $new_firstname,
        "lastname" : $new_lastname,
        "email" : $new_email,
        "gender" : $new_gender
    }
}
```

Info attribute of response message contains response code which is set to 0 if updation was successfull.
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0
    }
}
```

* Action Code 7

The Client is requesting server to given details of all classroom available in database.There is no Info atrribute in message.Example is as follows
```
{
    "id" : $messadeId,
    "action_code" : 7
}
```

Info Attribute of Response Message contains an JSON array, this array has JSON object containing information about each classroom, Example is as follows
```
{
    "id" : $messageId,
    "info" : [
        {
            "standard" : $standard_1,
            "division" : $division_1,
            "teacher" : {
                "phone" : $phone_no_of_teacherIncharge,
            "firstname" : $firstname_of_teacherIncharge,
            "lastname" : $lastname_of_teacherIncharge,
            },
            "subject_list" : [
                {
                    "subject_name" : $subjectname_1,
                    "teacher" : {
                        "phone" : $phone_no_of_subjectIncharge,
                    "firstname" : $firstname_of_subjectIncharge,
                    "lastname" : $lastname_of_subjectIncharge
                    }
                },
                 {
                    "subject_name" : $subjectname_2,
                    "teacher" : {
                        "phone" : $phone_no_of_subjectIncharge,
                        "firstname" : $firstname_of_subjectIncharge,
                        "lastname" : $lastname_of_subjectIncharge
                    }  
                }
            ]
        },
        {
            "standard" : $standard_2,
            "division" : $division_2,
            "teacher" : {
                "phone" : $phone_no_of_teacherIncharge,
            "firstname" : $firstname_of_teacherIncharge,
            "lastname" : $lastname_of_teacherIncharge,
            },
            "subject_list" : [
                {
                    "subject_name" : $subjectname_1,
                    "teacher" : {
                        "phone" : $phone_no_of_subjectIncharge,
                    "firstname" : $firstname_of_subjectIncharge,
                    "lastname" : $lastname_of_subjectIncharge
                    }
                },
                 {
                    "subject_name" : $subjectname_2,
                    "teacher" : {
                        "phone" : $phone_no_of_subjectIncharge,
                        "firstname" : $firstname_of_subjectIncharge,
                        "lastname" : $lastname_of_subjectIncharge
                    }  
                }
            ]
        }
    ]
}
```

* Action Code 8

The Client is requesting server to delete a teacher Id.Info attribute of message contains phone no of teacher whoose id is to be deleted.Exmaple is as follows
```
{
    "id" : $messageId,
    "action_code" : 8,
    "info" : {
        "phone" : $phone_of_teacher
    }
}
```

Info attribute of response message contains response code which is set to 0 if teacher was deleted and set to 1 if teacher was not deleted because teacher is incharge of a class or subject.This check is done to make sure that we do not delete a teacher which has been appointed some duty.Example of JSON is as follows
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0 or 1
    }
}
```

* Action Code 9 
The Client is requesting server to update details of a classroom.Info Attribute of message contains details about classroom.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 9,
    "info" : {
        "standard" : $standard,
        "division" : $division,
        "teacher_incharge" : $phone_of_teacher,
        "subject_list" : [
            {
                "old_subject_name" : $old_subject_name1,
                "new_subject_name" : $subject_name_1,
                "old_subject_teacher" : $phone_of_teacher,
                "new_subject_teacher" : $phone_of_teacher,
            },
            {
                "old_subject_name" : $old_subject_name2,
                "new_subject_name" : $subject_name_2,
                "old_subject_teacher" : $phone_of_teacher,
                "new_subject_teacher" : $phone_of_teacher,
            },
            {
                "old_subject_name" : $old_subject_name3,
                "new_subject_name" : $subject_name_3,
                "old_subject_teacher" : $phone_of_teacher,
                "new_subject_teacher" : $phone_of_teacher,
            }
        ]
    }
}

Note : if new subject is created while editing then old_subject_name  and ols_subject_teacher will contain null
```
Info attribute of response message contains response code which is set to 0 indicating that classroom has been updated successfully.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0
    }
}
```

* Action Code 10

The Client is requesting the server to delete data of classroom.Info attribute of message contains details about the classroom to be deleted.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 10,
    "info" : {
        "standard" : $standard,
        "division" : $division
    }
}
```

Info Attribute of response message contains response code indicating status of this request.Response code is set to 0 if request is completed succesfully.Example is as follow
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0
    }
}
```

* Action Code 11

The Client is requesting server to provide the information about classroom with a particular teacher incharge.Info attribute of message contains the phone no of teacher.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 11,
    "info" : {
        "phone" : $phone_of_teacher
    }
}
```

Info Attribute of response message contains an arrray, where each object has an stanadard and all the division with that standard.Example is as follows
```
{
    "id" : $messageId,
    "info" : [
        {
            "standard" : $standard,
            "division" : [$divisionArray]
        },
        {
            "standard" : $standard,
            "division" : [$divisionArray]
        }
    ]
}
```

* Action Code 12

The Client is requesting server to create new Student Id.Info attribute of message conatins all information about student.Example is as folows.
```
{
    "id" : $messageId,
    "action_code" : 12,
    "info" : {
        "firstname" : $firstname,
        "lastname" : $lastname,
        "email" : $email,
        "phone" : $phone,
        "gender" : $gender,
        "dob" : $date_of_birth,
        "standard" : $standard,
        "division" : $division,
        "roll_no" : $roll_no,
        "father_firstname" : $father_firstname,
        "father_lastname" : $father_lastname,
        "father_phone" : $father_phone,
        "father_email" : $father_email,
        "mother_firstname" : $mother_firstname,
        "mother_lastname" : $mother_lastname,
        "mother_phone" : $mother_phone,
        "mother_email" : $mother_email,
        "img" : $img
    }
}

Note : If input for any of the above field is not given by user then it will contain "null"(String).Dob is of long data type,obtained from Date.getTime() method.Image contains an Base64 encoded String so that image is being able to be sent through JSON.
```

Info attribute of response message contains sid of the newly created Student ID.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "sid" : $sid;
    }
}
```

* Action Code 13

The Client is requesting server to provide list of student who are in classroom with a particular teacher incharge.Info attribute contains phone no of teacher incharge.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 13,
    "info" : {
        "phone" : $phone_of_teacher_incharge
    }
}
```

Info attribute of response message contains an JSON array with details of all students.Example is as follows
```
{
    "id" : $messageId,
    "info" : [
        {
            $info_about_student1
        },
        {
            $info_about_student2
        },
        {
            $info_about_student3
        }
    ]
}

Info of student contains all the attributes that were sent while creating student id,Refer Action Code 12
```

* Action Code 14

The Client is requesting server to provide list of all students that are being taught by a particular teacher.Info attribute of message contains the phone of teacher.Example is as follows.
```
{
    "id" : $messageId,
    "action_code" : 14,
    "info" : {
        "phone" : $phone_no_of_teacher
    }
}
```

Info Attribute of response message contains all the attribute that were sent while creating student id, Refer Action Code 12.

* Action Code 15

The Client is requesting server to delete the Student Info Attribute contains sid of student.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 15,
    "info" : {
        "sid" : $sid_of_student
    }
}
```

Info attribute of response message contains response code which is set to 0 indicating that student id has been deleted.Example is as follows
```
{
    "id" : messageId,
    "info" : {
        "response_code" : 0
    }
}
```

* Action Code 16

The Client is requesting server to update details of a student.Info attribute of message contains all attributes of student.Refer action Unlike action code 12 info attribute may or may not have img attribute.If it has img attribute then image has been updated or else image has not been updated.Example is as follows.
```
{
    "id" : $messageId,
    "action_code" : 12,
    "info" : {
        "firstname" : $firstname,
        "lastname" : $lastname,
        "email" : $email,
        "phone" : $phone,
        "gender" : $gender,
        "dob" : $date_of_birth,
        "standard" : $standard,
        "division" : $division,
        "father_firstname" : $father_firstname,
        "father_lastname" : $father_lastname,
        "father_old_phone" : $father_old_phone,
        "father_new_phone" : $father_new_phone,
        "father_email" : $father_email,
        "mother_firstname" : $mother_firstname,
        "mother_lastname" : $mother_lastname,
        "mother_old_phone" : $mother_old_phone,
        "mother_new_phone" : $mother_new_phone,
        "mother_email" : $mother_email,
        "img" : $img
    }
}
```

The Info attribute of response message contains response code set to 0 indicating that Student Id has been updated.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0
    }
}
```

* Action Code 17

The Client is requestint server to provide information of all students as well as provide list of all classrooms as in Action Code 11.Info Attribute is not there in message.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 17
}
```

Info attribute of response message contains 2 array one array containing list of student as in action code 13 and second array will contain list of classrooms as in action code 11.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "student_list" : $studentListArray
        "classroom_list" : $classroomListArray
    }
}
```

* Action Code 18

The Client is requesting server to provide a list of all distinct exam names and a list of all subjects taught by a teacher.This action code is used while creating new exam.Info attribute contains phone no of teacher.Example is as folows.
```
{
    "id" : $messageId,
    "action_code" : 18,
    "info" : {
        "phone" : $phone_no_of_teacher
    }
}
```

Info attribute of response message contains list of all distinct exam names and list of subjects taught by teacher.Example is as follows
```
{
    "id" : $messageId,
    "info" : {
        "exam_name" : [$exam_name_array],
        "subject_list" : [
            {
                "subject_id" : $sub_id,
                "subject_name" : $subject_name,
                "standard" : $standard,
                "division" : $division,
                "phone" : $phone_of_teacher
            },
            {
                "subject_id" : $sub_id,
                "subject_name" : $subject_name,
                "standard" : $standard,
                "division" : $division,
                "phone" : $phone_of_teacher
            },
            {
                "subject_id" : $sub_id,
                "subject_name" : $subject_name,
                "standard" : $standard,
                "division" : $division,
                "phone" : $phone_of_teacher
            }
        ]
    }
}
```