package com.openpayd.task.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse {

    private int currentPage;
    private long totalElements;
    private int totalPages;
    private Object content;
}
