package com.bank.usecase;

public interface UseCase<R, P> {
    R execute(P param);
}