## MySQL Setup

### About
This file contains all the queries that must be executed in MySQL to setup the database for proper functioning of the server program.

1. Login into root account of MySQl to create a new Database, New MySQL Account and grant all permission to newly created account on newly created database.Execute the commands given below;
```
CREATE DATABASE school_app;

CREATE USER 'school_user' IDENTIFIED BY 'school_pass';

GRANT ALL PRIVILEGES ON school_app.* TO 'school_user';

FLUSH PRIVILEGES;
```

1. Login into MySQl using newly created Account and then execute the command given below to define database schema.
```
USE school_app;

CREATE TABLE parent (
		phone BIGINT PRIMARY KEY,
		firstname VARCHAR(50),
		lastname VARCHAR(50),
		password VARCHAR(256),
		email VARCHAR(50),
		gender VARCHAR(6),
		CHECK(gender IN('Male','Female','Other'))
		);

CREATE TABLE student (
	sid INT PRIMARY KEY AUTO_INCREMENT,
	firstname VARCHAR(50),
	lastname VARCHAR(50),
	gender VARCHAR(6),
	dob DATE,
	email VARCHAR(50),
	phone BIGINT,
	roll_no INT,
	standard INT,
	division VARCHAR(1),
	CHECK(gender IN('Male','Female','Other'))
	);

CREATE TABLE parent_child(
	phone BIGINT,
	sid INT,
	PRIMARY KEY(phone,sid),
	FOREIGN KEY(phone) REFERENCES parent(phone) ON DELETE CASCADE,
	FOREIGN KEY(sid) REFERENCES student(sid) ON DELETE CASCADE
	);

CREATE TABLE classroom (
	standard INT,
	division VARCHAR(1),
	t_phone BIGINT,
	PRIMARY KEY(standard,division)
	);

ALTER TABLE student
ADD FOREIGN KEY(standard,division) REFERENCES classroom(standard,division) ON DELETE CASCADE;

CREATE TABLE subject (
	sub_id INT PRIMARY KEY AUTO_INCREMENT,
	subject_name VARCHAR(50),
	standard INT,
	division VARCHAR(1),
	t_phone BIGINT,
	FOREIGN KEY(standard,division) REFERENCES classroom(standard,division) ON DELETE CASCADE
);

CREATE TABLE exam (
	exam_id INT PRIMARY KEY AUTO_INCREMENT,
	exam_name VARCHAR(100),
	exam_date DATE,
	sub_id INT,
	total_marks INT,
	FOREIGN KEY(sub_id) REFERENCES subject(sub_id) ON DELETE CASCADE
	);

CREATE TABLE score (
	sid INT,
	exam_id INT,
	marks_obtained INT,
	PRIMARY KEY(sid,exam_id),
	FOREIGN KEY(sid) REFERENCES student(sid) ON DELETE CASCADE,
	FOREIGN KEY(exam_id) REFERENCES exam(exam_id) ON DELETE CASCADE
	);

CREATE TABLE teacher (
	t_phone BIGINT PRIMARY KEY,
	firstname VARCHAR(50),
	lastname VARCHAR(50),
	email VARCHAR(50),
	gender VARCHAR(6),
	password VARCHAR(256),
	CHECK(gender IN('Male','Female','Other'))
	);

ALTER TABLE classroom
ADD FOREIGN KEY(t_phone) REFERENCES teacher(t_phone) ON DELETE SET NULL;

ALTER TABLE subject
ADD FOREIGN KEY(t_phone) REFERENCES teacher(t_phone) ON DELETE SET NULL;
```

After these commands are executed successfully the mysql is setup and ready for the program.

Read more about password field of parent entity in refer Authentication section of [Parent Android App](../ParentAndroidApp/README.md)

NOTE : Images for students will be stored in a directory named "Student Images" and filename for each image would be same as "SID" of student.