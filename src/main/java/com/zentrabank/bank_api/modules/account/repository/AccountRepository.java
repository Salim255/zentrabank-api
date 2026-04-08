package com.zentrabank.bank_api.modules.account.repository;

import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> { }