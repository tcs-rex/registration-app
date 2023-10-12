package com.srsapi.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.srsapi.data.DataStore;
import com.srsapi.model.Student;
import com.srsapi.model.jsonResponse;

@RequestMapping("register")
@RestController
public class RegisterController {

    @Autowired
    private DataStore dataStore;

    @PostMapping
    public jsonResponse register(@RequestBody Student student) {
        return dataStore.register(student);
    }

    
}

/*
 * documentation query this endpoint conntroller methods
 * 
 * 1- to register student
 * request type: POST request url: http://localhost:8080/register request body: { "email":"email@example.com", "password":"password", "firstName":"firstName", "lastName":"lastName" }
 * response e.g. : {"status":"success","message":"student registered","data":1c6ca9cc-6232-4468-8737-e177469fac9f"}
 *
 */