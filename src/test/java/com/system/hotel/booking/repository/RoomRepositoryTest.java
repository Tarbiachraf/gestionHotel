package com.system.hotel.booking.repository;

import com.system.hotel.booking.entity.Hotel;
import com.system.hotel.booking.entity.HotelRoomType;
import com.system.hotel.booking.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

class RoomRepositoryTest {

    @Autowired
    private HotelRoomTypeRepository hotelRoomTypeRepository;
    @Autowired
    private  RoomRepository roomRepository;
    @Autowired
    private  HotelRepository hotelRepository;


    private List<Long> roomTypeIds;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel = hotelRepository.save(hotel);

        roomTypeIds = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HotelRoomType roomType = new HotelRoomType();
            roomType.setName("Room Type " + i);
            roomType.setDescription("Description of Room Type " + i); // Ajoutez une description valide
            roomType.setHotel(hotel);
            roomType = hotelRoomTypeRepository.save(roomType);
            roomTypeIds.add(roomType.getId());

            for (int j = 1; j <= 2; j++) {
                Room room = new Room();
                room.setName("Room " + j + " of Type " + i);
                room.setHotelRoomType(roomType);
                roomRepository.save(room);
            }
        }
    }

    @Test
    void findRoomsByRoomTypeId() {
        Pageable pageable = PageRequest.of(0, 4);

        // Assurez-vous que la méthode existe dans votre repository et est correcte
        List<Room> rooms = roomRepository.findRoomsByRoomTypeId(pageable, roomTypeIds);

        // Vérifier les résultats
        assertThat(rooms).hasSize(4); // Doit renvoyer 4 résultats (en fonction du Pageable)
        assertThat(rooms.get(0).getName()).isEqualTo("Room 2 of Type 5"); // Vérifie l'ordre DESC
        assertThat(rooms.get(1).getName()).isEqualTo("Room 1 of Type 5");
        assertThat(rooms.get(2).getName()).isEqualTo("Room 2 of Type 4");
        assertThat(rooms.get(3).getName()).isEqualTo("Room 1 of Type 4");
    }
}