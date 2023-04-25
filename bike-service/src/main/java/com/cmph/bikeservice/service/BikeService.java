package com.cmph.bikeservice.service;

import com.cmph.bikeservice.entity.Bike;
import com.cmph.bikeservice.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {

    @Autowired
    BikeRepository bikeRepository;

    public List<Bike> getAll(){
        return bikeRepository.findAll();
    }

    public Bike getCarById(Integer id){
        return bikeRepository.findById(id).orElse(null);
    }

    public Bike save(Bike car){
        Bike carNew = bikeRepository.save(car);
        return carNew;
    }

    public List<Bike> byUserId(int userId){
        return bikeRepository.findByUserId(userId);
    }
}
