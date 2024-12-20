package org.weaponplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.weaponplace.entities.Auction;
import org.weaponplace.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a from Auction a where a.closingAt<current_timestamp")
    Iterable<Auction> findToClose();

    List<Auction> findAllByClosedIsFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Auction a SET a.pretender = :pretender, a.lastPrice = :price\n" +
            "WHERE a.id = :id AND a.lastPrice < :price AND a.closed = false")
    void updateWithBid(User pretender, Long id, Float price);
}
