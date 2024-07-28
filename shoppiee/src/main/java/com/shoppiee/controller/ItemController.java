package com.shoppiee.controller;

import com.shoppiee.model.Item;
import com.shoppiee.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/addItem")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.addItem(item));
    }

    @PostMapping("/updateItem")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(item));
    }

    @PostMapping("/deleteItem")
    public ResponseEntity<Void> deleteItem(@RequestBody Item item) {
        itemService.deleteItem(item.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }
}
