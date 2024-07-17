package com.example.demo.dto.response;

import java.util.List;

import com.example.demo.dto.pagination.CustomerPagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListResponse {
     private List<CustomerResponse> customerList;
    private CustomerPagination pagination;
}
