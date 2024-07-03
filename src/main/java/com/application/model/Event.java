package com.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jdEvent")
public class
Event implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "celebrated_user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonBackReference("celebratedEvents")
    private User celebratedUser;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonBackReference("ledEvents")
    private User collectorUser;

    private Double collectedAmount = 100.d;

    // in principiu, collectingPlace trebuie sa fie grupul de care apartine colectorul
    // daca exista un singur grup, atunci va exista un grup de "rezerva" (de exemplu, in cadrul unei firme,
    // poate fi reprezentat de o sala goala, unde nu lucreaza nimeni)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collecting_place_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonBackReference("groupEvents")
    private Group collectingPlace;

    @ManyToMany(mappedBy = "joinedEvents", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<User> participants;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate creationDate;

    @JsonGetter("collectingPlaceName")
    public String getCollectingPlaceName() {
        return this.collectingPlace != null ? this.collectingPlace.getName() : null;
    }

    @JsonGetter("celebratedName")
    public String getCelebratedName() {
        return this.celebratedUser != null ? this.celebratedUser.getUsername() : null;
    }

    @JsonGetter("collectorName")
    public String getCollectorName() {
        return this.collectorUser != null ? this.collectorUser.getUsername() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return getId().equals(event.getId()) && getCelebratedUser().equals(event.getCelebratedUser()) && getCollectorUser().equals(event.getCollectorUser()) && getCollectedAmount().equals(event.getCollectedAmount()) && Objects.equals(getCollectingPlace(), event.getCollectingPlace()) && getCreationDate().equals(event.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCelebratedUser(), getCollectorUser(), getCollectedAmount(), getCollectingPlace(), getCreationDate());
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", celebratedUser=" + celebratedUser +
                ", collectorUser=" + collectorUser +
                ", collectedAmount=" + collectedAmount +
                ", eventDate=" + creationDate +
                '}';
    }
}
