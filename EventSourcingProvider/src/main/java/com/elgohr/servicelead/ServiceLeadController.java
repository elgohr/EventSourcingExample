package com.elgohr.servicelead;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ServiceLeadController {

    public final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private ServiceLeadRepository serviceLeadRepository;

    @Autowired
    public ServiceLeadController(ServiceLeadRepository serviceLeadRepository) {
        this.serviceLeadRepository = serviceLeadRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void publishServiceLead(
            @RequestBody ServiceLead serviceLead) {
        serviceLeadRepository.save(serviceLead);
        LOGGER.info("Saved:" + serviceLead.toString());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String getFeed() throws FeedException {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");

        Iterable<ServiceLead> serviceLeads = serviceLeadRepository.findAll();

        List<SyndEntry> entries = new ArrayList();
        serviceLeads.forEach(it -> {
            SyndEntryImpl entry = new SyndEntryImpl();
            entry.setAuthor("PublishingSystem");
            entry.setPublishedDate(it.getPublished());
            entry.setTitle(it.getId().toString());
            entry.setLink("http://localhost:8080/lead/" + it.getId());
            entries.add(entry);
        });
        feed.setEntries(entries);

        return new SyndFeedOutput().outputString(feed);
    }

    @RequestMapping(
            path = "lead/{leadId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ServiceLead getLeadDetail(
            @PathVariable("leadId") UUID leadID) throws FeedException {
        return serviceLeadRepository.findOne(leadID);
    }

    @PostConstruct
    public void prefilDatabase() {
        ServiceLead lead = new ServiceLead();
        lead.setId(UUID.randomUUID());
        lead.setFirstName("Tom");
        lead.setLastName("Tester");
        lead.setMileage(2);
        lead.setTriggerEvent("Breakdown");
        serviceLeadRepository.save(lead);

        ServiceLead lead2 = new ServiceLead();
        lead2.setId(UUID.randomUUID());
        lead2.setFirstName("Mr");
        lead2.setLastName("T");
        lead2.setMileage(20);
        lead2.setTriggerEvent("Explosion");
        serviceLeadRepository.save(lead);
    }

}
