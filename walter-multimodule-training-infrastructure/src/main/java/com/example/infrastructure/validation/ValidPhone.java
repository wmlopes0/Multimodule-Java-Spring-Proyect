package com.example.infrastructure.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

  String message() default "Invalid phone format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}