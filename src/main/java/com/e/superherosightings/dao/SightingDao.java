/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dto.Sighting;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Edward Chang
 */
public interface SightingDao {
    Sighting addSighting(Sighting sighting);
    Sighting getSightingById(int id);
    List<Sighting> getAll();
    void updateSighting(Sighting sighting);
    void deleteSighting(int id);
    
    List<Sighting> getSightingsForDate(LocalDate date);
    List<Sighting> getLastTenSightings();
}
