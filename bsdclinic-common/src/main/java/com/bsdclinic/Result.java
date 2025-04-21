package com.bsdclinic;

import java.io.Serializable;
import java.util.Map;
import lombok.Generated;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 8441050065523394019L;
    private final T data;
    private final Integer errorCode;
    private final String message;
    private final Map<String, String> errors;

    private Result(T data, Integer errorCode, String message, Map<String, String> errors) {
        this.data = data;
        this.errorCode = errorCode;
        this.message = message;
        this.errors = errors;
    }

    public static <T> SuccessBuilder<T> success() {
        return new DefaultBuilder<>();
    }

    public static <T> FailureBuilder<T> fail() {
        return new DefaultBuilder<>();
    }

    @Generated
    public T getData() {
        return this.data;
    }

    @Generated
    public Integer getErrorCode() {
        return this.errorCode;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public Map<String, String> getErrors() {
        return this.errors;
    }

    private static class DefaultBuilder<T> implements SuccessBuilder<T>, FailureBuilder<T> {
        protected T data;
        protected Integer errorCode;
        protected String message;
        protected Map<String, String> errors;

        private DefaultBuilder() {
        }

        public SuccessBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public FailureBuilder<T> errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public FailureBuilder<T> errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public Result<T> build() {
            return new Result<>(this.data, this.errorCode, this.message, this.errors);
        }
    }

    public interface FailureBuilder<T> extends ResultBuilder<T> {
        FailureBuilder<T> errorCode(Integer errorCode);

        FailureBuilder<T> errors(Map<String, String> errors);
    }

    public interface ResultBuilder<T> {
        ResultBuilder<T> message(String message);

        Result<T> build();
    }

    public interface SuccessBuilder<T> extends ResultBuilder<T> {
        SuccessBuilder<T> data(T data);
    }
}