package br.com.quintinno.securityapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quintinno.securityapi.service.SignupService;
import br.com.quintinno.securityapi.transfer.SignupRequestTransfer;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequestTransfer signupRequestTransfer) {
        try {
            return ResponseEntity.ok(this.signupService.signup(signupRequestTransfer));
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(runtimeException.getMessage());
        }
    }

}
