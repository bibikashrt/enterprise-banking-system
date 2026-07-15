package com.bank.entity;

import com.bank.enums.CustomerStatus;
import com.bank.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Customer entity representing the customer table.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Long customerId;

    private String customerNumber;

    private String firstName;

    private String middleName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String citizenshipNumber;

    private String panNumber;

    private String email;

    private String mobileNumber;

    private String address;

    private CustomerStatus customerStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}