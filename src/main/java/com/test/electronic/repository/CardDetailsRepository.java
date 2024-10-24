package com.test.electronic.repository;

import com.test.electronic.model.entity.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDetailsRepository extends JpaRepository <CardDetails, Long> {


}
