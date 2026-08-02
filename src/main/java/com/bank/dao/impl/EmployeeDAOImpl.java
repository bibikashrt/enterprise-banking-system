package com.bank.dao.impl;

import com.bank.dao.EmployeeDAO;
import com.bank.entity.Employee;
import com.bank.mapper.EmployeeMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class EmployeeDAOImpl implements EmployeeDAO {

    @Inject
    private SqlSession sqlSession;


    private EmployeeMapper mapper() {
        return sqlSession.getMapper(EmployeeMapper.class);
    }

    @Override
    public int save(Employee employee) {

        log.debug("Saving employee: {}",
                employee.getEmployeeNumber());

        return mapper().insert(employee);
    }

    @Override
    public void changePassword(
            Long employeeId,
            String passwordHash
    ) {

        mapper().changePassword(
                employeeId,
                passwordHash
        );
    }

    @Override
    public Employee findById(Long employeeId) {

        return mapper().findById(employeeId);
    }

    @Override
    public Employee findByEmployeeNumber(
            String employeeNumber) {

        return mapper().findByEmployeeNumber(employeeNumber);
    }

    @Override
    public Employee findByEmail(String email) {

        return mapper().findByEmail(email);
    }

    @Override
    public List<Employee> findByBranchId(Long branchId) {

        return mapper().findByBranchId(branchId);
    }

    @Override
    public List<Employee> findAll() {

        return mapper().findAll();
    }

    @Override
    public List<Employee> search(String keyword) {

        return mapper().search(keyword);
    }

    @Override
    public int update(Employee employee) {

        log.debug("Updating employee: {}",
                employee.getEmployeeNumber());

        return mapper().update(employee);
    }

    @Override
    public int deactivate(Long employeeId) {

        log.debug("Deactivating employee: {}", employeeId);

        return mapper().deactivate(employeeId);
    }
}