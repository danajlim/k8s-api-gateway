package com.welab.k8s_api_gateway.common.exception;

public class NotFound extends ClientError{

    public NotFound(String message){
        this.errorCode = "Not Found";
        this.errorMessage = message;
    }
}