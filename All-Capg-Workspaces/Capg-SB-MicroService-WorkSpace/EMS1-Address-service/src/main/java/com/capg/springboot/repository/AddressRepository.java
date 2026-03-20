package com.capg.springboot.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.springboot.entity.Address;



public interface AddressRepository extends JpaRepository<Address, Long> {
}