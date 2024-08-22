package com.backend.service;

import com.backend.annotations.Loggable;
import com.backend.model.ActionLog;
import com.backend.model.Car;
import com.backend.model.User;
import com.backend.repository.impl.CarRepository;
import com.backend.repository.impl.OrderRepository;
import com.backend.repository.impl.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Loggable
@Service
public class ClientService {

    Scanner scanner = new Scanner(System.in);

    ActionLogService actionLogService;
    OrderRepository orderRepository;
    CarRepository carRepository;
    UserRepository userRepository;

    OrderService orderService;

    public ClientService() {
        actionLogService = new ActionLogService();
        orderRepository = new OrderRepository();
        userRepository = new UserRepository();
        carRepository = new CarRepository();

        orderService = new OrderService();
    }

    public boolean addClient(User client) {
        client.setRole(User.Role.CLIENT);
        return userRepository.save(client);
    }


    public boolean updateClient(String userName, User updatedClient) {
        actionLogService.logAction(ActionLog.ActionType.UPDATE, "Update order");

        if (userRepository.findByUserName(userName)==null) {
            return false;
        } else {
            userRepository.update(updatedClient);
            return true;
        }
    }


    public boolean removeClient(String userName) {
        actionLogService.logAction(ActionLog.ActionType.DELETE, "Delete client");

        User client = userRepository.findByUserName(userName);
        return userRepository.delete(client);
    }


    public List<Car> getClientCars(String username) {
        actionLogService.logAction(ActionLog.ActionType.VIEW, "View own cars");
        return carRepository.findCarsByClient(username);
    }

    public Optional<User> getClientByUsername (String username){
        return userRepository.findByUserName(username) != null
                ? Optional.of(userRepository.findByUserName(username)) : Optional.empty();
    }

    public List<User> getAllClients () {
        actionLogService.logAction(ActionLog.ActionType.VIEW, "View all clients");
        return userRepository.findUsersByRole(User.Role.CLIENT);
    }

    public List<User> getClientsBySearch(User user) {
        actionLogService.logAction(ActionLog.ActionType.VIEW, "Search clients");
        user.setRole(User.Role.CLIENT);
        return userRepository.search(user);
    }

}
