package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pager {
    private int page;
    private int size;
    private int perGroup;
    private Integer totalElements;
    private Integer totalPage;

    public void setDefault() {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (perGroup < 1) perGroup = 5;
    }
}
