package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Room;

public interface RoomService {

    Room saveRoom(Room room);

    List<Room> getAllRooms();

    Room getRoomById(Long id);

    Room updateRoom(Long id, Room room);

    void deleteRoom(Long id);
}