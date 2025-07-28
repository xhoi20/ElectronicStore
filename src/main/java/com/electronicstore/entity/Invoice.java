package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

@Table(name="invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arketar_id")
    @JsonBackReference
    private User arketar;

    @Column(name="totali")
    private double totali;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "invoice-purchaseItem")
    private List<PurchaseItem> artikujt = new ArrayList<>();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.PAPAGUAR;;
    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", arketarId=" + (arketar != null ? arketar.getId() : "null") +
                ", totali=" + totali +
                ", artikujt=" + artikujt.stream()
                .map(pi -> "PurchaseItem{id=" + pi.getId() + "}")
                .toList() +
                '}';
    }

}