package com.quintor.api.validators;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Validatable {
    public String validateFormat(String input);
}
