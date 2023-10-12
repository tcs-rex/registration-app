package com.srsapi.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.srsapi.data.DataStore;
import com.srsapi.model.Student;
import com.srsapi.model.jsonResponse;

@RequestMapping("login")
@RestController
public class LoginController {
    
        @Autowired
        private DataStore dataStore;
    
        @PostMapping
        public jsonResponse login(@RequestBody Student login) {
            return dataStore.login(login.getEmail(), login.getPassword());
        }
    
}


/*
 * documentation query this endpoint conntroller methods
 * 
 * 1- to login student by email and password
 * request type: POST request url: http://localhost:8080/login request body: { "email":"email@example.com", "password":"password" }
 * response e.g. : {"status":"success","message":"student found","data":1c6ca9cc-6232-4468-8737-e177469fac9f"}
 */
 