package cn.edu.sustech.cs309.controller;

import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "登录注册页面")
@RestController
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public ResponseResult<?> register(@RequestParam(value = "username") String username,
                                               @RequestParam(value = "password") String password) {
        if (!username.matches("[A-z0-9]{6,}")) {
            throw new RuntimeException("Username should be a combination of letters and digits with length longer than 5");
        }
        if (!password.matches("[A-z0-9]{6,16}")) {
            throw new RuntimeException("Password length is 6-16 digits or letters");
        }
        return ResponseResult.success(accountService.createAccount(username, password));
    }

    @ApiOperation(value="用户登录")
    @PostMapping("/login")
    public ResponseResult<?>login(@RequestParam(value="username")String username,
                                  @RequestParam(value="password")String password){
        return ResponseResult.success(accountService.checkAccount(username,password));
    }

    @ApiOperation(value="修改密码")
    @PostMapping("/password")
    public ResponseResult<?>updatePassword(@RequestParam(value="username")String username,
                                  @RequestParam(value="old_password")String oldPassword,
                                  @RequestParam(value="new_password")String newPassword){
        if (!newPassword.matches("[A-z0-9]{6,16}")) {
            throw new RuntimeException("New password length is 6-16 digits or letters");
        }
        return ResponseResult.success(accountService.updatePassword(username,oldPassword,newPassword));
    }
}
