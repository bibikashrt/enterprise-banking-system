package com.bank.service;

import com.bank.dto.request.CreateAccountRequest;
import com.bank.dto.request.UpdateAccountRequest;
import com.bank.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request);

    AccountResponse updateAccount(
            Long accountId,
            UpdateAccountRequest request);

    AccountResponse getAccountById(Long accountId);

    AccountResponse getAccountByAccountNumber(String accountNumber);

    List<AccountResponse> getAccountsByCustomer(Long customerId);

    List<AccountResponse> getAccountsByBranch(Long branchId);

    List<AccountResponse> getAllAccounts();

    List<AccountResponse> searchAccounts(String keyword);

    void closeAccount(Long accountId);

}