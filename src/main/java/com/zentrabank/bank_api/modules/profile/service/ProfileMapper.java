package com.zentrabank.bank_api.modules.profile.service;

import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;
import com.zentrabank.bank_api.modules.profile.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile toEntity(CreateProfileDto dto) {
        return Profile.builder()
                .title(dto.title())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .dateOfBirth(dto.dateOfBirth())
                .addressLine(dto.addressLine())
                .city(dto.city())
                .country(dto.country())
                .zipCode(dto.zipCode())
                .phoneNumber(dto.phoneNumber())
                .phoneType(dto.phoneType())
                .employmentStatus(dto.employmentStatus())
                .build();
    }

    public void updateEntity(Profile profile, CreateProfileDto dto) {
        profile.setTitle(dto.title());
        profile.setFirstName(dto.firstName());
        profile.setLastName(dto.lastName());
        profile.setAddressLine(dto.addressLine());
        profile.setCity(dto.city());
        profile.setCountry(dto.country());
        profile.setZipCode(dto.zipCode());
        profile.setPhoneNumber(dto.phoneNumber());
        profile.setPhoneType(dto.phoneType());
        profile.setEmploymentStatus(dto.employmentStatus());
    }

    public ProfileDto toDto(Profile profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getTitle(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getDateOfBirth(),
                profile.getAddressLine(),
                profile.getCity(),
                profile.getCountry(),
                profile.getZipCode(),
                profile.getPhoneNumber(),
                profile.getPhoneType(),
                profile.getEmploymentStatus()
        );
    }
}
