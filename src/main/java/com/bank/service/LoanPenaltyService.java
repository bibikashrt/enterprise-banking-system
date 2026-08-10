package com.bank.service;


import com.bank.dto.response.LoanPenaltyResponse;

import java.util.List;


public interface LoanPenaltyService {


    List<LoanPenaltyResponse> getPenaltiesByLoan(
            Long loanId
    );

}