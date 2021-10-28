/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.services;

import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Organization;
import com.e.superherosightings.dto.Sighting;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Edward Chang
 */
public interface ServiceLayer {
    List<Hero> getHeroesFromSighting(Sighting sighting);
    
    List<Hero> getHeroesFromLocation(Location location);
    
    List<Location> getLocationsFromHero(Hero hero);
    
    List<Sighting> getSightingsFromDate(LocalDateTime date);
    
    List<Hero> getHeroesFromOrganization(Organization organization);
    
    List<Organization> getOrganizationsFromHero(Hero hero);
    
    List<Sighting> getLastTenSightings();
}
