package com.freely.backend.integration.asaas;

import com.freely.backend.user.UserAccount;
import com.freely.backend.user.user_properties.UserProperty;
import com.freely.backend.user.user_properties.UserPropertyDomainEnum;
import com.freely.backend.user.user_properties.UserPropertyService;
import com.freely.backend.user.user_properties.dto.UserPropertyDTO;
import com.freely.backend.web.integrations.asaas.dto.IntegrationAsaasForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IntegrationAsaasService {
    @Autowired
    private UserPropertyService userPropertyService;

    public UserPropertyDTO get(UserAccount userAccount){
        List<UserPropertyDTO> properties = userPropertyService.getByDomain(userAccount, UserPropertyDomainEnum.ASAAS)
                .stream().map(userProperty -> userPropertyService.entityToDTO(userProperty)).toList();

        if(!properties.isEmpty()) {
            return properties.get(0);
        }

        return null;
    }

    public UserPropertyDTO save(UserAccount userAccount, IntegrationAsaasForm integrationAsaasForm){
        List<UserProperty> property = userPropertyService.getByDomain(userAccount, UserPropertyDomainEnum.ASAAS);

        UUID propertyId = null;

        if(!property.isEmpty()) {
            propertyId = property.get(0).getId();
        }
        return userPropertyService.save(userAccount, UserPropertyDomainEnum.ASAAS, "apiKey", integrationAsaasForm.getApiKey(), propertyId);
    }
}
