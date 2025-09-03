package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pager {
    private Integer page;
    private Integer size;
    private Integer perGroup;
    private Integer totalElements;
    private Integer totalPage;

    public void setDefault() {
        this.page = 1;
        this.size = 10;
        this.perGroup = 5;
    }
}
