package com.elgohr.servicelead;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ServiceLeadControllerTest {

    private ServiceLeadController serviceLeadController;
    private ServiceLeadRepository serviceLeadRepository;

    @Before
    public void setUp() throws Exception {
        serviceLeadRepository = mock(ServiceLeadRepository.class);
        serviceLeadController = new ServiceLeadController(serviceLeadRepository);
    }

    @Test
    public void shouldSaveServiceLead() throws Exception {
        ServiceLead lead = new ServiceLead();
        serviceLeadController.publishServiceLead(lead);
        verify(serviceLeadRepository).save(lead);
    }

    @Test
    public void shouldPublishFeed() throws Exception {
        ArrayList<ServiceLead> response = new ArrayList();
        response.add( new ServiceLead(
                "Test",
                "Tommy",
                2,
                "test"));
        when(serviceLeadRepository.findAll()).thenReturn(response);

        String content = serviceLeadController.getFeed();

        verify(serviceLeadRepository).findAll();

        assertTrue(content.contains("<link rel=\"alternate\" href=\"http://localhost:8080/lead/"));
        assertTrue(content.contains("<published>"));
        assertTrue(content.contains("<updated>"));
        assertTrue(content.contains("<dc:creator>PublishingSystem</dc:creator>"));
        assertTrue(content.contains("<dc:date>"));
        assertTrue(content.contains("<name>PublishingSystem</name>"));
        assertTrue(content.contains("<title>"));
    }

    @Test
    public void shouldOfferDetailForServiceLead() throws Exception {
        ServiceLead lead = new ServiceLead(
                "Test",
                "Tommy",
                2,
                "test");
        lead.setId(UUID.fromString("62d692ab-dfd0-49dd-a69b-2624b5a88f83"));
        when(serviceLeadRepository.findOne(any()))
                .thenReturn(lead);

        ServiceLead returnedLead = serviceLeadController
                .getLeadDetail(UUID.fromString("62d692ab-dfd0-49dd-a69b-2624b5a88f83"));
        assertEquals(lead, returnedLead);

        verify(serviceLeadRepository)
                .findOne(eq(UUID.fromString("62d692ab-dfd0-49dd-a69b-2624b5a88f83")));
    }
}