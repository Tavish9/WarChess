package cn.edu.sustech.cs309.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public ResponseResult(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data){
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseResult<T> success(Integer code, T data){
        return new ResponseResult<>(code, data);
    }

    public static <T> ResponseResult<T> fail(Integer code, String msg){
        return new ResponseResult<>(code, msg);
    }
}
