package br.com.zupacademy.proposta.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValueConstraint.class)
public @interface UniqueValue {

	String message() default "O valor informado já foi cadastrado!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String field();

	Class<?> domain();

}
