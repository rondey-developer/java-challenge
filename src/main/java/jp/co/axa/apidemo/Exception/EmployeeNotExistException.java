package jp.co.axa.apidemo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = " Employee Not Exist")
public class EmployeeNotExistException extends RuntimeException {
}
