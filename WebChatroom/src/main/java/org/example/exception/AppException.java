package org.example.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常：保存错误码和错误消息
 */
@Getter
@Setter
public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
