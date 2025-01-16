package com.currency_converter.exception;

public class InvalidCurrencyException extends RuntimeException {
	public InvalidCurrencyException(String message) {
		super(message);
	}
}
