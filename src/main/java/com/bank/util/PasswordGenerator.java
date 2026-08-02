package com.bank.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$";


    public static String generate() {

        SecureRandom random = new SecureRandom();

        StringBuilder password =
                new StringBuilder();


        for (int i = 0; i < 10; i++) {

            password.append(
                    CHARACTERS.charAt(
                            random.nextInt(
                                    CHARACTERS.length()
                            )
                    )
            );
        }

        return password.toString();
    }

    public static void main(String[] args) {

        String password =
                PasswordGenerator.generate();

        System.out.println(
                "Temporary Password: " + password
        );


        String hash =
                PasswordUtil.hashPassword(password);


        System.out.println(
                "Password Hash: " + hash
        );

    }
}