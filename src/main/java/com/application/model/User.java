package com.application.model;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jdUser")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")

    private LocalDate birthday;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")

    private LocalDate joinDate;
    private Boolean locked;
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Group group;

    // Tinem evidenta persoanelor la ale caror evenimente un user doreste sa participe
    @ManyToMany
    @JoinTable(name="participates_for",
    joinColumns = @JoinColumn(name="participant_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="celebrated_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<User> participationList;

    @ManyToMany(mappedBy = "participationList")
    @JsonIgnore
    private List<User> participantsList;

    @ManyToMany
    @JoinTable(name="joins",
    joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Event> joinedEvents;

    @OneToMany(mappedBy = "collectorUser", cascade = CascadeType.ALL)
    @JsonManagedReference("ledEvents")
    private List<Event> ledEvents;

    @OneToMany(mappedBy = "celebratedUser", cascade = CascadeType.ALL)
    @JsonManagedReference("celebratedEvents")
    private List<Event> celebratedEvents;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Metode la care se adauga si getUsername() si getPassword(),
    // fiind generate de Lombok

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId().equals(user.getId())
                && getUsername().equals(user.getUsername())
                && getEmail().equals(user.getEmail())
                && getPassword().equals(user.getPassword())
                && getLastName().equals(user.getLastName())
                && getFirstName().equals(user.getFirstName())
                && getBirthday().equals(user.getBirthday())
                && getJoinDate().equals(user.getJoinDate())
                && Objects.equals(getLocked(), user.getLocked())
                && Objects.equals(isEnabled(), user.isEnabled())
                && getUserRole() == user.getUserRole()
                && Objects.equals(getGroup().getId(), user.getGroup().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail(), getPassword(), getLastName(), getFirstName(), getBirthday(), getJoinDate(), getLocked(), isEnabled(), getUserRole(), (getGroup() != null ? getGroup().getId() : null));
    }

    @JsonProperty("group")
    public Group getGroup() {
        return group != null ? group : null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthday=" + birthday +
                ", joinDate=" + joinDate +
                ", locked=" + locked +
                ", enabled=" + enabled +
                ", userRole=" + userRole +
                ", groupId=" + (group != null ? group.getId().toString() : "null") +
                '}';
    }
}
