
package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "[user]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "emri")
    private String name;

    @Column(name = "mbiemri")
    private String mbiemri;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_sectors",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"user_id", "sector_id", "role"}
            )
    )
    @JsonIgnore
    private Set<Sector> sectors = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private List<String> lejet = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole role;
    public void setSector(Sector sector) {
        if (this.role == UserRole.CASHIER && sector != null) {
            this.sectors.add(sector);
            //sector.getUsers().add(this);
            if (sector.getUsers() != null) {
                sector.getUsers().add(this);
            }
        }
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emri='" + name + '\'' +
                ", mbiemri='" + mbiemri + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lejet=" + lejet +
                '}';
    }
}