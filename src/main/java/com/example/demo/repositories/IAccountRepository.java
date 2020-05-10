package com.example.demo.repositories;

import com.example.demo.repositories.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);

    @Query(value = "SELECT * FROM account acc WHERE acc.email like %:value%", nativeQuery = true)
    List<Account> findAccountByEmailParamsNative(@Param("value") String value);

}
