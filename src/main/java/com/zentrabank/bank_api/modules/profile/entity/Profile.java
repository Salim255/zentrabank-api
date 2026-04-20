package com.zentrabank.bank_api.modules.profile.entity;

import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Profile {
    private UUID id;
    private String firstName;
    private String lastName;
    private String addressLine;
    private String city;
    private String country;
    private String zipCode;
    private String phoneNumber;
    private String phoneType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String employmentStatus;
    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    User user; // The User object that owns this account

}