package br.com.anzolin.api.services.exceptions;

public class DataIntegrationViolationException extends RuntimeException{

        public DataIntegrationViolationException(String message){
            super(message);
        }

}
