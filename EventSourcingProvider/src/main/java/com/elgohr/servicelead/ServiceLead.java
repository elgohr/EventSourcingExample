package com.elgohr.servicelead;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class ServiceLead {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private Date published;
    private String lastName;
    private String firstName;
    private long mileage;
    private String triggerEvent;

    public ServiceLead() {
        // WHY JPA, WHY?
    }

    public ServiceLead(String lastName,
                       String firstName,
                       long mileage,
                       String triggerEvent) {
        this.id = UUID.randomUUID();
        this.published = new Date();
        this.lastName = lastName;
        this.firstName = firstName;
        this.mileage = mileage;
        this.triggerEvent = triggerEvent;
    }

    @Override
    public String toString() {
        return "ServiceLead{" +
                "id=" + id +
                ", published=" + published +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", mileage=" + mileage +
                ", triggerEvent='" + triggerEvent + '\'' +
                '}';
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }
}
