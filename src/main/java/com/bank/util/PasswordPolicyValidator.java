package com.bank.util;

import com.bank.exception.InvalidOperationException;

public class PasswordPolicyValidator {


    public static void validate(String password) {


        if(password == null || password.length() < 8) {
            throw new InvalidOperationException(
                    "Password must contain at least 8 characters."
            );
        }


        if(!password.matches(".*[A-Z].*")) {
            throw new InvalidOperationException(
                    "Password must contain at least one uppercase letter."
            );
        }


        if(!password.matches(".*[a-z].*")) {
            throw new InvalidOperationException(
                    "Password must contain at least one lowercase letter."
            );
        }


        if(!password.matches(".*\\d.*")) {
            throw new InvalidOperationException(
                    "Password must contain at least one number."
            );
        }


        if(!password.matches(".*[@#$%^&+=].*")) {
            throw new InvalidOperationException(
                    "Password must contain at least one special character."
            );
        }
    }
}