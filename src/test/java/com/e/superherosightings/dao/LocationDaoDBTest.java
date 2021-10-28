/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.TestApplicationConfiguration;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Sighting;
import com.e.superherosightings.dto.Superpower;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Edward Chang
 */
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class LocationDaoDBTest {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    public LocationDaoDBTest() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Sighting> sightings = sightingDao.getAll();
        for(Sighting sighting : sightings){
            sightingDao.deleteSighting(sighting.getId());
        }
        
        List<Hero> heroes = heroDao.getAll();
        for(Hero hero : heroes){
            heroDao.deleteHero(hero.getId());
        }
        
        List<Superpower> powers = superpowerDao.getAll();
        for(Superpower power : powers){
            superpowerDao.deleteSuperpower(power.getId());
        }
        
        List<Location> locations = locationDao.getAll();
        for(Location location : locations){
            locationDao.deleteLocation(location.getId());
        }
    }

    /**
     * Test of addLocation method, of class LocationDaoDB.
     */
    @Test
    public void testAddGetLocation() {
        Location location = new Location();
        location.setName("Downtown");
        location.setDescription("over there");
        location.setLatitude(BigDecimal.ONE.setScale(6));
        location.setLongitude(BigDecimal.ONE.setScale(6));
        
        location = locationDao.addLocation(location);
        Location fromDao = locationDao.getLocationById(location.getId());
        
        assertEquals(location, fromDao);
    }
    
    /**
     * Test of getAll method, of class LocationDaoDB.
     */
    @Test
    public void testGetAll() {
        Location location = new Location();
        location.setName("Downtown");
        location.setDescription("over there");
        location.setLatitude(BigDecimal.ONE.setScale(6));
        location.setLongitude(BigDecimal.ONE.setScale(6));
        location = locationDao.addLocation(location);
        
        Location location2 = new Location();
        location2.setName("Test Location 2");
        location2.setDescription("HERE");
        location2.setLatitude(BigDecimal.ONE.setScale(6));
        location2.setLongitude(BigDecimal.ONE.setScale(6));
        location2 = locationDao.addLocation(location);
        
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        locations.add(location2);
        
        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location2));
    }

    /**
     * Test of updateLocation method, of class LocationDaoDB.
     */
    @Test
    public void testUpdateLocation() {
        Location location = new Location();
        location.setName("Downtown");
        location.setDescription("over there");
        location.setLatitude(BigDecimal.ONE.setScale(6));
        location.setLongitude(BigDecimal.ONE.setScale(6));
        
        location = locationDao.addLocation(location);
        Location fromDao = locationDao.getLocationById(location.getId());
        
        assertEquals(location, fromDao);
        
        location.setName("test 2");
        location.setDescription("test description 2");
        location.setLatitude(BigDecimal.ZERO.setScale(6));
        location.setLongitude(BigDecimal.ZERO.setScale(6));
        
        locationDao.updateLocation(location);
        assertNotEquals(location, fromDao);
        
        fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    /**
     * Test of deleteLocation method, of class LocationDaoDB.
     */
    @Test
    public void testDeleteLocation() {
        Location location = new Location();
        location.setName("Downtown");
        location.setDescription("over there");
        location.setLatitude(BigDecimal.ONE.setScale(6));
        location.setLongitude(BigDecimal.ONE.setScale(6));
        
        location = locationDao.addLocation(location);
        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
        
        locationDao.deleteLocation(location.getId());
        
        fromDao = locationDao.getLocationById(location.getId());
        assertNull(fromDao);
    }
    
    @Test
    public void testGetLocationsForHero(){
        Superpower power = new Superpower();
        power.setName("???");
        power.setDescription("asdf");
        power = superpowerDao.addSuperpower(power);
        
        Hero hero = new Hero();
        hero.setName("AAAA");
        hero.setDescription("BBBB");
        hero.setSuperpowerId(power.getId());
        hero.setSuperpower(power);
        hero = heroDao.addHero(hero);
        
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        
        Location location = new Location();
        location.setName("Downtown");
        location.setDescription("over there");
        location.setLatitude(BigDecimal.ONE.setScale(6));
        location.setLongitude(BigDecimal.ONE.setScale(6));
        location = locationDao.addLocation(location);
        
        LocalDate date = LocalDate.parse("2021-10-25");
        Sighting sighting = new Sighting();
        sighting.setHeroes(heroes);
        sighting.setDate(date);
        sighting.setLocation(location);
        sighting = sightingDao.addSighting(sighting);
        
        List<Location> fromDao = locationDao.getLocationsForHero(hero);
        assertTrue(fromDao.contains(location));
    }
}
