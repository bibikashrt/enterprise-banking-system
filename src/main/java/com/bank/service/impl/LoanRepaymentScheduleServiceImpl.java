package com.bank.service.impl;

import com.bank.dao.LoanPenaltyDAO;
import com.bank.dao.LoanRepaymentScheduleDAO;
import com.bank.dto.response.LoanRepaymentScheduleResponse;
import com.bank.entity.LoanPenalty;
import com.bank.entity.LoanRepaymentSchedule;
import com.bank.service.LoanRepaymentScheduleService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class LoanRepaymentScheduleServiceImpl
        implements LoanRepaymentScheduleService {


    @Inject
    private LoanRepaymentScheduleDAO scheduleDAO;


    @Inject
    private LoanPenaltyDAO penaltyDAO;



    @Override
    public List<LoanRepaymentScheduleResponse> getByLoanId(
            Long loanId) {


        List<LoanRepaymentSchedule> schedules =
                scheduleDAO.findByLoanId(loanId);


        List<LoanRepaymentScheduleResponse> responses =
                new ArrayList<>();


        for (LoanRepaymentSchedule schedule : schedules) {


            LoanPenalty penalty =
                    penaltyDAO.findByScheduleId(
                            schedule.getScheduleId()
                    );


            BigDecimal penaltyAmount =
                    BigDecimal.ZERO;


            if (penalty != null) {

                penaltyAmount =
                        penalty.getPenaltyAmount();
            }


            BigDecimal payableAmount =
                    schedule.getTotalAmount()
                            .add(penaltyAmount);



            LoanRepaymentScheduleResponse response =
                    LoanRepaymentScheduleResponse.builder()

                            .scheduleId(
                                    schedule.getScheduleId()
                            )

                            .loanId(
                                    schedule.getLoanId()
                            )

                            .installmentNumber(
                                    schedule.getInstallmentNumber()
                            )

                            .dueDate(
                                    schedule.getDueDate()
                            )

                            .principalAmount(
                                    schedule.getPrincipalAmount()
                            )

                            .interestAmount(
                                    schedule.getInterestAmount()
                            )

                            .totalAmount(
                                    schedule.getTotalAmount()
                            )

                            .penaltyAmount(
                                    penaltyAmount
                            )

                            .payableAmount(
                                    payableAmount
                            )

                            .scheduleStatus(
                                    schedule.getScheduleStatus()
                            )

                            .createdAt(
                                    schedule.getCreatedAt()
                            )

                            .updatedAt(
                                    schedule.getUpdatedAt()
                            )

                            .build();


            responses.add(response);
        }


        return responses;
    }

}