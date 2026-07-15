package com.bank.dao;

import com.bank.entity.Customer;

import java.util.List;

/**
 * DAO interface for Customer operations.
 */
public interface CustomerDAO {


    int save(Customer customer);

    Customer findById(Long customerId);

    Customer findByCustomerNumber(String customerNumber);

    Customer findByCitizenshipNumber(String citizenshipNumber);

    Customer findByPanNumber(String panNumber);

    Customer findByEmail(String email);

    List<Customer> findAll();

    List<Customer> search(String keyword);

    int update(Customer customer);

    int closeCustomer(Long customerId);

}