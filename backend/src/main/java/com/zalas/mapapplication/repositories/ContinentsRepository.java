package com.zalas.mapapplication.repositories;

import com.zalas.mapapplication.model.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContinentsRepository extends JpaRepository<Continent, Long> {
}