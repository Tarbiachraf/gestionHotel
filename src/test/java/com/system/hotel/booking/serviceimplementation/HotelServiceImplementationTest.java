package com.system.hotel.booking.serviceimplementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import com.system.hotel.booking.entity.Hotel;
import com.system.hotel.booking.entity.HotelRoomType;
import com.system.hotel.booking.entity.User;
import com.system.hotel.booking.repository.HotelRepository;
import com.system.hotel.booking.repository.HotelRoomTypeRepository;
import com.system.hotel.booking.repository.UserRepository;
import com.system.hotel.booking.common.RoomAndBookingCountsVO;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Disabled("Cette classe est désactivée pour le moment")
class HotelServiceImplementationTest {

    @InjectMocks
    private HotelServiceImplementation hotelService;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;
    @Autowired // Injecté grâce à la configuration EmailServiceTestConfig
    private JavaMailSender mailSender;
    @Mock
    private HotelRoomTypeRepository hotelRoomTypeRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel savedHotel = hotelService.createHotel(hotel);
        assertNotNull(savedHotel);
        assertEquals("Test Hotel", savedHotel.getName());

        verify(hotelRepository, times(1)).save(hotel);
    }

   /* @Test
    void testCreateHotelUser() throws MessagingException {
        // Arrange: Préparation des données
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setEmail("test@example.com");

        // Mock du comportement de mailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Mock du comportement de userRepository avec validation
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("Test Hotel", user.getName(), "User name does not match Hotel name");
            assertEquals("test@example.com", user.getEmail(), "User email does not match Hotel email");
            assertEquals("Test Hotel@123", user.getPassword(), "User password does not match expected password");
            assertEquals(UserRole.HOTEL_ADMIN, user.getRole(), "User role is incorrect");
            return user; // Retourne l'utilisateur simulé
        });

        // Act: Appel de la méthode à tester
        User savedUser = hotelService.createHotelUser(hotel);

        // Assert: Vérifications finales
        assertNotNull(savedUser, "The returned user is null");
        assertEquals("Test Hotel", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("Test Hotel@123", savedUser.getPassword());
        assertEquals(UserRole.HOTEL_ADMIN, savedUser.getRole());

        // Vérification des interactions avec les mocks
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(userRepository, times(1)).save(any(User.class));
    }*/


    @Test
    void testGetHotelById() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel foundHotel = hotelService.getHotelById(1L);
        assertNotNull(foundHotel);
        assertEquals("Test Hotel", foundHotel.getName());

        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHotelById_NotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            hotelService.getHotelById(1L);
        });

        assertEquals("Hotel not found", exception.getMessage());
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel());
        hotels.add(new Hotel());

        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.getAllHotels();
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testDeleteHotelById() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setEmail("test@example.com");

        User user = new User();
        user.setEmail("test@example.com");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        hotelService.deleteHotelById(1L);

        verify(userRepository, times(1)).delete(user);
        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteHotelById_NotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            hotelService.deleteHotelById(1L);
        });

        assertEquals("Hotel not found", exception.getMessage());
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRoomsAndBookingCounts() {
        List<HotelRoomType> roomTypes = new ArrayList<>();
        HotelRoomType roomType1 = new HotelRoomType();
        roomType1.setTotalRooms(10);
        roomType1.setAvailableRooms(5);
        roomType1.setBookedCount(3);

        HotelRoomType roomType2 = new HotelRoomType();
        roomType2.setTotalRooms(15);
        roomType2.setAvailableRooms(10);
        roomType2.setBookedCount(5);

        roomTypes.add(roomType1);
        roomTypes.add(roomType2);

        when(hotelRoomTypeRepository.findByHotelId(1L)).thenReturn(roomTypes);

        RoomAndBookingCountsVO result = hotelService.getRoomsAndBookingCounts(1L);

        assertNotNull(result);
        assertEquals(25, result.getTotalRooms());
        assertEquals(15, result.getAvailableRooms());
        assertEquals(8, result.getBookingCounts());

        verify(hotelRoomTypeRepository, times(1)).findByHotelId(1L);
    }
}
