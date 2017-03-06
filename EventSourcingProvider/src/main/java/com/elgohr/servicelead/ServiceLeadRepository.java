package com.elgohr.servicelead;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ServiceLeadRepository extends CrudRepository<ServiceLead, UUID>{
}
