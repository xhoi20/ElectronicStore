package com.electronicstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Purchase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_blerjes")

    private LocalDateTime dataBlerjes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "furnitor_id")
    @JsonBackReference(value = "supplier-purchase")
    private Supplier furnitori;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menaxher_id")
    @JsonBackReference(value = "user-purchase")
    private User menaxheri;

    @Column(name = "totali_kostos")

    private double totaliKostos;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "purchase-purchaseItem")
    private List<PurchaseItem> artikujt = new ArrayList<>();


        @Column(name = "sasia")

        private int sasia;
@Override
    public String toString() {
    return "Purchase"+
            "id"+id+
            "data e blerjes"+dataBlerjes+
            "furnitori"+furnitori+
            "menaxheri"+menaxheri+
            "totali Kosto"+totaliKostos+
            "artikujt"+artikujt;
}
    }
