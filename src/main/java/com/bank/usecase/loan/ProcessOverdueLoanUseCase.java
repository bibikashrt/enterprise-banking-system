package com.bank.usecase.loan;


import com.bank.dao.LoanRepaymentScheduleDAO;
import com.bank.entity.LoanRepaymentSchedule;
import com.bank.enums.ScheduleStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@ApplicationScoped
public class ProcessOverdueLoanUseCase {


    @Inject
    private LoanRepaymentScheduleDAO scheduleDAO;


    @Transactional(rollbackOn = Exception.class)
    public int execute() {


        LocalDate today =
                LocalDate.now();


        log.info(
                "Starting overdue loan schedule processing. Date: {}",
                today
        );


        List<LoanRepaymentSchedule> overdueSchedules =
                scheduleDAO.findOverdueSchedules(
                        today
                );

        if (overdueSchedules.isEmpty()) {

            log.info(
                    "No overdue loan schedules found."
            );

            return 0;
        }

        int overdueCount = 0;


        for (LoanRepaymentSchedule schedule : overdueSchedules) {


            schedule.setScheduleStatus(
                    ScheduleStatus.OVERDUE
            );


            int updated =
                    scheduleDAO.updateStatus(
                            schedule
                    );


            if (updated > 0) {


                overdueCount++;


                log.info(
                        "Repayment schedule marked overdue. Schedule ID: {}, Loan ID: {}",
                        schedule.getScheduleId(),
                        schedule.getLoanId()
                );

            } else {

                log.warn(
                        "Failed to update overdue schedule. Schedule ID: {}",
                        schedule.getScheduleId()
                );

            }

        }
        log.info(
                "Overdue processing completed. Total overdue schedules: {}",
                overdueCount
        );


        return overdueCount;

    }
}



