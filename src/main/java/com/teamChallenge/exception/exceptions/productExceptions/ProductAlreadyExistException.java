package com.teamChallenge.exception.exceptions.productExceptions;

import java.util.UUID;

public class ProductAlreadyExistException extends Exception{
    private static final String PRODUCT_ALREADY_EXIST_EXCEPTION_ID = "Product with id = %s already exist.";
    private static final String PRODUCT_ALREADY_EXIST_EXCEPTION_NAME = "Product with name = %s already exist.";

    public ProductAlreadyExistException(UUID id) {
        super(String.format(PRODUCT_ALREADY_EXIST_EXCEPTION_ID, id));
    }

    public ProductAlreadyExistException(String name) {
        super(String.format(PRODUCT_ALREADY_EXIST_EXCEPTION_NAME, name));
    }
}
