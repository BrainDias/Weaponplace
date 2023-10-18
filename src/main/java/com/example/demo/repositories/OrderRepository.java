package com.example.demo.repositories;

import com.example.demo.entities.ProductOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<ProductOrder,Long>, CrudRepository<ProductOrder,Long> {
}
