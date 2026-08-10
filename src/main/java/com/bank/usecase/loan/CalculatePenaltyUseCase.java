package com.bank.usecase.loan;


import com.bank.dao.LoanPenaltyDAO;
import com.bank.entity.LoanPenalty;
import com.bank.entity.LoanRepaymentSchedule;

import com.bank.enums.PenaltyType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;


@Slf4j
@ApplicationScoped
public class CalculatePenaltyUseCase {


    @Inject
    private LoanPenaltyDAO penaltyDAO;



    private static final BigDecimal PENALTY_RATE =
            new BigDecimal("12.00");



    @Transactional(rollbackOn = Exception.class)
    public void execute(
            LoanRepaymentSchedule schedule
    ) {


        log.info(
                "Calculating penalty for schedule ID: {}",
                schedule.getScheduleId()
        );



        // Prevent duplicate penalty creation

        LoanPenalty existingPenalty =
                penaltyDAO.findByScheduleId(
                        schedule.getScheduleId()
                );


        if(existingPenalty != null){

            log.info(
                    "Penalty already exists for schedule ID: {}",
                    schedule.getScheduleId()
            );

            return;
        }



        LocalDate today =
                LocalDate.now(
                        ZoneId.of("Asia/Kathmandu")
                );



        long overdueDays =
                ChronoUnit.DAYS.between(
                        schedule.getDueDate(),
                        today
                );



        if(overdueDays <= 0){

            log.info(
                    "Schedule is not overdue. Skipping penalty."
            );

            return;
        }



        /*
         Formula:

         Penalty =
         Total Installment Amount
         *
         Annual Penalty Rate
         *
         Overdue Days
         /
         365

        */


        BigDecimal penaltyAmount =
                schedule.getTotalAmount()
                        .multiply(
                                PENALTY_RATE
                                        .divide(
                                                BigDecimal.valueOf(100),
                                                2,
                                                RoundingMode.HALF_UP
                                        )
                        )
                        .multiply(
                                BigDecimal.valueOf(overdueDays)
                        )
                        .divide(
                                BigDecimal.valueOf(365),
                                2,
                                RoundingMode.HALF_UP
                        );



        LoanPenalty penalty =
                LoanPenalty.builder()
                        .loanId(
                                schedule.getLoanId()
                        )
                        .scheduleId(
                                schedule.getScheduleId()
                        )
                        .penaltyAmount(
                                penaltyAmount
                        )
                        .penaltyType(
                                PenaltyType.LATE_PAYMENT_FEE
                        )
                        .overdueDays(
                                (int) overdueDays
                        )
                        .penaltyRate(
                                PENALTY_RATE
                        )
                        .calculatedDate(
                                today
                        )
                        .paid(false)
                        .build();



        penaltyDAO.save(
                penalty
        );



        log.info(
                "Penalty created. Schedule ID: {}, Amount: {}",
                schedule.getScheduleId(),
                penaltyAmount
        );

    }

}