package com.bank.mapper;

import com.bank.entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface AuthMapper {

    Employee findByEmail(@Param("email") String email);

}