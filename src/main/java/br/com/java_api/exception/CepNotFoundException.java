package br.com.java_api.exception;

public class CepNotFoundException extends RuntimeException {
    
    public CepNotFoundException(String message) {
        super(message);
    }
    
    public CepNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}