package com.cmph.bikeservice.controller;

import com.cmph.bikeservice.entity.Bike;
import com.cmph.bikeservice.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    BikeService bikeService;

    @GetMapping
    public ResponseEntity<?> listCar(){
        List<Bike> userList = bikeService.getAll();
        if(!userList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id){
        Bike bike = bikeService.getCarById(id);
        if(bike != null){
            return ResponseEntity.status(HttpStatus.OK).body(bike);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Bike bike){
        Bike bikeNew = bikeService.save(bike);
        if(bike != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(bikeNew);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/byuser/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable("userId") int userId){
        List<Bike> bikeList = bikeService.byUserId(userId);
        if(!bikeList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(bikeList);
        }
        return ResponseEntity.noContent().build();
    }
}
