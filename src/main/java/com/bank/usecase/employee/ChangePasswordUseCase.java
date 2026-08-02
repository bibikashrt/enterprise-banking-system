package com.bank.usecase.employee;

import com.bank.dao.EmployeeDAO;
import com.bank.dto.request.ChangePasswordRequest;
import com.bank.entity.Employee;
import com.bank.exception.InvalidOperationException;
import com.bank.util.PasswordPolicyValidator;
import com.bank.util.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class ChangePasswordUseCase {


    @Inject
    private EmployeeDAO employeeDAO;


    @Transactional(rollbackOn = Exception.class)
    public void execute(
            Long employeeId,
            ChangePasswordRequest request
    ) {


        Employee employee =
                employeeDAO.findById(employeeId);


        if(employee == null){
            throw new InvalidOperationException(
                    "Employee not found."
            );
        }

        if(!request.getNewPassword()
                .equals(request.getConfirmPassword())){

            throw new InvalidOperationException(
                    "New password and confirm password do not match."
            );
        }


        boolean valid =
                PasswordUtil.verifyPassword(
                        request.getOldPassword(),
                        employee.getPasswordHash()
                );


        if(!valid){
            throw new InvalidOperationException(
                    "Old password is incorrect."
            );
        }

        PasswordPolicyValidator.validate(
                request.getNewPassword()
        );


        String newHash =
                PasswordUtil.hashPassword(
                        request.getNewPassword()
                );


        employeeDAO.changePassword(
                employeeId,
                newHash
        );
    }
}