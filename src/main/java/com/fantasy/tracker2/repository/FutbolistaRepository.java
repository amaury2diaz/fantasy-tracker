package com.fantasy.tracker2.repository;

import com.fantasy.tracker2.domain.Futbolista;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Futbolista entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FutbolistaRepository extends JpaRepository<Futbolista, Long> {
}
