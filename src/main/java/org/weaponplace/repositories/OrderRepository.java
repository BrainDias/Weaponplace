package org.weaponplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weaponplace.entities.ProductOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
}
