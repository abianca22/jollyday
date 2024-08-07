package com.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jdGroup")
public class Group implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message="Denumirea este obligatorie")
    @Max(value = 20, message = "Denumirea nu trebuie sa depaseasca 20 de caractere")
    @Min(value = 1, message = "Denumirea trebuie sa contina cel putin un caracter")
    @Column(unique = true)
    private String name;
    @Max(value = 200, message = "Denumirea nu trebuie sa depaseasca 200 de caractere")
    private String description;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "leader_id", nullable = true)
    @JsonBackReference("groupLeader")
    private User leader;

    // lista de persoane care apartin de acest grup
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonManagedReference("groupUsers")
    @JsonIgnore
    private List<User> usersList;

    // lista de evenimente pentru care strangerea de fonduri s-a organizat in acest grup
    @OneToMany(mappedBy = "collectingPlace", cascade = CascadeType.ALL)
    @JsonManagedReference("groupEvents")
    @JsonIgnore
    private List<Event> eventsList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group group)) return false;
        return getId().equals(group.getId()) && getName().equals(group.getName()) && getDescription().equals(group.getDescription()) && getLeader().getId().equals(group.getLeader().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), (getLeader() != null ? getLeader().getId() : null));
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", leaderId=" + (leader != null ? leader.getId() : null) +
                '}';
    }

    @JsonGetter("leaderId")
    public Integer getLeaderId() {
        return leader != null ? leader.getId() : null;
    }
}
