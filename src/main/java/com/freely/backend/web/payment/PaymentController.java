package com.freely.backend.web.payment;

import com.freely.backend.payment.PaymentService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.payment.dto.PaymentCallbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody PaymentCallbackDTO paymentCallbackDTO) {
        paymentService.updatePayment(paymentCallbackDTO);
        return ResponseEntity.ok().build();
    }
}
