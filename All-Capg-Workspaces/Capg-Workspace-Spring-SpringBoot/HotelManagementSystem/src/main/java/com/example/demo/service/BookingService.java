package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Booking;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    Booking updateBooking(Long id, Booking booking);

    void cancelBooking(Long id);
}