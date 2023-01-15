package com.dataiku.millenium.repositories;

import com.dataiku.millenium.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for accessing and managing {@link Route} entities in the database.
 * Extends {@link JpaRepository} and provides default CRUD methods as well as additional custom methods.
 */
public interface RouteRepository extends JpaRepository<Route, Long> {
}