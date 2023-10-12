package com.srsapi.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.mysql.cj.xdevapi.PreparableStatement;
import com.srsapi.model.*;

@Component("dataStore")
public class DataStore implements IDataStore {
    @Autowired
    private Environment env;

    private Statement getStatement() throws SQLException, ClassNotFoundException {
        Connection conn = DriverManager.getConnection(env.getProperty("db-url"), env.getProperty("db-username"),
                env.getProperty("db-password"));
        Statement stmt = conn.createStatement();
        return stmt;
    }

    private PreparedStatement getPreparedStatement(String query) throws SQLException, ClassNotFoundException {
        Connection conn = DriverManager.getConnection(env.getProperty("db-url"), env.getProperty("db-username"),
                env.getProperty("db-password"));
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt;
    }

    private boolean checkSQLInjection(String str) {
        // if string null or empty
        if (str == null || str.isEmpty()) {
            return false;
        }
        String regex = "((\\%27)|(\\'))((\\%6F)|o|(\\%4F))((\\%72)|r|(\\%52))";
        return str.matches(regex);
    }

    public jsonResponse getStudent(String uuid) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(uuid)) {
                return null;
            }

            stmt = getStatement();
            String strSelect = "select * from student where uuid = '" + uuid + "'";
            rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                String firstName = rset.getString("first_name");
                String lastName = rset.getString("last_name");
                String email = rset.getString("email");
                Student student = new Student(uuid, firstName, lastName, email, "");

                return new jsonResponse("success", "student found", student);
            }

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);

        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }

            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
        return new jsonResponse("error", "student not found", null);

    }

    private boolean ifExist(String email) {
        Statement stmt = null;
        try {
            if (checkSQLInjection(email)) {
                return false;
            }

            stmt = getStatement();
            String strSelect = "select * from student where email = '" + email + "'";
            java.sql.ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean checkPreReq(String studentId, String courseId) {
        Statement stmt = null;
        try {
            if (checkSQLInjection(studentId) || checkSQLInjection(courseId)) {
                return false;
            }

            stmt = getStatement();
            String strSelect = "select * from prerequisite where course_uuid = '" + courseId + "'";
            java.sql.ResultSet rset = stmt.executeQuery(strSelect);
            List<Boolean> regList = new ArrayList<>();
            while (rset.next()) {
                String preReqId = rset.getString("prerequisite_course_uuid");
                strSelect = "select * from registration where student_uuid = '" + studentId + "' and course_uuid = '"
                        + preReqId + "' and (status = 'registered' or status = 'completed')";
                rset = stmt.executeQuery(strSelect);
                if (rset.next()) {
                    regList.add(true);
                } else {
                    regList.add(false);
                }
            }
            if (regList.contains(false)) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public jsonResponse login(String username, String password) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;

        try {
            if (checkSQLInjection(username) || checkSQLInjection(password)) {
                return null;
            }

            stmt = getStatement();
            String strSelect = "select * from student where email = '" + username + "' and password = '" + password
                    + "'";
            rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                String uuid = rset.getString("uuid");
                return new jsonResponse("success", "student found", uuid);
            }

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
        return new jsonResponse("error", "student not found", null);

    }

    @Override
    public jsonResponse register(Student student) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;

        try {
            if (checkSQLInjection(student.getEmail()) || checkSQLInjection(student.getPassword())) {
                return null;
            }

            if (ifExist(student.getEmail())) {
                return new jsonResponse("error", "email already exist", null);
            }

            String uuid = UUID.randomUUID().toString();
            stmt = getStatement();
            String strInsert = "insert into student (uuid, first_name, last_name, email, password) values ('"
                    + uuid + "', '" + student.getFirstName() + "', '" + student.getLastName() + "', '"
                    + student.getEmail() + "', '" + student.getPassword() + "')";
            int countInserted = stmt.executeUpdate(strInsert);
            if (countInserted == 1) {
                return new jsonResponse("success", "student registered", uuid);
            }

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
        return new jsonResponse("error", "student not registered", null);
    }

    @Override
    public boolean checkEmail(String email) {

        return ifExist(email);
    }

    @Override
    public jsonResponse getStudentCourses(String uuid) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(uuid)) {
                return null;
            }

            stmt = getStatement();
            String strSelect = "select * from registration join course on registration.course_uuid = course.uuid join offering on registration.course_uuid = offering.course_uuid and registration.section_number = offering.section_number join department on course.department_uuid = department.uuid where registration.student_uuid = '"
                    + uuid + "' and (registration.status = 'registered' or registration.status = 'completed')";
            rset = stmt.executeQuery(strSelect);
            List<Registration> courses = new ArrayList<Registration>();
            while (rset.next()) {
                String courseUuid = rset.getString("course_uuid");
                String courseName = rset.getString("name");
                String courseNumber = rset.getString("number");
                String courseDepartment = rset.getString("department.name");
                String deptUuid = rset.getString("department.uuid");

                int sectionNumber = rset.getInt("section_number");
                String semester = rset.getString("semester");
                int year = rset.getInt("year");
                String offeringUuid = rset.getString("offering.uuid");
                String status = rset.getString("status");
                String grade = rset.getString("grade");
                Course course = new Course(courseUuid, courseName, courseNumber, courseDepartment, deptUuid);
                Offering offering = new Offering(offeringUuid, sectionNumber, semester, year, course, 0);
                Registration reg = new Registration(offering, grade, status);
                courses.add(reg);
            }
            while (rset.next()) {

            }
            return new jsonResponse("success", "student courses", courses);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);

        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }

    }

    @Override
    public jsonResponse registerCourse(String studentuuid, String courseuuid, int section) {

        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(studentuuid) || checkSQLInjection(courseuuid)) {
                return null;
            }

            if (!checkPreReq(studentuuid, courseuuid)) {
                return new jsonResponse("error", "prerequisite not met", null);
            }
            if (checkMaxCourses(studentuuid)) {
                return new jsonResponse("error", "cannot register more than 6 courses", null);
            }
            if (checkAlreadyRegistered(studentuuid, courseuuid, section)) {
                return new jsonResponse("error", "already registered", null);
            }

            stmt = getStatement();
            String strSelect = "select * from offering join course on offering.course_uuid = course.uuid join department on course.department_uuid = department.uuid where offering.course_uuid = '"
                    + courseuuid + "' and offering.section_number = " + section;
            rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                String offeringUuid = rset.getString("offering.uuid");
                String offeringSemester = rset.getString("semester");
                int offeringYear = rset.getInt("year");
                String courseName = rset.getString("course.name");
                String courseNumber = rset.getString("number");
                String courseDepartment = rset.getString("department.name");
                String deptUuid = rset.getString("department.uuid");
                Course course = new Course(courseuuid, courseName, courseNumber, courseDepartment, deptUuid);
                Offering offering = new Offering(offeringUuid, section, offeringSemester, offeringYear, course, 0);
                String strInsert = "insert into registration (student_uuid, course_uuid, section_number, grade, status) values ('"
                        + studentuuid + "', '" + courseuuid + "', " + section + ", '"
                        + "NA" + "', '" + "registered" + "')";
                int countInserted = stmt.executeUpdate(strInsert);
                if (countInserted == 1) {
                    int count = checkRegistrationCount(courseuuid, section);
                    offering.setCurrentEnrollment(count);
                    Registration registration = new Registration(offering, "NA", "registered");
                    if (count >= 8) {
                        return new jsonResponse("success", "course added", registration);
                    } else {
                        return new jsonResponse("success",
                                "course added, this course may not run if doesn't meet the minimum required number of students (8)",
                                registration);
                    }
                }

            } else {
                return new jsonResponse("error", "course not found", null);
            }

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
        return new jsonResponse("error", "course not added", null);
    }

    private int checkRegistrationCount(String courseuuid, int section) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            stmt = getStatement();
            String strSelect = "select count(*) from registration where course_uuid = '" + courseuuid
                    + "' and section_number = "
                    + section + " and status = 'registered' ";
            rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getInt(1);
            }
        } catch (SQLException e) {
            return 0;
        } catch (ClassNotFoundException e) {
            return 0;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return 0;
            }
        }
        return 0;
    }

    private boolean checkAlreadyRegistered(String studentuuid, String courseuuid, int section) {

        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(studentuuid) || checkSQLInjection(courseuuid)) {
                return false;
            }

            stmt = getStatement();
            String strSelect = "select * from registration where student_uuid = '" + studentuuid
                    + "' and course_uuid = '"
                    + courseuuid + "' and section_number = " + section;
            rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return false;
            }
        }
    }

    private boolean checkMaxCourses(String studentuuid) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(studentuuid)) {
                return false;
            }

            stmt = getStatement();
            String strSelect = "select * from registration where student_uuid = '" + studentuuid + "' and status = 'registered'";
            rset = stmt.executeQuery(strSelect);
            int count = 0;
            while (rset.next()) {
                count++;
            }
            if (count >= 6) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return false;
            }
        }

    }

    @Override
    public jsonResponse dropCourse(String studentuuid, String courseuuid, int section) {

        Statement stmt = null;
        java.sql.ResultSet rset = null;

        try {
            if (checkSQLInjection(studentuuid) || checkSQLInjection(courseuuid)) {
                return null;
            }
            stmt = getStatement();
            String strDelete = "delete from registration where student_uuid = '" + studentuuid
                    + "' and course_uuid = '"
                    + courseuuid + "' and section_number = " + section;
            int countDeleted = stmt.executeUpdate(strDelete);
            if (countDeleted > 0) {
                return new jsonResponse("success", "course dropped", null);
            }

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
        return new jsonResponse("error", "course does not exist", null);
    }

    @Override
    public jsonResponse getCourse(String uuid) {

        Statement stmt = null;
        java.sql.ResultSet rset = null;

        try {
            if (checkSQLInjection(uuid)) {
                return null;
            }

            stmt = getStatement();
            String strSelect = "select * from course join department on course.department_uuid = department.uuid where course.uuid = '"
                    + uuid + "'";
            rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                String courseName = rset.getString("name");
                String courseNumber = rset.getString("number");
                String courseDepartment = rset.getString("department.name");
                String deptUuid = rset.getString("department.uuid");
                Course course = new Course(uuid, courseName, courseNumber, courseDepartment, deptUuid);
                rset.close();
                // get prereqs and join with course table to get course name, number, department
                strSelect = "select * from prerequisite join course on prerequisite.prerequisite_course_uuid = course.uuid join department on course.department_uuid = department.uuid where prerequisite.course_uuid = '"
                        + uuid + "'";
                rset = stmt.executeQuery(strSelect);
                ArrayList<Course> prereqs = new ArrayList<Course>();
                while (rset.next()) {
                    String prereqUuid = rset.getString("prerequisite_course_uuid");
                    String prereqName = rset.getString("name");
                    String prereqNumber = rset.getString("number");
                    String prereqDepartment = rset.getString("department.name");
                    String prereqDeptUuid = rset.getString("department.uuid");
                    Course prereq = new Course(prereqUuid, prereqName, prereqNumber, prereqDepartment, prereqDeptUuid);
                    prereqs.add(prereq);
                }
                course.setPreReqs(prereqs);
                return new jsonResponse("success", "course", course);
            }

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
        return new jsonResponse("error", "course not found", null);
    }

    @Override
    public jsonResponse getAllCourses() {

        Statement stmt = null;
        java.sql.ResultSet rset = null;

        try {
            stmt = getStatement();
            String strSelect = "select * from course join department on course.department_uuid = department.uuid order by course.name, course.number";
            rset = stmt.executeQuery(strSelect);
            List<Course> courses = new ArrayList<Course>();
            while (rset.next()) {
                String courseName = rset.getString("name");
                String courseNumber = rset.getString("number");
                String courseDepartment = rset.getString("department.name");
                String courseUuid = rset.getString("course.uuid");
                String deptUuid = rset.getString("department.uuid");
                Course course = new Course(courseUuid, courseName, courseNumber, courseDepartment, deptUuid);
                courses.add(course);
            }
            return new jsonResponse("success", "courses found", courses);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }

    }

    @Override
    public jsonResponse getCoursePreReqs(String courseuuid) {
        java.sql.ResultSet rset = null;
        Statement stmt = null;
        try {
            if (checkSQLInjection(courseuuid)) {
                return null;
            }
            stmt = getStatement();
            String strSelect = "select * from prerequisite join course on prerequisite.prerequisite_course_uuid = course.uuid join department on course.department_uuid = department.uuid where prerequisite.course_uuid = '"
                    + courseuuid + "'";
            rset = stmt.executeQuery(strSelect);
            List<Course> prereqs = new ArrayList<Course>();
            while (rset.next()) {
                String prereqUuid = rset.getString("prerequisite_course_uuid");
                String prereqName = rset.getString("name");
                String prereqNumber = rset.getString("number");
                String prereqDepartment = rset.getString("department.name");
                String prereqDeptUuid = rset.getString("department.uuid");
                Course prereq = new Course(prereqUuid, prereqName, prereqNumber, prereqDepartment, prereqDeptUuid);
                prereqs.add(prereq);

            }
            return new jsonResponse("success", "prereqs found", prereqs);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }

    }

    @Override
    public jsonResponse getCourseOfferings(String courseuuid) {

        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(courseuuid)) {
                return null;
            }
            stmt = getStatement();
            String strSelect = "select offering.uuid,course_uuid, course.name, number, department.name, section_number, semester, year,(select count(*) from registration where registration.section_number =offering.section_number and registration.course_uuid = offering.course_uuid) as currentEnrollment from offering join course on offering.course_uuid = course.uuid join department on course.department_uuid = department.uuid where offering.course_uuid = '"
                    + courseuuid + "'";
            rset = stmt.executeQuery(strSelect);
            List<Offering> offerings = new ArrayList<Offering>();
            while (rset.next()) {
                Course course = new Course(rset.getString("course_uuid"), rset.getString("name"),
                        rset.getString("number"), rset.getString("department.name"), null);
                Offering offering = new Offering(rset.getString("uuid"), rset.getInt("section_number"),
                        rset.getString("semester"), rset.getInt("year"), course, rset.getInt("currentEnrollment"));
                offerings.add(offering);
            }
            return new jsonResponse("success", "offerings found", offerings);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse searchCourse(String query) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(query)) {
                return null;
            }
            stmt = getStatement();
            String strSelect = "select * from course join department on course.department_uuid = department.uuid  where course.name like '%"
                    + query + "%' or number like '%" + query
                    + "%' or department.name like '%" + query + "%' order by course.name, course.number";
            rset = stmt.executeQuery(strSelect);
            List<Course> courses = new ArrayList<Course>();
            while (rset.next()) {
                String courseName = rset.getString("course.name");
                String courseNumber = rset.getString("number");
                String courseDepartment = rset.getString("department.name");
                String courseUuid = rset.getString("course.uuid");
                String deptUuid = rset.getString("department.uuid");
                Course course = new Course(courseUuid, courseName, courseNumber, courseDepartment, deptUuid);
                courses.add(course);
            }
            return new jsonResponse("success", "courses found", courses);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse addCourse(Course course) {

        Statement stmt = null;
        try {
            if (checkSQLInjection(course.getUuid()) || checkSQLInjection(course.getCourseName())
                    || checkSQLInjection(course.getCourseNumber()) || checkSQLInjection(course.getCourseDept())) {
                return null;
            }
            stmt = getStatement();
            course.setUuid(UUID.randomUUID().toString());
            String strSelect = "insert into course (uuid,name,number,department_uuid) values ('"
                    + course.getUuid() + "','" + course.getCourseName() + "','" + course.getCourseNumber() + "','"
                    + course.getDeptUuid() + "')";
            stmt.executeUpdate(strSelect);
            strSelect = "insert into prerequisite (uuid,course_uuid,prerequisite_course_uuid) values ";
            for (Course prereq : course.getPreReqs()) {
                strSelect += "('" + UUID.randomUUID().toString() + "','" + course.getUuid() + "','"
                        + prereq.getUuid() + "'),";

            }
            strSelect = strSelect.substring(0, strSelect.length() - 1);
            stmt.executeUpdate(strSelect);

            return new jsonResponse("success", "course added", null);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse addOffering(Offering offering) {

        Statement stmt = null;
        try {
            if (checkSQLInjection(offering.getTheCourse().getUuid()) || checkSQLInjection(offering.getSemester())) {
                return null;
            }
            stmt = getStatement();
            offering.setUuid(UUID.randomUUID().toString());
            String strSelect = "insert into offering (uuid,course_uuid,section_number,semester,year) values ('"
                    + offering.getUuid() + "','" + offering.getTheCourse().getUuid() + "',"
                    + offering.getSection()
                    + ",'" + offering.getSemester() + "'," + offering.getYear() + ")";
            stmt.executeUpdate(strSelect);
            return new jsonResponse("success", "offering added", null);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse updateCourse(Course course) {

        PreparedStatement stmt = null;
        try {
            if (checkSQLInjection(course.getUuid()) || checkSQLInjection(course.getCourseName())
                    || checkSQLInjection(course.getCourseNumber()) || checkSQLInjection(course.getCourseDept())) {
                return null;
            }

            String strSelect = "update course set name = '" + course.getCourseName() + "', number = '"
                    + course.getCourseNumber() + "' where uuid = '" + course.getUuid() + "'";
            stmt = getPreparedStatement(strSelect);
            stmt.executeUpdate();
            return new jsonResponse("success", "course updated", null);
        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse updateOffering(Offering offering) {
        PreparedStatement stmt = null;
        try {
            if (checkSQLInjection(offering.getTheCourse().getUuid()) || checkSQLInjection(offering.getSemester())) {
                return null;
            }
            String strSelect = "update offering set section_number = " + offering.getSection() + ", semester = '"
                    + offering.getSemester()
                    + "', year = " + offering.getYear() + " where uuid = '" + offering.getUuid() + "'";
            stmt = getPreparedStatement(strSelect);
            stmt.executeUpdate();
            return new jsonResponse("success", "offering updated", null);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse deleteCourse(String uuid) {
        Statement stmt = null;
        try {
            if (checkSQLInjection(uuid)) {
                return null;
            }
            stmt = getStatement();
            // delete the course prerequisites first
            String strSelect = "delete from prerequisite where course_uuid = '" + uuid
                    + "' or prerequisite_course_uuid = '"
                    + uuid + "'";
            stmt.executeUpdate(strSelect);
            // delete the course
            strSelect = "delete from course where uuid = '" + uuid + "'";
            int result = stmt.executeUpdate(strSelect);
            if (result == 0) {
                return new jsonResponse("error", "course not found", null);
            }
            return new jsonResponse("success", "course deleted", null);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse deleteOffering(String uuid) {
        Statement stmt = null;
        try {
            if (checkSQLInjection(uuid)) {
                return null;
            }
            stmt = getStatement();
            String strSelect = "delete from offering where uuid = '" + uuid + "'";
            int result = stmt.executeUpdate(strSelect);
            if (result == 0) {
                return new jsonResponse("error", "offering not found", null);
            }
            return new jsonResponse("success", "offering deleted", null);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse getOffering(String id) {
        Statement stmt = null;
        java.sql.ResultSet rset = null;
        try {
            if (checkSQLInjection(id)) {
                return null;
            }
            stmt = getStatement();
            String strSelect = "select * from offering join course on offering.course_uuid = course.uuid where offering.uuid = '"
                    + id + "'";
            rset = stmt.executeQuery(strSelect);
            Offering offering = new Offering();
            while (rset.next()) {
                offering.setUuid(rset.getString("uuid"));
                offering.setSection(rset.getInt("section_number"));
                offering.setSemester(rset.getString("semester"));
                offering.setYear(rset.getInt("year"));
                Course course = new Course();
                course.setUuid(rset.getString("course.uuid"));
                course.setCourseName(rset.getString("course.name"));
                course.setCourseNumber(rset.getString("course.number"));
                course.setCourseDept(rset.getString("department_uuid"));
                offering.setTheCourse(course);

            }
            return new jsonResponse("success", "offering found", offering);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

    @Override
    public jsonResponse getAllOfferings() {
        Statement stmt = null;
        java.sql.ResultSet rset = null;

        try {
            stmt = getStatement();
            String strSelect = "select * from offering join course on offering.course_uuid = course.uuid";
            rset = stmt.executeQuery(strSelect);
            ArrayList<Offering> offerings = new ArrayList<Offering>();
            while (rset.next()) {
                Offering offering = new Offering();
                offering.setUuid(rset.getString("uuid"));
                offering.setSection(rset.getInt("section_number"));
                offering.setSemester(rset.getString("semester"));
                offering.setYear(rset.getInt("year"));
                Course course = new Course();
                course.setUuid(rset.getString("course.uuid"));
                course.setCourseName(rset.getString("course.name"));
                course.setCourseNumber(rset.getString("course.number"));
                offering.setTheCourse(course);
                offerings.add(offering);
            }
            return new jsonResponse("success", "offerings found", offerings);

        } catch (SQLException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new jsonResponse("error", e.getMessage(), null);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                return new jsonResponse("error", e.getMessage(), null);
            }
        }
    }

}
