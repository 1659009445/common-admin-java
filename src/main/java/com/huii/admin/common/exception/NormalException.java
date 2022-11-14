package com.huii.admin.common.exception;

import com.huii.admin.common.enums.ResultCode;
import lombok.Data;

@Data
public class NormalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    protected String errorCode;

    protected String errorMsg;

    public NormalException(){
        super();
    }

    public NormalException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public NormalException(String errorCode, String errorMsg){
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NormalException(String errorCode, String errorMsg, Throwable cause){
        super(errorCode,cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NormalException(ResultCode resultCode){
        super(resultCode.getCode());
        this.errorCode = resultCode.getCode();
        this.errorMsg = resultCode.getMessage();
    }

    public NormalException(ResultCode resultCode, Throwable cause){
        super(resultCode.getCode(),cause);
        this.errorCode = resultCode.getCode();
        this.errorMsg = resultCode.getMessage();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
