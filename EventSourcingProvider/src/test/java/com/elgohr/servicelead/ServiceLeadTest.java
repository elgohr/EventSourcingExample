package com.elgohr.servicelead;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceLeadTest {

    @Test
    public void shouldContainSender() throws Exception {
        ServiceLead serviceLead = new ServiceLead(
                "Test",
                "Tommy",
                10,
                "Breakdown");

        assertEquals("Tommy", serviceLead.getFirstName());
        assertEquals("Test", serviceLead.getLastName());
        assertEquals(10, serviceLead.getMileage());
        assertEquals("Breakdown", serviceLead.getTriggerEvent());
    }
}
