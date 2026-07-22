package com.bank.service.impl;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.BeneficiaryDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dto.request.CreateBeneficiaryRequest;
import com.bank.dto.request.UpdateBeneficiaryRequest;
import com.bank.dto.response.BeneficiaryResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Beneficiary;
import com.bank.entity.Customer;
import com.bank.enums.AccountStatus;
import com.bank.enums.BeneficiaryStatus;
import com.bank.enums.CustomerStatus;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BeneficiaryNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.service.BeneficiaryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class BeneficiaryServiceImpl implements BeneficiaryService {

    @Inject
    private BeneficiaryDAO beneficiaryDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BeneficiaryResponse createBeneficiary(
            CreateBeneficiaryRequest request) {

        log.info("Creating beneficiary for customer ID: {}",
                request.getCustomerId());

        Customer customer =
                customerDAO.findById(request.getCustomerId());

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: "
                            + request.getCustomerId());
        }

        if (customer.getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Customer is not active.");
        }

        Account account =
                accountDAO.findById(request.getBeneficiaryAccountId());

        if (account == null) {
            throw new AccountNotFoundException(
                    "Beneficiary account not found with ID: "
                            + request.getBeneficiaryAccountId());
        }

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Beneficiary account is not active.");
        }

        if (account.getCustomerId().equals(request.getCustomerId())) {
            throw new InvalidOperationException(
                    "Customer cannot add own account as beneficiary.");
        }

        Beneficiary existingBeneficiary =
                beneficiaryDAO.findByCustomerAndAccount(
                        request.getCustomerId(),
                        request.getBeneficiaryAccountId());

        if (existingBeneficiary != null) {
            throw new InvalidOperationException(
                    "Beneficiary already exists.");
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .customerId(request.getCustomerId())
                .beneficiaryAccountId(
                        request.getBeneficiaryAccountId())
                .beneficiaryName(request.getBeneficiaryName())
                .nickname(request.getNickname())
                .beneficiaryStatus(BeneficiaryStatus.ACTIVE)
                .build();

        beneficiaryDAO.save(beneficiary);

        AuditLog auditLog = AuditLog.builder()
                .action("CREATE")
                .entityName("BENEFICIARY")
                .entityId(beneficiary.getBeneficiaryId())
                .description("Beneficiary created successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info(
                "Beneficiary created successfully. Beneficiary ID: {}",
                beneficiary.getBeneficiaryId());

        return mapToResponse(beneficiary);
    }

    @Override
    public BeneficiaryResponse getBeneficiaryById(
            Long beneficiaryId) {

        log.info("Fetching beneficiary with ID: {}", beneficiaryId);

        Beneficiary beneficiary =
                beneficiaryDAO.findById(beneficiaryId);

        if (beneficiary == null) {
            throw new BeneficiaryNotFoundException(
                    "Beneficiary not found with ID: " + beneficiaryId);
        }

        return mapToResponse(beneficiary);
    }

    @Override
    public List<BeneficiaryResponse> getBeneficiariesByCustomer(
            Long customerId) {

        log.info("Fetching beneficiaries for customer ID: {}",
                customerId);

        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        List<Beneficiary> beneficiaries =
                beneficiaryDAO.findByCustomerId(customerId);

        List<BeneficiaryResponse> responses = new ArrayList<>();

        for (Beneficiary beneficiary : beneficiaries) {
            responses.add(mapToResponse(beneficiary));
        }

        log.info("Total beneficiaries found: {}", responses.size());

        return responses;
    }

    @Override
    public List<BeneficiaryResponse> getAllBeneficiaries() {

        log.info("Fetching all beneficiaries.");

        List<Beneficiary> beneficiaries =
                beneficiaryDAO.findAll();

        List<BeneficiaryResponse> responses = new ArrayList<>();

        for (Beneficiary beneficiary : beneficiaries) {
            responses.add(mapToResponse(beneficiary));
        }

        log.info("Total beneficiaries found: {}", responses.size());

        return responses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BeneficiaryResponse updateBeneficiary(
            Long beneficiaryId,
            UpdateBeneficiaryRequest request) {

        log.info("Updating beneficiary with ID: {}", beneficiaryId);

        Beneficiary beneficiary =
                beneficiaryDAO.findById(beneficiaryId);

        if (beneficiary == null) {
            throw new BeneficiaryNotFoundException(
                    "Beneficiary not found with ID: " + beneficiaryId);
        }

        beneficiary.setNickname(request.getNickname());
        beneficiary.setBeneficiaryStatus(
                request.getBeneficiaryStatus());

        beneficiaryDAO.update(beneficiary);

        AuditLog auditLog = AuditLog.builder()
                .action("UPDATE")
                .entityName("BENEFICIARY")
                .entityId(beneficiary.getBeneficiaryId())
                .description("Beneficiary updated successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info(
                "Beneficiary updated successfully. Beneficiary ID: {}",
                beneficiaryId);

        return mapToResponse(beneficiary);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deactivateBeneficiary(Long beneficiaryId) {

        log.info("Deactivating beneficiary with ID: {}",
                beneficiaryId);

        Beneficiary beneficiary =
                beneficiaryDAO.findById(beneficiaryId);

        if (beneficiary == null) {
            throw new BeneficiaryNotFoundException(
                    "Beneficiary not found with ID: " + beneficiaryId);
        }

        beneficiaryDAO.deactivate(beneficiaryId);

        AuditLog auditLog = AuditLog.builder()
                .action("DEACTIVATE")
                .entityName("BENEFICIARY")
                .entityId(beneficiaryId)
                .description("Beneficiary deactivated successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info(
                "Beneficiary deactivated successfully. Beneficiary ID: {}",
                beneficiaryId);
    }

    private BeneficiaryResponse mapToResponse(
            Beneficiary beneficiary) {

        return BeneficiaryResponse.builder()
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .customerId(beneficiary.getCustomerId())
                .beneficiaryAccountId(
                        beneficiary.getBeneficiaryAccountId())
                .beneficiaryName(beneficiary.getBeneficiaryName())
                .nickname(beneficiary.getNickname())
                .beneficiaryStatus(
                        beneficiary.getBeneficiaryStatus())
                .createdAt(beneficiary.getCreatedAt())
                .updatedAt(beneficiary.getUpdatedAt())
                .build();
    }
}