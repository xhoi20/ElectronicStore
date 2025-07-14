package com.electronicstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "emri_furnitorit")

    private String name;

    @Column(name = "kontakti")

    private String email;

    @Column(name = "adresa")

    private String adresa;

    @ManyToMany(mappedBy = "suppliers")
    @JsonIgnore
    private Set<Item> items = new HashSet<>();


    @Override
    public String toString() {
        return "Furnitori{" +
                "id=" + id +
                ", emriFurnitorit='" + name + '\'' +
                ", kontakti='" + email + '\'' +
                ", adresa='" + adresa + '\'' +
                ", produktet=" + items +
                '}';
    }
}