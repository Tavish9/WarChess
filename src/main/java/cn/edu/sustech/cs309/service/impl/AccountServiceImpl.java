package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.service.AccountService;
import cn.edu.sustech.cs309.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ResponseResult<Account> saveAccount(Account account) {
        Account account1 = accountRepository.findAccountByUsername(account.getUsername());
        if (account1 != null) {
            throw new RuntimeException("Account already exists");
        } else {
            log.debug("create successfully");
            return ResponseResult.success(HttpStatus.OK.value(), accountRepository.save(account));
        }
    }
}
