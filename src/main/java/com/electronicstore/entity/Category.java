package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Long id;
    @Column(name="emmrikategorise")
    private String emmrikategorise;
    @Column(name="pershkrimi")
    private String pershkrimi;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "category-item")
    private List<Item> items = new ArrayList<>();
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", emmrikategorise='" + emmrikategorise + '\'' +
                ", pershkrimi='" + pershkrimi + '\'' +
                '}';
    }

}
