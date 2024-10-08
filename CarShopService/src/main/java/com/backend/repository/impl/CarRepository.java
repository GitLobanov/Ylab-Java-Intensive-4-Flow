package com.backend.repository.impl;

import com.backend.model.Car;
import com.backend.model.User;
import com.backend.repository.abstracts.BaseRepository;
import com.backend.util.db.SQLRequest;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class CarRepository extends BaseRepository<Car> {

    @Override
    public boolean save(Car car) {
        String sql = SQLRequest.INSERT_ALL_INTO_CAR;
        return execute(sql, car.getBrand(), car.getModel(), car.getYear(),
                car.getPrice(), car.getCondition(), car.getColor(), car.isAvailability());
    }

    @Override
    public boolean update(Car car) {
        String sql = "UPDATE main.car SET brand = ?, model = ?, year = ?, price = ?," +
                " condition = ?, color = ?, availability = ? WHERE id = ?";
        return execute(sql, car.getBrand(), car.getModel(), car.getYear(), car.getPrice(),
                car.getCondition(), car.getColor(), car.isAvailability(), car.getId());
    }

    @Override
    public boolean delete(Car car) {
        String sql = SQLRequest.DELETE_CAR_BY_ID;
        return execute(sql, car.getId());
    }

    @Override
    public Car findById(int id) {
        String sql = SQLRequest.SELECT_CAR_BY_ID;
        return findById(sql, id);
    }

    @Override
    public List<Car> findAll() {
        String sql = "SELECT * FROM main.car";
        return findAll(sql);
    }


    public List<Car> findAllAvailableCars() {
        String sql = "SELECT * FROM main.car WHERE availability = true";
        return findAll(sql);
    }

    public List<Car> findCarsByClient(String username) {
        String sql = SQLRequest.SELECT_CAR_BY_CLIENT;
        return findBy(sql, username);
    }



    @Override
    protected Car mapRowToEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String brand = rs.getString("brand");
        String model = rs.getString("model");
        int year = rs.getInt("year");
        double price = rs.getDouble("price");
        String condition = rs.getString("condition");
        String color = rs.getString("color");
        boolean availability = rs.getBoolean("availability");
        return new Car(id, brand, model, year, price, condition, color, availability);
    }
}
