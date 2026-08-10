package com.bank.service.impl;


import com.bank.dao.LoanPenaltyDAO;
import com.bank.dto.response.LoanPenaltyResponse;
import com.bank.entity.LoanPenalty;
import com.bank.service.LoanPenaltyService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@ApplicationScoped
public class LoanPenaltyServiceImpl
        implements LoanPenaltyService {


    @Inject
    private LoanPenaltyDAO penaltyDAO;



    @Override
    public List<LoanPenaltyResponse> getPenaltiesByLoan(
            Long loanId) {


        log.info(
                "Fetching penalties for loan ID: {}",
                loanId
        );


        List<LoanPenalty> penalties =
                penaltyDAO.findByLoanId(
                        loanId
                );


        return penalties.stream()
                .map(this::mapToResponse)
                .toList();

    }



    private LoanPenaltyResponse mapToResponse(
            LoanPenalty penalty) {


        return LoanPenaltyResponse.builder()

                .penaltyId(
                        penalty.getPenaltyId()
                )

                .loanId(
                        penalty.getLoanId()
                )

                .scheduleId(
                        penalty.getScheduleId()
                )

                .penaltyAmount(
                        penalty.getPenaltyAmount()
                )

                .penaltyType(
                        penalty.getPenaltyType()
                )

                .overdueDays(
                        penalty.getOverdueDays()
                )

                .penaltyRate(
                        penalty.getPenaltyRate()
                )

                .calculatedDate(
                        penalty.getCalculatedDate()
                )

                .paid(
                        penalty.getPaid()
                )

                .createdAt(
                        penalty.getCreatedAt()
                )

                .build();

    }

}