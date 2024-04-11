package com.freely.backend.payment;

import com.freely.backend.payment.payment_asaas.PaymentAsaasService;
import com.freely.backend.payment.payment_asaas.dto.PaymentAsaasDTO;
import com.freely.backend.project.Project;
import com.freely.backend.project.ProjectRepository;
import com.freely.backend.user.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentAsaasService paymentAsaasService;

    @Autowired
    private ProjectRepository projectRepository;

    public PaymentAsaasDTO generate(UUID projectId, UserAccount userAccount){
        Project project = projectRepository.findByIdAndCompany(projectId, userAccount).orElseThrow();

        return paymentAsaasService.generate(project);
    }
}
