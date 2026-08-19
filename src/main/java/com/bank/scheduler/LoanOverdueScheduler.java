package com.bank.scheduler;


import com.bank.usecase.loan.ProcessOverdueLoanUseCase;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Singleton
@Startup
public class LoanOverdueScheduler {


    @Inject
    private ProcessOverdueLoanUseCase processOverdueLoanUseCase;



    /**
     * Runs every day at midnight.
     */
    @Schedule(
            hour = "*",
            minute = "*",
            second = "10",
            persistent = false
    )
    public void processOverdueLoans() {


        log.info(
                "Loan overdue scheduler started."
        );


        int processedCount =
                processOverdueLoanUseCase.execute();



        log.info(
                "Loan overdue scheduler completed. Processed schedules: {}",
                processedCount
        );

    }

}