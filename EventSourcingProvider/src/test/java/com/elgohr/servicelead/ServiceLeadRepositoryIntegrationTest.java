package com.elgohr.servicelead;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class ServiceLeadRepositoryIntegrationTest {

    @Autowired
    private ServiceLeadRepository serviceLeadRepository;

    @Test
    public void shouldSaveAndLoadLeads() throws Exception {
        ServiceLead lead = new ServiceLead();
        lead.setId(UUID.randomUUID());
        lead.setFirstName("Tommy");
        lead.setLastName("Test");
        lead.setMileage(2);
        lead.setTriggerEvent("test");
        serviceLeadRepository.save(lead);

        assertEquals(1, serviceLeadRepository.findAll().spliterator().estimateSize());
    }
}
