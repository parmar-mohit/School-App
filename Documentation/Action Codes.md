# Action Codes

---

## Action Code 1

**About :** Client is requesting Server to provide password of a teacher.

**Info Attribute of Message** : contains phone no of teacher whoose password is requested.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 1,
    "info" : {
        "phone" : $phone_no_of_teacher
    }
}
```

**Number of Response Message** : Only one Response Message will be sent.

**Info Attribute of Response Message** : contains hashed hashes password if user teacher id with given phone no exist in database,else contains null

**Example Repsonse Message**
```
{
    "id" : $messageId,
    "info" : {
        "password" : $null_or_password
    }
}
```

**Module processing the request in  Server Program :** [GetCredentials](../ServerProgram/src/ServerProgram/ActionCode/GetCredentials.java)

---

## Action Code 2

**About :** Client is requesting server to create a new Teacher Id.

**Info Attribute of Message** contains all details(firstname,lastname,phone,email,gender,password) required to add Teacher Id to Database.

**Example Message**
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

**Number of Response Message :** Only one Response Message will be sent.

**Info Attribute of Response Message** contains Response Code indicating status of the request.
* Response Code 0 : Teacher Id has been created Sucessfully.
* Response Code 1 : Teacher Id was not created because there exist a Teacher with same phone no as the phone no obtained from message.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing the request in Server Program :** [CreateTeacherId](../ServerProgram/src/ServerProgram/ActionCode/CreateTeacherId.java)

---

## Action Code 3

**About :** Client is requesting to provide a list of all teacher available in database.

**Info Attribute of Message** does not exist for this action Code

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 3
}
```

**Number of Response Message :** n+1 messages will be sent, where n is the number of teachers available in database.

**Info Attribute of Response Message** contains number of teacher available for the first response message and for all other response messages info attribute holds data about a individual teacher id.

**Example Response Message**
```
First message Containing number of teacher available in database.
{
    "id" : $messageId,
    "info" : {
        "total_teachers" : $total_teachers
    }
}

Other messages containing inidividual teacher data
{
    "id" : $messageId,
    "info" : {
        "firstname" : $firstname_of_teacher1,
        "lastname" : $lastname_of_teacher1,
        "phone" : $phone_of_teacher1,
        "email" : $email_of_teacher1,
        "gender" : $gender_of_teacher1
    }
}
```

**Module processing request in Server Program :** [GetTeacherList](../ServerProgram/src/ServerProgram/ActionCode/GetTeacherList.java)

---

## Action Code 4

**About :** Client is requesting server to create new classroom in database.

**Info Attribute of Message** contains alll details required to create new classroom.

**Example Message**
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

**Number of Response Message :** Only one response message will be sent

**Info Attribute of Response Message** conatins response code indicating status of request
* Response Code 0 : Classroom Created Sucessfully.
* Response Code 1 : Classroom was not created because a classroom with same standard and division exist in database.

**Example Response Message**
```
{
    "id" : $messageid,
    "info" : {
        "response_code" : $responseCode
}
```

**Module processing request in Server Program :** [CreateClassroom](../ServerProgram/src/ServerProgram/ActionCode/CreateClassroom.java)

---

## Action Code 5

**About :** Client is requesting server to update password of a teaher id.

**Info attribute of Message** contains phone no of teacher and new hashed password.

**Example Message**
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

**Number of Response Message :** Only one message will be sent.

**Info Attribute of Respone Message** contains response code indicating status of request.
* Response Code 0 : Password has been changed successfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [ChangePassword](../ServerProgram/src/ServerProgram/ActionCode/ChangePassword.java)

---

## Action Code 6

**About :** Client is requesting server to update details of teacher.

**Info attribute of Message** contains phone no of teacher and all other new details of teacher.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 6,
    "info" : {
        "phone" : $phone,
        "firstnamee" : $new_firstname,
        "lastname" : $new_lastname,
        "email" : $new_email,
        "gender" : $new_gender
    }
}
```

**Number of Response Message :** Only one message will be sent.

**Info attribute of Response Message** contains response code indicating status of request.
* Response Code 0 : Teacher Id has been updated Sucessfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [UpdateTeacherAttributes](../ServerProgram/src/ServerProgram/ActionCode/UpdateTeacherAttributes.java)

---

## Action Code 7

**About :** Client os requesting server to provide details of all classroom available in database.

**Info Attribute of Message** does not exist.

**Example Message**
```
{
    "id" : $messadeId,
    "action_code" : 7
}
```

**Number of Response Message :** n+1 messages will be sent where n is the number of classrooms available in database

**Info Attribute of Response Message** contains number of classrooms for first response message else contains details about inidvidual classroom for other response messages

**Example Response Message**
```
First Response Message containing total number of classroooms
{
    "id" : $messageId,
    "info" : {
        "total_classrooms" : $totalClassrooms
    }
}

