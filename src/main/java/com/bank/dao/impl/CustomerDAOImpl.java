package com.bank.dao.impl;

import com.bank.dao.CustomerDAO;
import com.bank.entity.Customer;
import com.bank.mapper.CustomerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerDAOImpl implements CustomerDAO {

    @Inject
    private SqlSession sqlSession;

    private CustomerMapper mapper() {
        return sqlSession.getMapper(CustomerMapper.class);
    }

    @Override
    public int save(Customer customer) {

        log.debug("Saving customer: {}", customer.getCustomerNumber());

        return mapper().insert(customer);
    }

    @Override
    public Customer findById(Long customerId) {
        return mapper().findById(customerId);
    }

    @Override
    public Customer findByCustomerNumber(String customerNumber) {
        return mapper().findByCustomerNumber(customerNumber);
    }

    @Override
    public Customer findByCitizenshipNumber(String citizenshipNumber) {
        return mapper().findByCitizenshipNumber(citizenshipNumber);
    }

    @Override
    public Customer findByPanNumber(String panNumber) {
        return mapper().findByPanNumber(panNumber);
    }

    @Override
    public Customer findByEmail(String email) {
        return mapper().findByEmail(email);
    }

    @Override
    public List<Customer> findAll() {
        return mapper().findAll();
    }

    @Override
    public List<Customer> search(String keyword) {
        return mapper().search(keyword);
    }

    @Override
    public int update(Customer customer) {

        log.debug("Updating customer: {}", customer.getCustomerNumber());

        return mapper().update(customer);
    }

    @Override
    public int closeCustomer(Long customerId) {

        log.debug("Closing customer: {}", customerId);

        return mapper().closeCustomer(customerId);
    }
}