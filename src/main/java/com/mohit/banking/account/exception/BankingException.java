package com.mohit.banking.account.exception;

/**
 * BankingException - Custom runtime exception for banking business errors.
 * @author mohit
 */
public class BankingException extends RuntimeException {

    /** HTTP status code associated with this exception. */
    private final int statusCode;

    /**
     * Constructs a BankingException with message and HTTP status code.
     * @param message error message
     * @param statusCode HTTP status code
     */
    public BankingException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Constructs a BankingException with message, cause, and HTTP status code.
     * @param message error message
     * @param cause root cause
     * @param statusCode HTTP status code
     */
    public BankingException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code.
     * @return status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
