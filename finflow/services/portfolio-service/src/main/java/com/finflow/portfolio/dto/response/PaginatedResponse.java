package com.finflow.portfolio.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Generic paginated response wrapper for list endpoints.
 * Provides consistent pagination metadata across all endpoints.
 *
 * @param <T> the type of content in the response
 */
public record PaginatedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last,
    boolean hasNext,
    boolean hasPrevious
) {
    /**
     * Factory method to create PaginatedResponse from Spring Data Page
     *
     * @param page the Spring Data Page
     * @param <T> the type of content
     * @return PaginatedResponse with pagination metadata
     */
    public static <T> PaginatedResponse<T> from(Page<T> page) {
        return new PaginatedResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious()
        );
    }

    /**
     * Factory method to create PaginatedResponse from Spring Data Page with content mapping
     * Useful for converting Entity pages to DTO pages
     *
     * @param page the Spring Data Page of entities
     * @param mapper function to convert entity to DTO
     * @param <E> the entity type
     * @param <D> the DTO type
     * @return PaginatedResponse with mapped content and pagination metadata
     */
    public static <E, D> PaginatedResponse<D> from(Page<E> page, Function<E, D> mapper) {
        List<D> mappedContent = page.getContent().stream()
            .map(mapper)
            .toList();

        return new PaginatedResponse<>(
            mappedContent,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
}
