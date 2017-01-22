package com.upconsulting.gilesecosystem.hank.web.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.upconsulting.gilesecosystem.hank.web.forms.ModelForm;

public class ModelFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ModelForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "upload_model_title_missing");
    }

}
