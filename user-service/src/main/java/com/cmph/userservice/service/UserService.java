package com.cmph.userservice.service;

import com.cmph.userservice.entity.User;
import com.cmph.userservice.feignclients.BikeFeignClient;
import com.cmph.userservice.feignclients.CarFeignClient;
import com.cmph.userservice.model.Bike;
import com.cmph.userservice.model.Car;
import com.cmph.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user){
        User userNew = userRepository.save(user);
        return userNew;
    }

    public List<Car> getCars(int userId){
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/car/byuser/" + userId, List.class);
        return cars;
    }

    public List<Bike> getBikes(int userId){
        List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/bike/byuser/" + userId, List.class);
        return bikes;
    }

    public Car saveCar(int userId, Car car){
        car.setUserId(userId);
        Car carNew = carFeignClient.save(car);
        return carNew;
    }

    public Bike saveBike(int userId, Bike bike){
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }

    public Map<String, Object> getUserAndVehicles(int userId){
        Map<String, Object> result = new HashMap<>();
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            result.put("User", user);
            List<Car> cars = carFeignClient.getCars(userId);
            if(!cars.isEmpty())
                result.put("Cars", cars);
            else
                result.put("Cars", "User has not cars");
            List<Bike> bikes = bikeFeignClient.getBikes(userId);
            if(!bikes.isEmpty())
                result.put("Bikes", bikes);
            else
                result.put("Bikes", "User has not bikes");
        } else
            result.put("User", "Not exists");
        return result;
    }
}
