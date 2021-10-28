/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Organization;
import com.e.superherosightings.dto.Sighting;
import java.util.List;

/**
 *
 * @author Edward Chang
 */
public interface HeroDao {
    Hero addHero(Hero hero);
    Hero getHeroById(int id);
    List<Hero> getAll();
    void updateHero(Hero hero);
    void deleteHero(int id);
    
    List<Hero> getHeroesForOrganization(Organization organization);
    List<Hero> getHeroesForSighting(Sighting sighting);
    List<Hero> getHeroesForLocation(Location location);
}
