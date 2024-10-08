package com.backend.controller;

import com.backend.dto.CarDTO;
import com.backend.loggerstarter.annotation.EnableAudit;
import com.backend.service.CarService;
import com.backend.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final ObjectMapper objectMapper;
    private final CarService carService;
    private final ClientService clientService;

    @Autowired
    public CarController(CarService carService, ClientService clientService) {
        this.carService = carService;
        this.clientService = clientService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @EnableAudit(actionType = "Get", description = "Retrieve all cars")
    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @EnableAudit(actionType = "Add", description = "Add a new car")
    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody CarDTO carDTO) {
        carService.addCar(carDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @EnableAudit(actionType = "Delete", description = "Delete car")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable int id) {
        if (carService.deleteCar(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @EnableAudit(actionType = "Update", description = "Update car")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCar(@PathVariable int id, @RequestBody CarDTO carDTO) {
        if (carService.updateCar(carDTO)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}