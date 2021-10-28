/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import java.util.List;

/**
 *
 * @author Edward Chang
 */
public interface LocationDao {
    Location addLocation(Location location);
    Location getLocationById(int id);
    List<Location> getAll();   
    void updateLocation(Location location);
    void deleteLocation(int id);
    
    List<Location> getLocationsForHero(Hero hero);
}
