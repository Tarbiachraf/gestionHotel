package com.system.hotel.booking.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 100)
    private String location;

    @Size(max = 250)
    private String description;

    private Boolean availability;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_room_type_id", nullable = true)
    private HotelRoomType hotelRoomType;

    @JsonIgnore
    @ManyToMany(mappedBy = "rooms",cascade = CascadeType.ALL)
    private List<Booking> bookings;
    
	public Room() {
		super();
	}

	public Room(Long id, @NotBlank @Size(max = 100) String name, @Size(max = 100) String location,
			@Size(max = 250) String description, Boolean availability, HotelRoomType hotelRoomType,
			List<Booking> bookings) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.description = description;
		this.availability = availability;
		this.hotelRoomType = hotelRoomType;
		this.bookings = bookings;
	}


}
