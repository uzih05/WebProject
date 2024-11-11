package com.example.myweb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.myweb.model.Item;
import com.example.myweb.repository.ItemRepository;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 게시글 목록을 가져오는 메서드
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
