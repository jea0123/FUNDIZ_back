package com.example.funding.common;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResult<T> {

    @Builder.Default
    private List<T> items = Collections.emptyList();

    private int page; //현재 페이지
    private int size; //페이지 크기
    private int totalElements; //전체 데이터 수
    private int totalPage; //전체 페이지 수

    private boolean hasPrev; //이전 페이지 여부
    private boolean hasNext; //다음 페이지 여부
    private int prevPage; //이전 페이지 번호
    private int nextPage; //다음 페이지 번호

    private int groupStart; //현재 그룹 시작 페이지
    private int groupEnd; //현재 그룹 끝 페이지
    private int perGroup; //그룹 크기

    public static <T> PageResult<T> of(List<T> items, Pager pager) {
        int page = (pager.getPage() < 1) ? 1 : pager.getPage();
        int size = (pager.getSize() < 1) ? 10 : pager.getSize();
        int perGroup = (pager.getPerGroup() < 1) ? 5 : pager.getPerGroup();

        int totalElements = (pager.getTotalElements() == null) ? 0 : pager.getTotalElements();
        int totalPage = Math.max(1, (int) Math.ceil(totalElements / (double) size));

        boolean hasPrev = page > 1;
        boolean hasNext = page < totalPage;
        int prevPage = hasPrev ? page - 1 : 1;
        int nextPage = hasNext ? page + 1 : totalPage;

        int groupIndex = (int) Math.ceil(page / (double) perGroup);
        int groupStart = (groupIndex - 1) * perGroup + 1;
        int groupEnd = Math.min(groupStart + perGroup - 1, totalPage);

        return PageResult.<T>builder()
                .items(items)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPage(totalPage)
                .hasPrev(hasPrev)
                .hasNext(hasNext)
                .prevPage(prevPage)
                .nextPage(nextPage)
                .groupStart(groupStart)
                .groupEnd(groupEnd)
                .perGroup(perGroup)
                .build();
    }
}
