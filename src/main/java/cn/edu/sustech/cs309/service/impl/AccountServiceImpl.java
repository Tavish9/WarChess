package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.MyUserDetails;
import cn.edu.sustech.cs309.dto.AccountDTO;
import cn.edu.sustech.cs309.repository.AccountRepository;
import cn.edu.sustech.cs309.security.MyPasswordEncoder;
import cn.edu.sustech.cs309.service.AccountService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService, UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MyPasswordEncoder passwordEncoder;

    @Override
    public AccountDTO createAccount(String username, String password) {
        Account account = accountRepository.findAccountByUsername(username);
        if (account != null) {
            throw new RuntimeException("Username already exists");
        } else {
            account = Account.builder().username(username).password(passwordEncoder.encode(password)).build();
            log.debug("create account: " + account);
            accountRepository.save(account);
            return DTOUtil.toAccountDTO(account, null);
        }
    }

    @Override
    public AccountDTO updatePassword(String username, String oldPassword, String newPassword) throws AuthenticationException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Account does not exist");
        } else if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new AuthenticationException("Wrong password");
        } else {
            account.setPassword(passwordEncoder.encode(newPassword));
            log.debug("update password:" + account);
            accountRepository.save(account);
            return DTOUtil.toAccountDTO(account, null);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        if (StringUtils.isNotBlank(username))
            account = accountRepository.findAccountByUsername(username);
        else
            throw new RuntimeException("Username should not be empty");
        if (account != null)
            return new MyUserDetails(account);
        else
            throw new UsernameNotFoundException("Account does not exist");
    }
}
