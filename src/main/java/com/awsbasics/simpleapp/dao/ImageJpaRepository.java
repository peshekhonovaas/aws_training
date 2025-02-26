package com.awsbasics.simpleapp.dao;

import com.awsbasics.simpleapp.entity.ImageEntityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntityModel, Long> {

    List<ImageEntityModel> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM image ORDER BY RAND() limit 1;")
    Optional<ImageEntityModel> findRandomEntity();
}
