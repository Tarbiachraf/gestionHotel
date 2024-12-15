package com.system.hotel.booking.serviceimplementation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.system.hotel.booking.common.BookingAndUserNameVO;
import com.system.hotel.booking.entity.*;
import com.system.hotel.booking.repository.BookingRepository;
import com.system.hotel.booking.repository.HotelRoomTypeRepository;
import com.system.hotel.booking.repository.RoomRepository;
import com.system.hotel.booking.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

class BookingServiceImplementationTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRoomTypeRepository hotelRoomTypeRepository;

    @InjectMocks
    private BookingServiceImplementation bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableRoomsByRoomTypeIdAndCheckInAndCheckOutDates() {
        Long roomTypeId = 1L;
        Date checkInDate = new Date();
        Date checkOutDate = new Date();

        // Mock room objects
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);

        // Mock booking objects with associated rooms
        Booking booking1 = new Booking();
        booking1.setRooms(Collections.singletonList(room1)); // Associate room1 with booking1

        List<Booking> existingBookings = Collections.singletonList(booking1);
        List<Room> availableRooms = Arrays.asList(room1, room2);

        // Mock repository behaviors
        when(bookingRepository.findBookingsByCheckInAndCheckOutDates(checkInDate, checkOutDate))
                .thenReturn(existingBookings);
        when(roomRepository.findByHotelRoomTypeIdAndAvailabilityTrue(roomTypeId))
                .thenReturn(availableRooms);

        // Call the service method
        Long availableRoomsCount = bookingService.getAvailableRoomsByRoomTypeIdAndCheckInAndCheckOutDates(roomTypeId, checkInDate, checkOutDate);

        // Assert the result
        assertEquals(1L, availableRoomsCount); // Only room2 is available
    }


   /*@Test
    void testCreateBooking_Success() {
        Long hotelRoomTypeId = 1L;
        Long userId = 1L;
        Booking booking = new Booking();
        booking.setCheckInDate(new Date());
        booking.setCheckOutDate(new Date());
        booking.setNumOfRooms(1L);

        HotelRoomType hotelRoomType = new HotelRoomType();
        hotelRoomType.setHotel(new Hotel());

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        Room room1 = new Room();
        room1.setId(1L);
        List<Room> availableRooms = Arrays.asList(room1);

        when(hotelRoomTypeRepository.findById(hotelRoomTypeId)).thenReturn(Optional.of(hotelRoomType));
        when(bookingRepository.findBookingsByCheckInAndCheckOutDates(any(), any())).thenReturn(new ArrayList<>());
        when(roomRepository.findByHotelRoomTypeIdAndAvailabilityTrue(hotelRoomTypeId)).thenReturn(availableRooms);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingIdAndRoomIds bookingIdAndRoomIds = bookingService.createBooking(booking, hotelRoomTypeId, userId);

        assertNotNull(bookingIdAndRoomIds.getBookingId());
        assertEquals(1L, bookingIdAndRoomIds.getRoomIds().size());
    }
*/
   /* @Test
    void testCreateBooking_NotEnoughRooms() {
        Long hotelRoomTypeId = 1L;
        Long userId = 1L;
        Booking booking = new Booking();
        booking.setCheckInDate(new Date());
        booking.setCheckOutDate(new Date());
        booking.setNumOfRooms(5L);  // Trying to book more rooms than available

        HotelRoomType hotelRoomType = new HotelRoomType();
        hotelRoomType.setHotel(new Hotel());

        Room room1 = new Room();
        room1.setId(1L);
        List<Room> availableRooms = Arrays.asList(room1);

        when(hotelRoomTypeRepository.findById(hotelRoomTypeId)).thenReturn(Optional.of(hotelRoomType));
        when(bookingRepository.findBookingsByCheckInAndCheckOutDates(any(), any())).thenReturn(new ArrayList<>());
        when(roomRepository.findByHotelRoomTypeIdAndAvailabilityTrue(hotelRoomTypeId)).thenReturn(availableRooms);

        BookingIdAndRoomIds bookingIdAndRoomIds = bookingService.createBooking(booking, hotelRoomTypeId, userId);

        assertEquals(0, bookingIdAndRoomIds.getRoomIds().size());  // No rooms should be booked
    }
*/
    @Test
    void testDeleteBookingById() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        Room room1 = new Room();
        room1.setHotelRoomType(new HotelRoomType());
        room1.getHotelRoomType().setId(1L);
        booking.setRooms(Arrays.asList(room1));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(hotelRoomTypeRepository.findById(1L)).thenReturn(Optional.of(new HotelRoomType()));

        bookingService.deleteBookingById(bookingId);

        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testGetBookingsByPage() {
        int pageNumber = 1;
        Long hotelId = 1L;

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setCheckInDate(new Date());
        booking1.setCheckOutDate(new Date());
        booking1.setNumOfRooms(1L);
        booking1.setCost(100.0);
        User user1 = new User();
        user1.setName("John Doe");
        booking1.setUser(user1);

        List<Booking> bookings = Arrays.asList(booking1);

        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        when(bookingRepository.findBookingsByPage(pageable, hotelId)).thenReturn(bookings);

        List<BookingAndUserNameVO> result = bookingService.getBookingsByPage(pageNumber, hotelId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetMyBookingsByPage() {
        int pageNumber = 1;
        Long userId = 1L;

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setCheckInDate(new Date());
        booking1.setCheckOutDate(new Date());
        booking1.setNumOfRooms(1L);
        booking1.setCost(100.0);
        User user1 = new User();
        user1.setName("John Doe");
        booking1.setUser(user1);

        List<Booking> bookings = Arrays.asList(booking1);

        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        when(bookingRepository.findMyBookingsByPage(pageable, userId)).thenReturn(bookings);

        List<BookingAndUserNameVO> result = bookingService.getMyBookingsByPage(pageNumber, userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetAllBookingsByPage() {
        int pageNumber = 1;

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setCheckInDate(new Date());
        booking1.setCheckOutDate(new Date());
        booking1.setNumOfRooms(1L);
        booking1.setCost(100.0);
        User user1 = new User();
        user1.setName("John Doe");
        booking1.setUser(user1);

        List<Booking> bookings = Arrays.asList(booking1);

        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        when(bookingRepository.findAllBookingsByPage(pageable)).thenReturn(bookings);

        List<BookingAndUserNameVO> result = bookingService.getAllBookingsByPage(pageNumber);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
}