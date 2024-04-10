package com.freely.backend.user.user_properties;

import com.freely.backend.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPropertyRepository extends JpaRepository<UserProperty, UUID> {

    List<UserProperty> findByCompanyAndDomain(UserAccount company, UserPropertyDomainEnum domain);
}
