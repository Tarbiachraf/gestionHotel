package com.system.hotel.booking.controller;

import java.util.Date;
import java.util.List;

import com.system.hotel.booking.entity.Room;
import com.system.hotel.booking.exception.ResourceNotFoundException;
import com.system.hotel.booking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.system.hotel.booking.common.BookingAndUserNameVO;
import com.system.hotel.booking.common.BookingIdAndRoomIds;
import com.system.hotel.booking.entity.Booking;
import com.system.hotel.booking.services.BookingService;

@RestController
@CrossOrigin
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	@Autowired
	private RoomRepository roomRepository;

	@GetMapping("/getRoomCount")
	public ResponseEntity<Long> getAvailableRooms(
			@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkInDate,
			@RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkOutDate,
			@RequestParam("roomTypeId") Long roomTypeId) throws ResourceNotFoundException {
		Long availableRooms = bookingService.getAvailableRoomsByRoomTypeIdAndCheckInAndCheckOutDates(roomTypeId, checkInDate, checkOutDate);
		if (availableRooms == null) {
			throw new ResourceNotFoundException("No rooms available for the selected dates.");
		}
		return ResponseEntity.ok(availableRooms);
	}

	@PostMapping("/create")
	public ResponseEntity<BookingIdAndRoomIds> createBooking(
			@RequestBody Booking booking,
			@RequestParam("hotelRoomTypeId") Long hotelRoomTypeId,
			@RequestParam("userId") Long userId) {
		BookingIdAndRoomIds bookingIdAndRoomIds = bookingService.createBooking(booking,
				hotelRoomTypeId, userId);
		if (!bookingIdAndRoomIds.getRoomIds().isEmpty()) {
			return ResponseEntity.ok(bookingIdAndRoomIds);
		} else {
			throw new IllegalArgumentException(booking.getNumOfRooms() +
					" rooms not available for these dates");
		}
	}


	@GetMapping("/getBookings/{page}")
	public ResponseEntity<List<BookingAndUserNameVO>> getBookingsByPage(
			@PathVariable("page") int page,
			@RequestParam("hotelId") Long hotelId) {
		List<BookingAndUserNameVO> bookings = bookingService.getBookingsByPage(page, hotelId);
		return ResponseEntity.ok(bookings);
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> deleteBooking(@PathVariable("bookingId") Long bookingId) {
		bookingService.deleteBookingById(bookingId);
		return ResponseEntity.ok("Booking deleted successfully");
	}

	@GetMapping("/mybookings/{page}")
	public ResponseEntity<List<BookingAndUserNameVO>> getMyBookingsByPage(
			@PathVariable("page") int page,
			@RequestParam("userId") Long userId) {
		List<BookingAndUserNameVO> bookings = bookingService.getMyBookingsByPage(page, userId);
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/getAllBookings/{page}")
	public ResponseEntity<List<BookingAndUserNameVO>> getAllBookingsByPage(@PathVariable("page") int page) {
		List<BookingAndUserNameVO> bookings = bookingService.getAllBookingsByPage(page);
		return ResponseEntity.ok(bookings);
	}
}