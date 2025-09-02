package com.example.funding.service.impl;

import com.example.funding.model.Notice;
import com.example.funding.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {

    @Override
    public List<Notice> list() {
        return List.of();
    }

    @Override
    public void add(Notice item) {

    }

    @Override
    public void update(Notice item) {

    }

    @Override
    public void delete(long noticeId) {

    }
}
