package com.example.funding.service;

import com.example.funding.model.Notice;

import java.util.List;

public interface NoticeService {
    List<Notice> list();

    void add(Notice item);

    void update(Notice item);

    void delete(long noticeId);
}
