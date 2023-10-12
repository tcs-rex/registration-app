-- Team Members:
-- Mohammed Alhajjaj
-- Stephen Thistle
-- Graeme Folk

# config
SET SQL_SAFE_UPDATES = 0;

-- 1) Show all tables and explain how they are related to one another (keys, triggers, etc.)
/*
The relations between the tables can be seen more clearly in the EER and schema diagrams. These relations
have been enforced using FOREIGN KEY constrains in the table creation script.
There are no specific triggers in this database.
NOT NULL constraints have been placed on all table values, and UNIQUE constraints have been placed on email addresses.
Primary keys for msot tables are a UUID.
*/

describe student;
describe course;
describe registration;
describe prerequisite;
describe offering;
describe department;
describe admin;

-- 2) A basic retrieval query
select * from student;

-- 3) A retrieval query with ordered results
select * from course order by name;

-- 4) A nested retrieval query
select * from course 
where number in 
(select number from course where number like 'E%');

-- 5) A retrieval query using joined tables
select * from offering
join course on offering.course_uuid = course.uuid 
join department on course.department_uuid = department.uuid 
where department.name = 'Software Engineering';

-- 6) An update operation with any necessary triggers
-- before
select * from student where uuid = 1;

-- update
update student set email = 'jdoe@hotmail.com', password = '321' 
where uuid = 1;

-- after
select * from student where uuid = 1;

-- 7) A deletion operation with any necessary triggers
-- before
select * from registration;

-- update
delete from registration 
where student_uuid = 2 
and course_uuid = 2 
and section_number = 1;

-- after
select * from registration;
