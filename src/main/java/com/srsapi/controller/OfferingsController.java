package com.srsapi.controller;

import com.srsapi.data.IDataStore;
import com.srsapi.model.Course;
import com.srsapi.model.Offering;
import com.srsapi.model.jsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("offerings")
@RestController
public class OfferingsController {
    @Autowired
    private IDataStore dataStore;

    @GetMapping(value = "/{id}")
    public jsonResponse getOfferingById(@PathVariable("id") String id) {
        return dataStore.getOffering(id);
    }

    @GetMapping(value = "/")
    public jsonResponse getAllOfferings() {
        return dataStore.getAllOfferings();
    }

    @GetMapping(value = "/{id}/course")
    public jsonResponse getOfferingCourse(@PathVariable("id") String id) {
        return dataStore.getCourseOfferings(id);
    }

    // add offering
    @PostMapping(value = "/add")
    public jsonResponse addOffering(@RequestBody Offering offering) {
        return dataStore.addOffering(offering);
    }

    @PutMapping(value = "/{id}/update")
    public jsonResponse updateOffering(@RequestBody Offering offering) {
        return dataStore.updateOffering(offering);
    }

    @DeleteMapping(value = "/{id}/delete")
    public jsonResponse deleteOffering(@PathVariable("id") String id) {
        return dataStore.deleteOffering(id);
    }


    /*
     * documentation query this endpoint conntroller methods
     * 
     * 1- to get offering by id
     * request type: GET request url: http://localhost:8080/offerings/{id}
     * response e.g. :
     * 
     * 2- to get all offerings
     * request type: GET request url: http://localhost:8080/offerings/
     * response e.g. :
     * 
     * 
     * 3- to get offering course
     * request type: GET request url: http://localhost:8080/offerings/{id}/course
     * response e.g. :
     * 
     * 4- to add offering
     * request type: POST request url: http://localhost:8080/offerings/add
     * request body: {
     * "section": "1",
     * "semester": "Fall",
     * "year": 2023,
     * "theCourse":{
     * "uuid":"9"
     * 
     * }
     * }
     * 
     */

}
