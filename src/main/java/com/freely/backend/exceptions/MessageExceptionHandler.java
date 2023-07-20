package com.freely.backend.exceptions;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageExceptionHandler {
  private Date timestamp;
  private Integer code;
  private String message;

}
