package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.Map;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        try {
            itemRepository.count(); // Test DB connection
            return ResponseEntity.ok(Map.of("status", "UP", "database", "CONNECTED"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('VIEWER', 'EDITOR', 'ADMIN')")
    public Page<Item> getAllItems(@PageableDefault(size = 5) Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @GetMapping("/items/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'EDITOR', 'ADMIN')")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setNombre(itemDetails.getNombre());
                    item.setDescripcion(itemDetails.getDescripcion());
                    Item updatedItem = itemRepository.save(item);
                    return new ResponseEntity<>(updatedItem, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
