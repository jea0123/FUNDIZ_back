package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Pager {
    private final Integer page;
    private final Integer size;
    private final Integer perGroup;

    public static Pager ofRequest(Integer page, Integer size, Integer perGroup) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1) ? 10 : size;
        int g = (perGroup == null || perGroup < 1) ? 5 : perGroup;

        return Pager.builder().page(p).size(s).perGroup(g).build();
    }

    public int getStartRow() {
        return (page - 1) * size + 1;
    }

    public int getEndRow() {
        return page * size;
    }
}
