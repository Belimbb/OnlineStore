package com.teamChallenge.exception.exceptions.productExceptions;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException{
    private static final String PRODUCT_NOT_FOUND_EXCEPTION_TEXT = "Product not found";
    private static final String PRODUCT_WITH_NAME_NOT_FOUND_EXCEPTION_TEXT = "Product with name = %s not found.";
    private static final String PRODUCT_WITH_ID_NOT_FOUND_EXCEPTION_TEXT = "Product with id = %s not found.";


    public ProductNotFoundException() {
        super(PRODUCT_NOT_FOUND_EXCEPTION_TEXT);
    }

    public ProductNotFoundException(String name) {
        super(String.format(PRODUCT_WITH_NAME_NOT_FOUND_EXCEPTION_TEXT, name));
    }

    public ProductNotFoundException(UUID id) {
        super(String.format(PRODUCT_WITH_ID_NOT_FOUND_EXCEPTION_TEXT, id));
    }
}