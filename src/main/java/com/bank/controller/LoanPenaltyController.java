package com.bank.controller;


import com.bank.dto.response.LoanPenaltyResponse;
import com.bank.service.LoanPenaltyService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Path("/loan-penalties")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanPenaltyController {


    @Inject
    private LoanPenaltyService loanPenaltyService;



    @GET
    @Path("/loan/{loanId}")
    public Response getPenaltiesByLoan(
            @PathParam("loanId") Long loanId
    ) {


        log.info(
                "Request received to fetch penalties for loan ID: {}",
                loanId
        );


        List<LoanPenaltyResponse> penalties =
                loanPenaltyService.getPenaltiesByLoan(
                        loanId
                );


        return Response
                .ok(penalties)
                .build();

    }

}