Other Response Messages containing details about individual classroom
{
    "id" : $messageId,
    "info" : {
        "standard" : $standard,
        "division" : $division,
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
}
```

**Module processing request in Server Program :** [GetClassroomListForPrincipal](../ServerProgram/src/ServerProgram/ActionCode/GetClassroomListForPrincipal.java)

---

## Action Code 8

**About :** Client is requesting server to delete a Teacher Id.

**Info Attribute of Message** contains phone number of teacher whoose id is to be deleted.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 8,
    "info" : {
        "phone" : $phone_of_teacher
    }
}
```

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains response code indicating status of request.
* Response Code 0 : Teacher Id has been deleted successfully.
* Response Code 1 : Teacher Id was not deleted because teacher is incharge of some classroom or teaches a subject to some classroom.(Teacher id can only be deleted if it is not incharge of any classroom and it does not teach any subject).

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [DeleteTeacherId](../ServerProgram/src/ServerProgram/ActionCode/DeleteTeacherId.java)

---

## Action Code 9

**About :** Client is requesting server to update details of a Classroom.

**Info Attribute of Message** contains old and new deatils about classroom.

**Example Message**
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

Note : if new subject is created while editing then old_subject_name  and old_subject_teacher will contain null
```

**Number of Response Message :** Only one response message will be sent.

**Info Attribute of Response Message** contains Response code indicating status of request.
* Response Code 0 : Classroom Details have been updated successfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [UpdateClassroom](../ServerProgram/src/ServerProgram/ActionCode/UpdateClassroom.java)

---

## Action Code 10

**About :** Client is requesting server to delete data of Classroom.

**Info Attribute of Message** contains details(standard,division) about classroom to be deleted.

**Example Message**
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

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains response code indicating status of request.
* Repsone Code 0 : Classroom data has been deleted successfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [DeleteClassroom](../ServerProgram/src/ServerProgram/ActionCode/DeleteClassroom.java)

---

## Action Code 11

**About :** Client is requesting server to provide a list of all Classroos with have a specific Teacher Incharge.

**Info Attribute of Message** conatins phone number of teacher.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 11,
    "info" : {
        "phone" : $phone_of_teacher
    }
}
```

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains an arrray, where each object has an stanadard and all the division with that standard.

**Example Response Message**
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

**Module processing request in Server Program :** [GetStandardDivisionOfTeacher](../ServerProgram/src/ServerProgram/ActionCode/GetStandardDivisionOfTeacher.java)

---

## Action Code 12

**About :** Client is requesting server to create new student Id.

**Info Attribute of Message** conatins all the data required to create a new student Id.Refer Example Message to see what details are provided.

**Example Message**
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

Note : If input for any of the above field is not given by user then it will contain "null"(String).Dob is of long data type,obtained from Date.getTime() method.Image contains an Base64 encoded String,It has been encoded so that we can send Image through an JSON Object.
```

**Number of Response Message :** Only one response message will be sent.

**Info Attribute of Response Message** contains sid of newly created Student Id.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "sid" : $sid;
    }
}
```

**Module processing request in Server Program :** [CreateStudentId](../ServerProgram/src/ServerProgram/ActionCode/CreateStudentId.java)

---

## Action Code 13

**About :** Client is requesting server to provide a list of all students who are enrolled in classroom with a given teacher incahrge.

**Info Attribute of Message** contains phone number of teacher.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 13,
    "info" : {
        "phone" : $phone_of_teacher_incharge
    }
}
```

**Number of Response Messages :** n+1 Response messages will be sent, where n is the number of students enrolled in classroom with given teacher incharge.

**Info Attribute of Response Message** contains number of students enrolled in classroom with given teacher incharge in first response message and for other response messages it will conatins details about individual student id.

**Example Response Message**
```
First Response message containing number of student.
{
    "id" : $messageId,
    "info" : {
        "total_students" : $no_of_students
    }
}

