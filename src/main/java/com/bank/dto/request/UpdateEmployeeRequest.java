package com.bank.dto.request;

import com.bank.enums.EmployeeRole;
import com.bank.enums.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {

    private Long employeeId;

    @NotBlank(message = "First name is required.")
    @Size(
            max = 100,
            message = "First name must not exceed 100 characters."
    )
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(
            max = 100,
            message = "Last name must not exceed 100 characters."
    )
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(
            max = 150,
            message = "Email must not exceed 150 characters."
    )
    private String email;

    @Size(
            max = 20,
            message = "Phone number must not exceed 20 characters."
    )
    private String phoneNumber;

    @NotNull(message = "Employee role is required.")
    private EmployeeRole employeeRole;

    @NotNull(message = "Employee status is required.")
    private EmployeeStatus employeeStatus;
}