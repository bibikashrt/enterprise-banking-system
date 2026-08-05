package com.bank.usecase.loan;

import com.bank.dao.LoanDAO;
import com.bank.dao.LoanRepaymentScheduleDAO;
import com.bank.entity.Loan;
import com.bank.entity.LoanRepaymentSchedule;
import com.bank.exception.LoanNotFoundException;
import com.bank.service.interest.InterestCalculator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@ApplicationScoped
public class GenerateRepaymentScheduleUseCase {


    @Inject
    private LoanDAO loanDAO;


    @Inject
    private LoanRepaymentScheduleDAO scheduleDAO;


    @Inject
    private InterestCalculator interestCalculator;



    @Transactional(rollbackOn = Exception.class)
    public void execute(Long loanId) {


        log.info(
                "Generating repayment schedule for loan: {}",
                loanId
        );


        Loan loan =
                loanDAO.findById(loanId);



        if (loan == null) {

            throw new LoanNotFoundException(
                    "Loan not found with ID: "
                            + loanId
            );
        }



        List<LoanRepaymentSchedule> schedules =
                interestCalculator.calculateSchedule(

                        loan.getLoanId(),

                        loan.getPrincipalAmount(),

                        loan.getInterestRate(),

                        loan.getTenureMonths(),

                        loan.getDisbursedAt()
                                .toLocalDate()
                );



        for (LoanRepaymentSchedule schedule : schedules) {

            scheduleDAO.save(schedule);

        }



        log.info(
                "Generated {} repayment schedules for loan {}",
                schedules.size(),
                loanId
        );

    }

}