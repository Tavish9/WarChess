package cn.edu.sustech.cs309.service;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.dto.AccountDTO;
import org.apache.tomcat.websocket.AuthenticationException;

public interface AccountService {
    ResponseResult<AccountDTO> createAccount(String username, String password);

    ResponseResult<AccountDTO> updatePassword(String username, String oldPassword, String newPassword) throws AuthenticationException;
}
