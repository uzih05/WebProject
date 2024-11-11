// ItemRepository.java
package com.example.myweb.repository;

import com.example.myweb.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}