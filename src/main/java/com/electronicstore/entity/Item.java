
package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emri")
    private String emri;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference(value = "category-item")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "ItemFurnitori",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    @JsonIgnore
    private Set<Supplier> suppliers = new HashSet<>();

    @Column(name = "cmimi")
    private BigDecimal cmimi;

    @Column(name = "sasia")
    private int sasia;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    @JsonBackReference(value = "sector-item")
    private Sector sector;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", emri='" + emri+ '\'' +
                ", cmimi=" + cmimi +
                ", sasia=" + sasia +
                ", suppliers=" + suppliers.stream()
                .map(Supplier::getId)
                .toList() +
                '}';
    }
}