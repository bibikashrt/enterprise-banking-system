package com.bank.util;

public class PasswordGenerator {

    public static void main(String[] args) {

        String password = "admin123";

        String hash = PasswordUtil.hashPassword(password);

        System.out.println(hash);

        System.out.println(
                PasswordUtil.verifyPassword(password, hash)
        );

        System.out.println(
                PasswordUtil.verifyPassword("WrongPassword", hash)
        );
    }
}