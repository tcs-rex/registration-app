package com.srsapi.data;

import org.springframework.boot.autoconfigure.cache.CacheProperties.Couchbase;

import com.srsapi.model.Course;
import com.srsapi.model.Offering;
import com.srsapi.model.Student;
import com.srsapi.model.jsonResponse;

public interface IDataStore {
    jsonResponse getStudent(String uuid);

    jsonResponse login(String username, String password);

    jsonResponse register(Student student);

    boolean checkEmail(String email);

    jsonResponse getStudentCourses(String uuid);

    jsonResponse registerCourse(String studentuuid, String courseuuid, int section);

    jsonResponse dropCourse(String studentuuid, String courseuuid, int section);

    jsonResponse addCourse(Course course);

    jsonResponse addOffering(Offering offering);

    jsonResponse updateCourse(Course course);
    
    jsonResponse updateOffering(Offering offering);

    jsonResponse deleteCourse(String uuid);

    jsonResponse deleteOffering(String uuid);

    jsonResponse getCourse(String courseCode);

    jsonResponse getAllCourses();

    jsonResponse getCoursePreReqs(String courseuuid);

    jsonResponse getCourseOfferings(String courseuuid);

    jsonResponse searchCourse(String query);

    jsonResponse getOffering(String id);

    jsonResponse getAllOfferings();

}
