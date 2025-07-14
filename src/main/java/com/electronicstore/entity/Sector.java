
package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "sector_name")
    private String sectorName;

    @ManyToMany(mappedBy = "sectors", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> users = new HashSet<>();


    //    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference(value = "sector-users")
//    private Set<User> users = new HashSet<>();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "manager_id")
//    @JsonBackReference(value = "sector-manager")
//    private User manager;
//
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "manager_sectors",
//            joinColumns = @JoinColumn(name = "sector_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    @ManyToMany(mappedBy = "sectors", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<User> users = new HashSet<>();
//
//    public void addCashier(User cashier) {
//        if (cashier != null && cashier.getRole() == UserRole.CASHIER) {
//            this.users.add(cashier);
//            cashier.setSector(this);
//        }
//    }
    @Override
    public String toString() {
        return "Sector{" +
                "id=" + id +
                ", sectorName='" + sectorName + '\'' +
//                ", manager=" + (manager != null ? manager.getId() : "null") +
                '}';
    }
    public void get(User cashier) {
        if (cashier != null && cashier.getRole() == UserRole.CASHIER) {
            this.users.add(cashier);
            cashier.setSector(this);
        }
    }
}