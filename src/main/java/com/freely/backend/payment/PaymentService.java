package com.freely.backend.payment;

import com.freely.backend.payment.payment_asaas.PaymentAsaasService;
import com.freely.backend.payment.payment_asaas.dto.PaymentAsaasDTO;
import com.freely.backend.project.Project;
import com.freely.backend.project.ProjectRepository;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.payment.dto.PaymentCallbackDTO;
import com.freely.backend.web.payment.dto.PaymentDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentAsaasService paymentAsaasService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public PaymentDTO generate(UUID projectId, UserAccount userAccount){
        Project project = projectRepository.findByIdAndCompany(projectId, userAccount).orElseThrow();

        Payment payment = new Payment();
        payment.setProject(project);
        payment.setClient(project.getClient());
        payment.setValue(project.getValue());
        payment.setStatus(PaymentStatusEnum.PENDING);

        Payment createdPayment = paymentRepository.save(payment);

        PaymentAsaasDTO paymentAsaasDTO = paymentAsaasService.generate(project, createdPayment);

        Payment updatedPayment = paymentRepository.findById(UUID.fromString(paymentAsaasDTO.getExternalReference())).orElseThrow();

        updatedPayment.setInvoiceUrl(paymentAsaasDTO.getInvoiceUrl());

        return entityToDTO(paymentRepository.save(updatedPayment));
    }

    public void updatePayment(PaymentCallbackDTO paymentCallbackDTO){
        Payment payment = paymentRepository.findById(UUID.fromString(paymentCallbackDTO.getPayment().getExternalReference())).orElseThrow();

        switch (paymentCallbackDTO.getEvent()){
            case "PAYMENT_CONFIRMED":
                payment.setStatus(PaymentStatusEnum.CONFIRMED);
                break;
            case "PAYMENT_OVERDUE":
                payment.setStatus(PaymentStatusEnum.OVERDUE);
                break;
        }

        paymentRepository.save(payment);
    }

    private PaymentDTO entityToDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .value(payment.getValue())
                .status(payment.getStatus())
                .invoiceUrl(payment.getInvoiceUrl())
                .updatedAt(payment.getUpdatedAt())
                .createdAt(payment.getCreatedAt())
                .client(ClientListDTO.builder()
                        .id(payment.getClient().getId())
                        .name(payment.getClient().getName())
                        .build())
                .project(ProjectDTO.builder()
                        .id(payment.getProject().getId())
                        .value(payment.getProject().getValue())
                        .title(payment.getProject().getTitle())
                        .build() )
                .build();
    }
}
