package cn.edu.sustech.cs309.exception;

import cn.edu.sustech.cs309.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseResult<Object> runtimeExceptionHandler(Exception e){
        log.error(e.getMessage());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(value = {AuthenticationException.class, UsernameNotFoundException.class})
    public ResponseResult<Object> authenticationExceptionHandler(AuthenticationException e) {
        log.error(e.getMessage());
        return ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}
