package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.dto.AccountDTO;
import cn.edu.sustech.cs309.service.AccountService;
import cn.edu.sustech.cs309.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ResponseResult<AccountDTO> createAccount(String username, String password) {
        Account account1 = accountRepository.findAccountByUsername(username);
        if (account1 != null) {
            throw new RuntimeException("Username already exists");
        } else {
            Account account = Account.builder().username(username).password(password).build();
            log.debug("create account: " + account);
            account = accountRepository.save(account);
            return ResponseResult.success(HttpStatus.OK.value(), new AccountDTO(account.getId(), account.getUsername(), null));
        }
    }

    @Override
    public ResponseResult<AccountDTO> updatePassword(String username, String oldPassword, String newPassword) throws AuthenticationException {
        Account account1 = accountRepository.findAccountByUsername(username);
        if (account1 == null) {
            throw new RuntimeException("Account does not exist");
        } else if (!account1.getPassword().equals(oldPassword)) {
            throw new AuthenticationException("Wrong password");
        } else {
            account1.setPassword(newPassword);
            log.debug("update password:" + account1);
            account1 = accountRepository.save(account1);
            return ResponseResult.success(HttpStatus.OK.value(), new AccountDTO(account1.getId(), account1.getUsername(), null));
        }
    }
}
