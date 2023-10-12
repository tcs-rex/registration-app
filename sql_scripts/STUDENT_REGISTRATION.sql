-- Team Members:
-- Mohammed Alhajjaj
-- Stephen Thistle
-- Graeme Folk

DROP DATABASE IF EXISTS studentregistration;
CREATE DATABASE studentregistration; 
USE studentregistration;

drop table if exists student;
drop table if exists course;
drop table if exists registration;
drop table if exists prerequisite;
drop table if exists offering;
drop table if exists department;
drop table if exists admin;

create table if not exists student (
    uuid varchar(36) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null unique,
    email varchar(255) not null,
    password varchar(255) not null,
    primary key (uuid)
);

create table if not exists course (
    uuid varchar(36) not null,
    name varchar(255) not null,
    number varchar(255) not null,
    department_uuid varchar(36) not null,
    foreign key (department_uuid) references department(uuid),
    primary key (uuid)
);

create table if not exists registration (
    student_uuid varchar(36) not null,
    course_uuid varchar(36) not null,
    section_number int not null,
    grade varchar(255),
    status varchar(255) not null,
    primary key (student_uuid, course_uuid, section_number),
    foreign key (student_uuid) references student (uuid),
    foreign key (course_uuid) references course (uuid)
);

create table if not exists prerequisite (
    uuid varchar(36) not null,
    course_uuid varchar(36) not null,
    prerequisite_course_uuid varchar(36) not null,
    primary key (uuid),
    foreign key (course_uuid) references course (uuid),
    foreign key (prerequisite_course_uuid) references course (uuid)
);

create table if not exists offering (
    uuid varchar(36) not null,
    course_uuid varchar(36) not null,
    section_number int not null,
    year int not null,
    semester varchar(255) not null,
    primary key (uuid),
    foreign key (course_uuid) references course (uuid)
);


create table if not exists department (
    uuid varchar(36) not null,
    name varchar(255) not null,
    primary key (uuid)
);

create table if not exists admin (
    uuid varchar(36) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    department_uuid varchar(36) not null,
    primary key (uuid),
    foreign key (department_uuid) references department (uuid)
);


insert into department (uuid, name) values ('1', 'Computer Science');
insert into department (uuid, name) values ('2', 'Software Engineering');
insert into department (uuid, name) values ('3', 'Information Systems');

insert into student (uuid, first_name, last_name, email,password) values ('1', 'John', 'Doe', 'John@gmail.com','123');
insert into student (uuid, first_name, last_name, email,password) values ('2', 'Jane', 'Doe', 'jane@gmail.com','123');
insert into student (uuid, first_name, last_name, email,password) values ('3', 'John', 'Smith', 'Smith@gmail.com','123');
insert into student (uuid, first_name, last_name, email,password) values ('4', 'Jane', 'Smith', 'JaneSmith@gmail.com','123');
insert into student (uuid, first_name, last_name, email,password) values ('5', 'same', 'Doe', 'sua@gmail.com','123');

insert into admin (uuid, first_name, last_name, email, password, department_uuid) values ('1', 'sua', 'lucak', 'sua@gmail.com', '123', '1');
insert into admin (uuid, first_name, last_name, email, password, department_uuid) values ('2', 'alli', 'down', 'alli@gmail.com', '123', '2');
insert into admin (uuid, first_name, last_name, email, password, department_uuid) values ('3', 'sally', 'lucak', 'alsallyli@gmail.com', '123', '3');

insert into course (uuid, name, number, department_uuid) values ('1', 'Intro to Computer Science', 'CS 101', '1');
insert into course (uuid, name, number, department_uuid) values ('2', 'Intro to Software Engineering', 'SE 101', '2');
insert into course (uuid, name, number, department_uuid) values ('3', 'software Development for software engineers I', 'ENSF 592', '2');
insert into course (uuid, name, number, department_uuid) values ('4', 'software Development for software engineers II', 'ENSF 593', '2');
insert into course (uuid, name, number, department_uuid) values ('5', 'Intro to Information Systems', 'IS 101', '3');
insert into course (uuid, name, number, department_uuid) values ('6', 'Intro to Database Systems', 'CS 304', '1');
insert into course (uuid, name, number, department_uuid) values ('7', 'Intro to Algorithms', 'CS 301', '1');

insert into prerequisite (uuid, course_uuid, prerequisite_course_uuid) values ('1', '3', '2');
insert into prerequisite (uuid, course_uuid, prerequisite_course_uuid) values ('2', '4', '3');
insert into prerequisite (uuid, course_uuid, prerequisite_course_uuid) values ('3', '6', '1');
insert into prerequisite (uuid, course_uuid, prerequisite_course_uuid) values ('4', '7', '1');

insert into offering (uuid, course_uuid, section_number, year, semester) values ('1', '1', 1, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('2', '1', 2, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('3', '1', 3, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('4', '2', 1, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('5', '2', 2, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('6', '2', 3, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('7', '3', 1, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('8', '3', 2, 2020, 'Fall');
insert into offering (uuid, course_uuid, section_number, year, semester) values ('9', '3', 3, 2020, 'Fall');

insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '1', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '2', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '3', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '4', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '5', 1, 'N/A', 'registered');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '6', 1, 'N/A', 'registered');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('1', '7', 1, 'N/A', 'registered');

insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '1', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '2', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '3', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '4', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '5', 1, 'N/A', 'registered');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '6', 1, 'N/A', 'registered');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('2', '7', 1, 'N/A', 'registered');

insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '1', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '2', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '3', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '4', 1, 'A', 'completed');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '5', 1, 'N/A', 'registered');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '6', 1, 'N/A', 'registered');
insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('3', '7', 1, 'N/A', 'registered');