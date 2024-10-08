package com.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int id;
    private Car car;
    private User client;
    private Date orderDate;
    private OrderStatus status;
    private TypeOrder type;
    private String note;
    private User manager;

    public enum OrderStatus {
        PENDING, COMPLETED, CANCELLED
    }

    public enum TypeOrder {
        BUYING, SERVICE
    }

    public Order(int id, Car car, User client, TypeOrder type, String note) {
        setId(id);
        setCar(car);
        setClient(client);
        setOrderDate(Date.valueOf(LocalDate.now().toString()));
        setStatus(OrderStatus.PENDING);
        setType(type);
        setNote(note);
    }

    @Override
    public String toString() {
        return "Order: "+ getId() + " {" +
                "\ncar= ["+ car.getModel() + ", " + car.getBrand() + "]" +
                "\nclient= [" + client.getUsername() + ", " + client.getName() + "]" +
                "\norderDateTime=" + orderDate +
                ", status=" + status +
                ", type=" + type +
                ", note='" + note + '\'' +
                '}';
    }
}
