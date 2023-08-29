package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomTypes roomType;

    @Column(name = "price")
    private Double price;

    @Column(name = "beds")
    private int beds;
    @Column(name = "capacity")
    private int capacity;

    public Room(){}

    public Room(String roomNumber, RoomTypes roomType, Double price, int beds, int capacity){
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.beds = beds;
        this.capacity = capacity;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomTypes getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypes roomType) {
        this.roomType = roomType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
