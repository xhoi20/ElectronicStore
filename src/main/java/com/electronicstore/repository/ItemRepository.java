package com.electronicstore.repository;

import com.electronicstore.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {

}
