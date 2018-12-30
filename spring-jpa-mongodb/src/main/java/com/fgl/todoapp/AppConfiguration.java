package com.fgl.todoapp;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@org.springframework.context.annotation.Configuration
public class AppConfiguration {

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }

}
