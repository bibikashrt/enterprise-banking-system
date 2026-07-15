package com.bank.mapper;

import com.bank.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerMapper {


    int insert(Customer customer);

    Customer findById(Long customerId);

    Customer findByCustomerNumber(String customerNumber);

    Customer findByCitizenshipNumber(String citizenshipNumber);

    Customer findByPanNumber(String panNumber);

    Customer findByEmail(String email);

    List<Customer> findAll();

    List<Customer> search(@Param("keyword") String keyword);

    int update(Customer customer);

    int closeCustomer(Long customerId);

}