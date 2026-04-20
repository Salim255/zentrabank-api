package com.zentrabank.bank_api.modules.profile.entity;

import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    // ---------- PERSONAL INFO ----------

    @NotNull(message = "Title is required")
    @Enumerated(EnumType.STRING)
    private PersonTitle title;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    // ---------- ADDRESS ----------

    @NotBlank(message = "Address line is required")
    @Size(max = 120)
    private String addressLine;

    @NotBlank(message = "City is required")
    @Size(max = 80)
    private String city;

    @NotBlank(message = "Country is required")
    @Size(max = 80)
    private String country;

    @NotBlank(message = "Zip code is required")
    @Size(max = 20)
    private String zipCode;

    // ---------- PHONE ----------

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+()\\-\\s]{6,20}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @NotBlank(message = "Phone type is required")
    @Size(max = 20)
    private String phoneType;

    // ---------- EMPLOYMENT ----------

    @NotNull(message = "Employment status is required")
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    // ---------- RELATIONSHIP ----------

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ---------- TIMESTAMPS ----------

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
