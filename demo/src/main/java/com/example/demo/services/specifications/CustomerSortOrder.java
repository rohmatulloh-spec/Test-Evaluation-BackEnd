package com.example.demo.services.specifications;

import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerSortOrder {

    CUSTOMERNAME_ASC("customerName", Sort.Direction.ASC),
    CUSTOMERNAME_DESC("customerName", Sort.Direction.DESC),
    CUSTOMEREMAIL_ASC("customerEmail", Sort.Direction.ASC),
    CUSTOMEREMAIL_DESC("customerEmail", Sort.Direction.DESC);

    
    private final String fieldName;
    private final Sort.Direction direction;
}
