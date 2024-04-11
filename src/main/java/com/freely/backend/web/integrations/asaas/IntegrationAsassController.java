package com.freely.backend.web.integrations.asaas;

import com.freely.backend.integration.asaas.IntegrationAsaasService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.integrations.asaas.dto.IntegrationAsaasForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrations/asaas")
public class IntegrationAsassController {
    @Autowired
    private IntegrationAsaasService integrationAsassService;

    @GetMapping
    public ResponseEntity<?> index(@AuthenticationPrincipal UserAccount userAccount){

        return ResponseEntity.ok(integrationAsassService.get(userAccount));
    }

    @PostMapping()
    public ResponseEntity<?> save(@AuthenticationPrincipal UserAccount userAccount,
                                    @RequestBody IntegrationAsaasForm integrationAsaasForm){
        return ResponseEntity.ok(integrationAsassService.save(userAccount, integrationAsaasForm));
    }
}
