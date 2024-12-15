package com.system.hotel.booking.repository;

import com.system.hotel.booking.entity.Booking;
import com.system.hotel.booking.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

 /*   @Test
    public void testFindBookingsByCheckInAndCheckOutDates() {
        // Arrange
        Booking booking1 = createBooking(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 10), true);
        Booking booking2 = createBooking(LocalDate.of(2024, 12, 5), LocalDate.of(2024, 12, 15), true);
        Booking booking3 = createBooking(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), true);

        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        // Act
        List<Booking> bookings = bookingRepository.findBookingsByCheckInAndCheckOutDates(
                Date.valueOf(LocalDate.of(2024, 12, 5)),
                Date.valueOf(LocalDate.of(2024, 12, 20)));

        // Assert
        assertTrue(bookings.contains(booking1));
        assertTrue(bookings.contains(booking2));
    }
*/
    @Test
    public void testFindBookingsByPage() {
        // Arrange
        Long hotelId = 1L;
        for (int i = 0; i < 10; i++) {
            Booking booking = createBooking(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 10), true);
            booking.setHotelId(hotelId);
            bookingRepository.save(booking);
        }

        Pageable pageable = PageRequest.of(0, 5);

        // Act
        List<Booking> bookings = bookingRepository.findBookingsByPage(pageable, hotelId);

        // Assert
        assertEquals(5, bookings.size());
    }

    /*@Test
    public void testFindMyBookingsByPage() {
        // Arrange
        Long userId = 1L;

        // Créer et sauvegarder l'utilisateur
        User user = new User();
        user.setId(userId);

        // Simuler une persistance de l'utilisateur (adapter selon le contexte)
        // Exemple : userRepository.save(user); (si un UserRepository est disponible)

        for (int i = 0; i < 10; i++) {
            Booking booking = createBooking(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 10), true);
            booking.setUser(user); // Associer l'utilisateur au booking
            bookingRepository.save(booking); // Sauvegarder le booking
        }

        Pageable pageable = PageRequest.of(0, 5);

        // Act
        List<Booking> bookings = bookingRepository.findMyBookingsByPage(pageable, userId);

        // Assert
        assertEquals(5, bookings.size());
    }*/

    @Test
    public void testFindAllBookingsByPage() {
        // Arrange
        for (int i = 0; i < 10; i++) {
            Booking booking = createBooking(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 10), true);
            bookingRepository.save(booking);
        }

        Pageable pageable = PageRequest.of(0, 5);

        // Act
        List<Booking> bookings = bookingRepository.findAllBookingsByPage(pageable);

        // Assert
        assertEquals(5, bookings.size());
    }

    private Booking createBooking(LocalDate checkIn, LocalDate checkOut, boolean paymentStatus) {
        Booking booking = new Booking();
        booking.setCheckInDate(Date.valueOf(checkIn));
        booking.setCheckOutDate(Date.valueOf(checkOut));
        booking.setPaymentStatus(paymentStatus);

        return booking;
    }
}