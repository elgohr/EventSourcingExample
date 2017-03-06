package com.elgohr.servicelead;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceLeadIntegrationTest {

    public final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private ServiceLead validServiceLead;

    @MockBean
    private ServiceLeadRepository serviceLeadRepository;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        validServiceLead = new ServiceLead(
                "Test",
                "Tommy",
                10,
                "Breakdown");
        List response = new ArrayList<ServiceLead>();
        response.add(validServiceLead);
        when(serviceLeadRepository.findAll()).thenReturn(response);
    }

    @Test
    public void shouldAcceptAndSaveServiceEvents() throws Exception {
        String jsonServiceLead = new ObjectMapper().writeValueAsString(validServiceLead);
        LOGGER.info(jsonServiceLead);
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonServiceLead))
                .andExpect(status().isCreated());
        verify(serviceLeadRepository).save(any(ServiceLead.class));
    }

    @Test
    public void shouldPublishFeedWithServiceEvents() throws Exception {
        String content = mockMvc.perform(get("/"))
                .andReturn().getResponse().getContentAsString();

        assertTrue(content.contains("<link rel=\"alternate\" href=\"http://localhost:8080/lead/"));
        assertTrue(content.contains("<published>"));
        assertTrue(content.contains("<updated>"));
        assertTrue(content.contains("<dc:creator>PublishingSystem</dc:creator>"));
        assertTrue(content.contains("<dc:date>"));
        assertTrue(content.contains("<name>PublishingSystem</name>"));
        assertTrue(content.contains("<title>"));
    }
}
