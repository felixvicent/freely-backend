package com.freely.backend.user.user_properties;

import com.freely.backend.user.UserAccount;
import com.freely.backend.user.user_properties.dto.UserPropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserPropertyService {
    @Autowired
    private UserPropertyRepository userPropertyRepository;

    public List<UserProperty> getByDomain(UserAccount userAccount, UserPropertyDomainEnum domain){
        return userPropertyRepository.findByCompanyAndDomain(userAccount.getCompany(), domain).stream().toList();
    }

    public UserPropertyDTO save(UserAccount userAccount, UserPropertyDomainEnum domain, String key, String value, UUID id){
        UserProperty userProperty = new UserProperty();

        userProperty.setCompany(userAccount.getCompany());
        userProperty.setDomain(domain);
        userProperty.setKey(key);
        userProperty.setValue(value);

        if(id != null) {
            userProperty.setId(id);
        }

        return entityToDTO(userPropertyRepository.save(userProperty));
    }

    public UserPropertyDTO entityToDTO(UserProperty userProperty){
        return UserPropertyDTO.builder()
                .id(userProperty.getId())
                .domain(userProperty.getDomain())
                .key(userProperty.getKey())
                .value(userProperty.getValue())
                .build();
    }
}
