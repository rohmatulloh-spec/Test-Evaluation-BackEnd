package com.example.demo.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPagination {
    private int currentPage;
    private boolean hasNextPage;
    private ItemsInfo items;
    private int lastVisiblePage;
}
