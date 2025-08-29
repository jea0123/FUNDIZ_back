package com.example.funding.controller;

import com.example.funding.model.Notice;
import com.example.funding.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoticeController {
        @Autowired
        NoticeService service;

        @GetMapping
        List<Notice> list() {
            return service.list();
        }

        @PostMapping
        Notice add(@RequestBody Notice item) {
            service.add(item);

            return item;
        }

        @PutMapping
        Notice update(@RequestBody Notice item) {
            service.update(item);

            return item;
        }

        @DeleteMapping("/{id}")
        void delete(@PathVariable long noticeId) {
            service.delete(noticeId);
    }
}
