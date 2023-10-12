package com.srsapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srsapi.data.DataStore;
import com.srsapi.data.IDataStore;
import com.srsapi.model.Student;
import com.srsapi.model.jsonResponse;

@RequestMapping("student")
@RestController
public class StudentController {

    @Autowired
    private DataStore dataStore;

    @GetMapping(value = "/{id}")
    public jsonResponse getStudentById(@PathVariable("id") String id) {
        return dataStore.getStudent(id);
    }

    @GetMapping(value = "/{id}/courses")
    public jsonResponse getStudentCourses(@PathVariable("id") String id) {
        return dataStore.getStudentCourses(id);
    }

    @PostMapping(value = "/{id}/course/{courseId}/{section}")
    public jsonResponse addCourse(@PathVariable("id") String id, @PathVariable("courseId") String courseId,
            @PathVariable("section") int section) {
        return dataStore.registerCourse(id, courseId, section);
    }

    @DeleteMapping(value = "/{id}/course/{courseId}/{section}")
    public jsonResponse dropCourse(@PathVariable("id") String id, @PathVariable("courseId") String courseId,
            @PathVariable("section") int section) {
        return dataStore.dropCourse(id, courseId, section);
    }

    /*
     * documentation query this endpoint conntroller methods
     * 
     * 1- to student by id
     * request type: GET request url:
     * http://localhost:8080/student/1c6ca9cc-6232-4468-8737-e177469fac9f
     * response e.g. :
     * {"status":"success","message":"student found","data":{"firstName":"sam",
     * "lastName":"kaleen","email":"sam@example.com","uuid":
     * "1c6ca9cc-6232-4468-8737-e177469fac9f","password":"","courseList":[],"id":
     * "1c6ca9cc-6232-4468-8737-e177469fac9f"}}
     * 
     * 2- to get student courses
     * request type: GET request url:
     * http://localhost:8080/student/1c6ca9cc-6232-4468-8737-e177469fac9f/courses
     * response e.g. :
     * {"status":"success","message":"student courses","data":[{"uuid":
     * "0c6bc51d-46ca-45be-9714-077c111e0b1f","theOffering":{"uuid":"","section":1,
     * "semester":"Fall","year":2018,"theCourse":{"uuid":"1",
     * "courseName":"Introduction to Computer Science","courseNumber":"CS 101"
     * ,"courseDept":"Computer Science","preReqs":[],"offeringList":[]}},"theGrade":
     * "NA","theStatus":"registered","theCourse":{"uuid":"1",
     * "courseName":"Introduction to Computer Science","courseNumber":"CS 101"
     * ,"courseDept":"Computer Science","preReqs":[],"offeringList":[]}}]}
     * 
     * 3- to add course
     * request type: POST request url:
     * http://localhost:8080/student/1c6ca9cc-6232-4468-8737-e177469fac9f/course/1/1
     * response e.g. :{
     * "status": "success",
     * "message": "course added",
     * "data": {
     * "uuid": "00362407-08bb-4f21-9440-68231fcbefb3",
     * "theOffering": {
     * "uuid": "1",
     * "section": 1,
     * "semester": "Fall",
     * "year": 2018,
     * "theCourse": {
     * "uuid": "1",
     * "courseName": "Introduction to Computer Science",
     * "courseNumber": "CS 101",
     * "courseDept": "Computer Science",
     * "preReqs": [],
     * "offeringList": []
     * }
     * },
     * "theGrade": "NA",
     * "theStatus": "registered",
     * "theCourse": {
     * "uuid": "1",
     * "courseName": "Introduction to Computer Science",
     * "courseNumber": "CS 101",
     * "courseDept": "Computer Science",
     * "preReqs": [],
     * "offeringList": []
     * }
     * }
     * }
     * 
     * 4- to drop course
     * request type: DELETE request url: http://localhost:8080/student/1c6ca9cc-6232-4468-8737-e177469fac9f/course/1/1
     * response e.g. :{
     * "status": "success",
     * "message": "course dropped",
     * "data": null
     * }
     * 
     */

}