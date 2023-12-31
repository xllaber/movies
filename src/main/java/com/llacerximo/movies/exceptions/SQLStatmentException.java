package com.llacerximo.movies.exceptions;

public class SQLStatmentException extends RuntimeException{
    private static final String DESCRIPTION = "Error executing SQL statement";

    public SQLStatmentException(String message) {
        super(DESCRIPTION + ". " + message);
    }
}
