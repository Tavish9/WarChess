package cn.edu.sustech.cs309.exception;

import cn.edu.sustech.cs309.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseResult<Object> exceptionHandler(Exception e){
        log.error(e.getMessage());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