Other Response messsage containing details about individual Student Id.
{
    "id" : $messageId,
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
```

**Module processing request in Server Program :** [GetStudentListForClassroomIncharge](../ServerProgram/src/ServerProgram/ActionCode/GetStudentListForClassroomIncharge.java)

---

## Action Code 14

**About :** Client is requesting server to provide list of all students that are being taught by a particular teacher.

**Info Attribute of Message** contains the phone number of teacher.

**Example Message**

**Number of Response Messages :** n+1 response messages will be sent where n is the number of students that are being taught by a particular teacher.

**Info Attribute of Response Message** contains number of students that are being taught by a particular teacher in first response message and for other response messages it will conatins details about individual student id.

**Example Response Message**
```
First Response message containing number of student.
{
    "id" : $messageId,
    "info" : {
        "total_students" : $no_of_students
    }
}

Other Response messsage containing details about individual Student Id.
{
    "id" : $messageId,
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
```

**Module processing request in Server Program :** [GetStudentListForSubjectTeacher](../ServerProgram/src/ServerProgram/ActionCode/GetStudentListForSubjectTeacher.java)

---

## Action Code 15

**About :** Client os requesting server to delete Student Id.

**Info Attribute of Message** contains sid of the student id to be deleted.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 15,
    "info" : {
        "sid" : $sid_of_student
    }
}
```

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains repsonse code indicating status of request.
* Response Code 0 : Student Id has been deleted Sucessfully.

**Example Response Message**
```
{
    "id" : messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [DeleteStudentId](../ServerProgram/src/ServerProgram/ActionCode/DeleteStudentId.java)

---

## Action Code 16

**About :** Client is requesting server to update details of student id.

**Info Attribute of Message** contains all details of Student Id.Unlike action code 12 info attribute may or may not have img attribute.If it has img attribute then image has been updated or else image has not been updated.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 16,
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

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains response code indicating status of request.
* Response Code 0 : Student Id has been updated Sucessfully

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [UpdateStudentId](../ServerProgram/src/ServerProgram/ActionCode/UpdateStudentId.java)

---

## Action Code 17

**About :** Client is requesting server to provide information of all students as well as provide list of all classrooms as in Action Code 11

**Info Attribute of Message** does not exist in this action code.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 17
}
```

**Number of Response Message :** n+1 response messages will be sent, where is the number of student available in database.

**Info Attribute of Response Message** contains total number of students and classroom array in First message and all other message contains details about individual student Id.

**Example Response Message**
```
First Response message containing classroom list and number of students.
{
    "id" : $messageId,
    "info" : {
        "classroom_list" : $classroomListArray,
        "total_students" : $no_of_students
    }
}


Other Response message containing details about individual student Id.
{
    "id" : $messageId,
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
```

**Module processing request in Server Program :** [GetStudentInfoForPrincipal](../ServerProgram/src/ServerProgram/ActionCode/GetStudentInfoForPrincipal.java)

---

## Action Code 18

**About :** Client is requesting server to provide a list of all distinct exam names and a list of all subjects taught by a teacher.This action code is used while creating new exam.

**Info Attribute of Message** contains phone no of teacher.

**Example Message** 
```
{
    "id" : $messageId,
    "action_code" : 18,
    "info" : {
        "phone" : $phone_no_of_teacher
    }
}
```

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains list of all distinct exam names and list of subjects taught by teacher.

**Example Response Message**
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

**Module processing request in Server Program :** [GetExamAndSubjectList](../ServerProgram/src/ServerProgram/ActionCode/GetExamAndSubjectList.java)

---

## Action Code 18 

**About :** Client is requesting server to provide sid,name,roll no of students enrolled in a specific classroom.This data is needed to enter marks of students in exam

**Info Attribute of Message** contains standard and division of classroom whoose students details are required.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 19,
    "info" : {
        "standard" : $standard,
        "division" : $division
    }
}
```

**Number of Response Messages :** n+1 messages will be sent, where n is the number of students enrolled in that classroom.

**Info Attribute of Response Message** contains number of students in first response messsage and other response message contains data about individual student.

**Example Response Message**
```
First message contatining total number of students.
{
    "id" : $messageId,
    "info" : {
        "total_students" : $totalStudentsInClassroom
    }
}

Other message containing data about individual students
{
    "id" : $messageId,
    "info" : {
        "sid" : $sid,
        "firstname" : $firstname,
        "lastname" : $lastname,
        "roll_no" : $rollNo
    }
}
```

**Module processing request in Server Program :** [GetStudentListForExam](../ServerProgram/src/ServerProgram/ActionCode/GetStudentListForExam.java)

---

## Action Code 25

**About :** Client is requesting server to add details of new exam to database.

**Info Attribute of Message** contains details about exam and score of students in exam.Refer Example Message for more detail.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 20,
    "info" : {
        "exam_name" : $examName,
        "date" : $examDate,
        "subject_id" : $subjectId,
        "total_marks" : $totalMarks,
        "score" : [
            {
                "sid" : $sid,
                "marks" : $marks
            },
            {
                "sid" : $sid,
                "marks" : $marks
            },
            {
                "sid" : $sid,
                "marks" : $marks
            }
        ]
    }
}
```

**Number of Response Messages :** Only one message will be sent.

**Info Attribute of Response Message** contains response code indicating status of request.
* Response Code 0 : Exam details added to database Successfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [AddNewExam](../ServerProgram/src/ServerProgram/ActionCode/AddNewExam.java)

---

## Action Code 21 

**About :** Client is requesting server to provide details of all exam conducted by a specific teacher.

**Info Attribute of Message** contains phone number of teacher.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 21,
    "info" {
        "phone" : $phone_of_teacher
    }
}
```

**Number of Response Messages :** n+1 messages will be sent where n is the number of exam conducted by teacher.

**Info Attribue of Response Message** contains total number of exams conducted by teacher in first response message and for other response messages info attribute will contain details about individual exam.

**Example Response Message**
```
First Response Message containing total number of exams
{
    "id" : $messageId,
    "info" : {
        "total_exams" : $total_exams
    }
}

