package com.test.electronic.repository;

import com.test.electronic.model.entity.Payment;
import com.test.electronic.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Page<Payment> findByUser(User user, Pageable pageable);


}




