package com.bank.util;

import java.util.UUID;

public class AccountNumberGenerator {

    private AccountNumberGenerator() {
    }

    public static String generate() {

        return "ACC-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 10)
                .toUpperCase();
    }
}