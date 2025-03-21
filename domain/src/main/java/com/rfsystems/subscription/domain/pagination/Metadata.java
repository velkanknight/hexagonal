package com.rfsystems.subscription.domain.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {
}
