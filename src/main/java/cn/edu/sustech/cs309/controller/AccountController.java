package cn.edu.sustech.cs309.controller;

import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.dto.AccountDTO;
import cn.edu.sustech.cs309.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseResult<AccountDTO> register(@RequestParam(value = "username") String username,
                                               @RequestParam(value = "password") String password) {
        if (!username.matches("[A-z0-9]{6,}")) {
            throw new RuntimeException("Username should be a combination of letters and digits with length longer than 5");
        }
        if (!password.matches("[A-z0-9]{6,16}")) {
            throw new RuntimeException("Password length is 6-16 digits or letters");
        }
        return accountService.createAccount(username, password);
    }


}
