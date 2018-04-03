package com.tea.mservice.portal.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Created by liuyihao on 2017/10/18.
 */
public class ValidatingFormException extends RuntimeException {

    private String formErrorsMessage;

    public ValidatingFormException(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder b = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            b.append(fieldError.getDefaultMessage()).append("\n");
        }
        b.deleteCharAt(b.length() - 1);
        formErrorsMessage = b.toString();
    }


    @Override
    public String getMessage() {
        return getFormErrorsMessage();
    }

    public String getFormErrorsMessage() {
        return formErrorsMessage;
    }
}
