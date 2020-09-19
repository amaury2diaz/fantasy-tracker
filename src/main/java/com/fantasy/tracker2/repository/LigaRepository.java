package com.fantasy.tracker2.repository;

import com.fantasy.tracker2.domain.Liga;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Liga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigaRepository extends JpaRepository<Liga, Long> {
}
