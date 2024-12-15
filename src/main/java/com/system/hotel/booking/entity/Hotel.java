package com.system.hotel.booking.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "hotel")
@AllArgsConstructor
@Data
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    private String email;
    
    @Size(min = 10, max = 10)
    private String phone;
    
    private String country;

    private String location;

    private String address;

    private String image;
    
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelRoomType> roomTypes;
    
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

	public Hotel() {
	//
	}
}
