package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    @JsonBackReference(value = "purchase-purchaseItem")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonBackReference(value = "item-purchaseItem")
    private Item item;
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "invoice_id")
    @JsonBackReference(value = "invoice-purchaseItem")
    private Invoice invoice;
    @Override
    public String toString() {
        return "artikujt"+
                "id"+id+
                "purchase"+purchase+
                "item"+item+
                "sasia"+quantity;
    }
}
