package com.hotel.repository;

import com.hotel.model.Employ;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface EmployRepository extends JpaRepository<Employ,Integer>{
    Employ findByUsername(String username);
}
