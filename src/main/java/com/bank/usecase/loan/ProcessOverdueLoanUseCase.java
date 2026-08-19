package com.bank.usecase.loan;


import com.bank.dao.LoanRepaymentScheduleDAO;
import com.bank.entity.LoanRepaymentSchedule;
import com.bank.enums.ScheduleStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@Slf4j
@ApplicationScoped
public class ProcessOverdueLoanUseCase {


    @Inject
    private LoanRepaymentScheduleDAO scheduleDAO;

    @Inject
    private CalculatePenaltyUseCase calculatePenaltyUseCase;


    @Transactional(rollbackOn = Exception.class)
    public int execute() {


        LocalDate today =
                LocalDate.now(
                        ZoneId.of("Asia/Kathmandu")
                );


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


            if (schedule.getScheduleStatus() != ScheduleStatus.OVERDUE) {


                schedule.setScheduleStatus(
                        ScheduleStatus.OVERDUE
                );


                int updated =
                        scheduleDAO.updateStatus(schedule);


                if (updated > 0) {

                    overdueCount++;

                    log.info(
                            "Repayment schedule marked overdue. Schedule ID: {}, Loan ID: {}",
                            schedule.getScheduleId(),
                            schedule.getLoanId()
                    );


                    calculatePenaltyUseCase.execute(
                            schedule
                    );


                } else {

                    log.warn(
                            "Failed to update overdue schedule. Schedule ID: {}",
                            schedule.getScheduleId()
                    );

                }


            } else {


                log.info(
                        "Already overdue schedule found. Schedule ID: {}, Loan ID: {}",
                        schedule.getScheduleId(),
                        schedule.getLoanId()
                );

                calculatePenaltyUseCase.execute(schedule);

                overdueCount++;

            }

        }
        log.info(
                "Overdue processing completed. Total overdue schedules: {}",
                overdueCount
        );




        return overdueCount;

    }
}



