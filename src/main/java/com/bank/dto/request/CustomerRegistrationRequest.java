package com.bank.dto.request;

import com.bank.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationRequest {

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        private String firstName;

        @Size(max = 100)
        private String middleName;

        @NotBlank(message = "Last name is required")
        @Size(max = 100)
        private String lastName;

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        @NotNull(message = "Gender is required")
        private Gender gender;

        @NotBlank(message = "Citizenship number is required")
        @Size(max = 50)
        private String citizenshipNumber;

        @Size(max = 20)
        private String panNumber;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        @Size(max = 150)
        private String email;

        @NotBlank(message = "Mobile number is required")
        private String mobileNumber;

        @NotBlank(message = "Address is required")
        private String address;

}