Other Response Messages containing details about individual exam
{
    "id" : $messageId,
    "info" : {
        "exam_id" : $examId,
        "exam_name" : $exam_name,
        "date" : $exam_date,
        "total_marks" : $total_marks,
        "subject" : {
            "subject_id" : $subjectId,
            "subject_name" : $subject_name,
            "standard" : $standard",
            "division" : $division,
            "phone" : $phone_of_teacher
        }
    }
}
```

**Module processing request in Server Program :** [GetExamListForTeacher](../ServerProgram/src/ServerProgram/ActionCode/GetExamListForTeacher.java)

---

## Action Code 22

**About :** Client is requesting server to provide scores of students in a particular exam.

**Info Attribute of Message** contains exam id of exam whoose scores are required.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 21,
    "info" : {
        "exam_id" : $exam_id
    }
}
```

**Number of Response Message :** n+1 response messages will be sent, where n is the number of students who gave the exam.

**Info Attribute of Response Message** contains total number of students who gave that exam for first response message, else for other response messages it will contain score and data of an individual student.

**Example Response Message**
```
First response message containing total number of students.
{
    "id" : $messageId,
    "info" : {
        "total_students" : $total_students
    }
}

Other Response messages containing score and details of individual student.
{
    "id" : $messageId,
    "info" : {
        "sid" : $sid
        "firstname" : $firstname,
        "lastname" : $lastname,
        "roll_no" : $roll_no,
        "score" : $score
    }
}
```

**Module processing request in Server Program :** [GetScoreOfExam](../ServerProgram/src/ServerProgram/ActionCode/GetScoreOfExam.java)

---

## Action Code 23

**About :** Client os requesting server to update details of an Exam.

**Info Attribute of Message** contains all details of Exam.Refer Example Message

**Example Message**
The Client is requesting server to update details of an exam.Info attribute of exam contains details about exam.Example is as follows
```
{
    "id" : $messageId,
    "action_code" : 23,
    "info" : {
        "exam_id" : $examId,
        "exam_name" : $examName,
        "date" : $examDate,
        "subject_id" : $subjectId,
        "total_marks" : $totalMarks,
        "score" : [
            {
                "sid" : $sid,
                "marks" : $marks
            },
            {
                "sid" : $sid,
                "marks" : $marks
            },
            {
                "sid" : $sid,
                "marks" : $marks
            }
        ]
    }
}
```

**Number of Response Messages :** Only one response message will be sent.

**Info Attribute of Response Message** contains response code indicating status of request
* Response Code 0 : Exam details have been updated successfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : $responseCode
    }
}
```

**Module processing request in Server Program :** [UpdateExam](../ServerProgram/src/ServerProgram/ActionCode/UpdateExam.java)

---

## Action Code 24

**About :** Client is requesting server to delete exam data from database.

**Info Attribute of Message** conatins exam id whoose data is to be deleted.

**Example Message**
```
{
    "id" : $messageId,
    "action_code" : 24,
    "info" : {
        "exam_id" : $exam_id
    }
}
```

**Number of Response Message :** Only one response message will be sent.

**Info Attribute of Response Message** contains response code indicating status of request.
* Response Code 0 : Exam data has been delete successfully.

**Example Response Message**
```
{
    "id" : $messageId,
    "info" : {
        "response_code" : 0
    }
}
```

**Module processing request in Server Program :** [DeleteExam](../ServerProgram/src/ServerProgram/ActionCode/DeleteExam.java)

---