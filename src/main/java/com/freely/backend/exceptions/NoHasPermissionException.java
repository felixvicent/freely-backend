package com.freely.backend.exceptions;

public class NoHasPermissionException extends RuntimeException {
  public NoHasPermissionException(String message) {
    super(message);
  }
}
