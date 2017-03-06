package com.elgohr;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class EventSourcingClientApplication {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, String> serviceLeads = new HashMap<>();
    private boolean newLeads = false;

    public static void main(String[] args) {
        SpringApplication.run(EventSourcingClientApplication.class, args);
    }

    @Scheduled(fixedRate = 10000)
    public void getLeads() throws FeedException {
        String feedContent = getFeedContentAsString();
        SyndFeed feed = getFeedFromString(feedContent);
        resetNewLeadsFlag();
        feed.getEntries().forEach(
                it -> {
                    if (!serviceLeads.containsKey(it.getTitle())) {
                        logger.info("New Service Lead was published");
                        String newLead = getContentOfNewLeadAsString(it);
                        logger.info("Adding:" + newLead);
                        serviceLeads.put(it.getTitle(), newLead);
                        newLeads = true;
                    }
                }
        );
        if (!newLeads)
            logger.info("Refreshed. No new Leads");

    }

    private String getContentOfNewLeadAsString(SyndEntry it) {
        return new RestTemplate().getForEntity(it.getLink(), String.class).getBody();
    }

    private void resetNewLeadsFlag() {
        newLeads = false;
    }

    private SyndFeed getFeedFromString(String feedContent) throws FeedException {
        return new SyndFeedInput().build(new InputSource(new StringReader(feedContent)));
    }

    private String getFeedContentAsString() {
        return new RestTemplate().getForEntity("http://localhost:8080/", String.class).getBody();
    }

}
