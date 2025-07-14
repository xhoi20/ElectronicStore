package com.electronicstore.repository;

import com.electronicstore.entity.Invoice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    List<Invoice> findByArketarId(Long userId);
    List<Invoice> findByArketarSectorsIdIn(List<Long> sectorIds);
    @EntityGraph(attributePaths = {"artikujt", "artikujt.item", "arketar"})
    Optional<Invoice> findById(Long id);

}
