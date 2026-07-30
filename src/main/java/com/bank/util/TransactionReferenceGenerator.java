package com.bank.util;

import java.util.UUID;

public class TransactionReferenceGenerator {

    private TransactionReferenceGenerator() {

    }

    public static String generate() {

        return "TXN-"
                + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}