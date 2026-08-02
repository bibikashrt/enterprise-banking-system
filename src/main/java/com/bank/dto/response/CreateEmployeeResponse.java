package com.bank.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmployeeResponse {

    private Long employeeId;

    private String employeeNumber;

    private String email;

    private String temporaryPassword;
}