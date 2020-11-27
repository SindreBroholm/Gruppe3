package com.fastis.validator;

import com.fastis.data.Event;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.time.LocalDateTime;

    public class EventValidator implements org.springframework.validation.Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty", "Forgot to set a name for the event?");
        ValidationUtils.rejectIfEmpty(errors, "datetime_from", "datetime_from.empty", "Forgot to set starting day and time?");
        ValidationUtils.rejectIfEmpty(errors, "datetime_to", "datetime_to.empty", "Forgot to set ending day and time?");
        Event e = (Event) object;

        if (e.getDatetime_from().isBefore(LocalDateTime.now())){
            errors.rejectValue("datetime_from", "negativeValue", "Event must be set to the present or in the future.");
        }
        if (e.getDatetime_to().isBefore(e.getDatetime_from())){
            errors.rejectValue("datetime_to", "negativeValue", "Event cant end before it starts");
        }
    }
}
