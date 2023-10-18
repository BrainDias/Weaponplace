package com.example.demo.repositories;

import com.example.demo.entities.Auction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends PagingAndSortingRepository<Auction,Long>, CrudRepository<Auction,Long> {
}
