package com.example.demo.service;

import com.example.demo.repositories.entities.Account;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    Account findByEmail(String email);
    List<Account> findAccountByEmailParamsNative(@Param("value") String value);
    List<Account> findAll();
    Optional<Account> findById(String id);
    Account save(Account account);
    void delete(Account account);
}
