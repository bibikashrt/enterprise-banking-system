package com.bank.service.interest;


import com.bank.entity.LoanRepaymentSchedule;
import com.bank.enums.ScheduleStatus;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class InterestCalculatorImpl
        implements InterestCalculator {



    @Override
    public List<LoanRepaymentSchedule> calculateSchedule(

            Long loanId,

            BigDecimal principalAmount,

            BigDecimal annualInterestRate,

            Integer tenureMonths,

            LocalDate startDate
    ) {


        List<LoanRepaymentSchedule> schedules =
                new ArrayList<>();


        BigDecimal monthlyRate =
                annualInterestRate
                        .divide(
                                BigDecimal.valueOf(12),
                                10,
                                RoundingMode.HALF_UP
                        )
                        .divide(
                                BigDecimal.valueOf(100),
                                10,
                                RoundingMode.HALF_UP
                        );


        BigDecimal emi =
                calculateEMI(
                        principalAmount,
                        monthlyRate,
                        tenureMonths
                );


        BigDecimal remainingBalance =
                principalAmount;



        for(int i = 1; i <= tenureMonths; i++) {


            BigDecimal interest =
                    remainingBalance
                            .multiply(monthlyRate)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );


            BigDecimal principal =
                    emi.subtract(interest);



            if(principal.compareTo(remainingBalance) > 0) {

                principal = remainingBalance;
            }



            LoanRepaymentSchedule schedule =
                    LoanRepaymentSchedule.builder()

                            .loanId(loanId)

                            .installmentNumber(i)

                            .dueDate(
                                    startDate
                                            .plusMonths(i)
                            )

                            .principalAmount(principal)

                            .interestAmount(interest)

                            .totalAmount(
                                    principal.add(interest)
                            )

                            .scheduleStatus(
                                    ScheduleStatus.PENDING
                            )

                            .build();



            schedules.add(schedule);



            remainingBalance =
                    remainingBalance
                            .subtract(principal);

        }


        return schedules;

    }




    private BigDecimal calculateEMI(

            BigDecimal principal,

            BigDecimal monthlyRate,

            int months
    ) {


        double p =
                principal.doubleValue();


        double r =
                monthlyRate.doubleValue();


        double n =
                months;



        double emi =
                (p * r *
                        Math.pow(1 + r, n))
                        /
                        (Math.pow(1 + r, n) - 1);



        return BigDecimal.valueOf(emi)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );

    }

}