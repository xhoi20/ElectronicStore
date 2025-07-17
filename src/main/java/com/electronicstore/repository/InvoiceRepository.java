package com.electronicstore.repository;

import com.electronicstore.entity.Invoice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByArketarId(Long userId);
    List<Invoice> findByArketarSectorsIdIn(List<Long> sectorIds);
    @EntityGraph(attributePaths = {"artikujt", "artikujt.item", "arketar"})
    Optional<Invoice> findById(Long id);

}
