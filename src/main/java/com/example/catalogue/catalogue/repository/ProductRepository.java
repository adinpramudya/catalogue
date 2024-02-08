package com.example.catalogue.catalogue.repository;

import com.example.catalogue.catalogue.entities.ProductEntity;
import com.example.catalogue.catalogue.globals.GlobalConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {

    @Query(value = "SELECT (*) COUNT FROM ms_product WHERE rec_status = '"+ GlobalConstant.REC_STATUS_ACTIVE+"'AND LOWER(name) = LOWER(:name)", nativeQuery = true)
    long countByName(@Param("name")String name);
}
