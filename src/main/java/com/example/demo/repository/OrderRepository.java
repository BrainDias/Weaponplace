package com.example.demo.repository;

import com.example.demo.entities.ProductOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<ProductOrder,Long>, CrudRepository<ProductOrder,Long> {
}
