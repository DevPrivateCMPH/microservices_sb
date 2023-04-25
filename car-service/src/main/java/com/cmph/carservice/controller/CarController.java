package com.cmph.carservice.controller;

import com.cmph.carservice.entity.Car;
import com.cmph.carservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    CarService carService;

    @GetMapping
    public ResponseEntity<?> listCar(){
        List<Car> userList = carService.getAll();
        if(!userList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id){
        Car car = carService.getCarById(id);
        if(car != null){
            return ResponseEntity.status(HttpStatus.OK).body(car);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Car car){
        Car carNew = carService.save(car);
        if(car != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(carNew);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/byuser/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable("userId") int userId){
        List<Car> carList = carService.byUserId(userId);
        if(!carList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(carList);
        }
        return ResponseEntity.noContent().build();
    }
}
