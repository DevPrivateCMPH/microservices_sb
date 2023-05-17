package com.cmph.userservice.controller;

import com.cmph.userservice.entity.User;
import com.cmph.userservice.model.Bike;
import com.cmph.userservice.model.Car;
import com.cmph.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<?> getCars(@PathVariable("userId") int userId) {
        User user = userService.getUserById(userId);
        if(user != null){
            List<Car> cars = userService.getCars(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cars);
        }
        return ResponseEntity.notFound().build();
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackSaveCar")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<?> saveCar(@PathVariable("userId") int userId, @RequestBody Car car) {
        if(userService.getUserById(userId) != null) {
            Car carNew = userService.saveCar(userId, car);
            if (carNew != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(carNew);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<?> getBikes(@PathVariable("userId") int userId) {
        User user = userService.getUserById(userId);
        if(user != null){
            List<Bike> bikes = userService.getBikes(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(bikes);
        }
        return ResponseEntity.notFound().build();
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackSaveBike")
    @PostMapping("/savebike/{userId}")
    public ResponseEntity<?> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike) {
        if(userService.getUserById(userId) != null) {
            Bike bikeNew = userService.saveBike(userId, bike);
            if (bikeNew != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(bikeNew);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @CircuitBreaker(name = "allCB", fallbackMethod = "fallBackGetAllVehicles")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<?> getAllVehicles(@PathVariable("userId") int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserAndVehicles(userId));
    }

    private ResponseEntity<?> fallBackGetCars(@PathVariable("userId") int userId, RuntimeException e) {
        return ResponseEntity.status(HttpStatus.OK).body("El usuario [" + userId + "]" + " tiene los coches en el taller");
    }

    private ResponseEntity<?> fallBackSaveCar(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException e){
        return ResponseEntity.status(HttpStatus.OK).body("El usuario [" + userId + "]" + " no tiene dinero para coches");
    }

    private ResponseEntity<?> fallBackGetBikes(@PathVariable("userId") int userId, RuntimeException e) {
        return ResponseEntity.status(HttpStatus.OK).body("El usuario [" + userId + "]" + " tiene las motos en el taller");
    }

    private ResponseEntity<?> fallBackSaveBike(@PathVariable("userId") int userId, @RequestBody Bike bike, RuntimeException e){
        return ResponseEntity.status(HttpStatus.OK).body("El usuario [" + userId + "]" + " no tiene dinero para motos");
    }

    private ResponseEntity<?> fallBackGetAllVehicles(@PathVariable("userId") int userId, RuntimeException e) {
        return ResponseEntity.status(HttpStatus.OK).body("El usuario [" + userId + "]" + " tiene los vehiculos en el taller");
    }
}
