package com.cmph.userservice.controller;

import com.cmph.userservice.entity.User;
import com.cmph.userservice.model.Bike;
import com.cmph.userservice.model.Car;
import com.cmph.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> listUser(){
        List<User> userList = userService.getAll();
        if(!userList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id){
        User user = userService.getUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user){
        User userNew = userService.save(user);
        if(user != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(userNew);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/cars/{userId}")
    public ResponseEntity<?> getCars(@PathVariable("userId") int userId){
        User user = userService.getUserById(userId);
        if(user != null){
            List<Car> cars = userService.getCars(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cars);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<?> getBikes(@PathVariable("userId") int userId){
        User user = userService.getUserById(userId);
        if(user != null){
            List<Bike> bikes = userService.getBikes(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(bikes);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/savecar/{userId}")
    public ResponseEntity<?> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
        if(userService.getUserById(userId) != null) {
            Car carNew = userService.saveCar(userId, car);
            if (carNew != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(carNew);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/savebike/{userId}")
    public ResponseEntity<?> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        if(userService.getUserById(userId) != null) {
            Bike bikeNew = userService.saveBike(userId, bike);
            if (bikeNew != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(bikeNew);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<?> getAllVehicles(@PathVariable("userId") int userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserAndVehicles(userId));
    }
}
