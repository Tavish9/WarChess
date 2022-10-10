package cn.edu.sustech.cs309.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ResponseResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public static <T> ResponseResult<T> success(Integer code, T data) {
        return ResponseResult.<T>builder().code(code).data(data).build();
    }

    public static <T> ResponseResult<T> success(T data) {
        return success(HttpStatus.OK.value(), data);
    }

    public static <T> ResponseResult<T> fail(Integer code, String msg) {
        return ResponseResult.<T>builder().code(code).msg(msg).build();
    }
}
