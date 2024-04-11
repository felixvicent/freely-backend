package com.freely.backend.web.payment;

import com.freely.backend.payment.PaymentService;
import com.freely.backend.user.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{projectId}")
    public ResponseEntity<?> generate(@AuthenticationPrincipal UserAccount userAccount,
                                      @PathVariable UUID projectId){
            return ResponseEntity.ok(paymentService.generate(projectId, userAccount));
    }
}
