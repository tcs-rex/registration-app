-- Team Members:
-- Mohammed Alhajjaj
-- Stephen Thistle
-- Graeme Folk

--1) Show all tables and explain how they are related to one another (keys, triggers, etc.)
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

--2) A basic retrieval query
--3) A retrieval query with ordered results
--4) A nested retrieval query
--5) A retrieval query using joined tables
--6) An update operation with any necessary triggers
--7) A deletion operation with any necessary triggers
