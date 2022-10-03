package cn.edu.sustech.cs309.service;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.ResponseResult;

public interface AccountService {
    ResponseResult<Account> saveAccount(Account account);
}
