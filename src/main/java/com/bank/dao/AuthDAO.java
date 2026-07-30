package com.bank.dao;

import com.bank.entity.Employee;

public interface AuthDAO {

    Employee findByEmail(String email);

}