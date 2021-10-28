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
public class SightingDaoDBTest {
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    public SightingDaoDBTest() {
    }

    @BeforeEach
    public void setUp() {
        List<Sighting> sightings = sightingDao.getAll();
        for(Sighting sighting : sightings){
            sightingDao.deleteSighting(sighting.getId());
        }
        
        List<Superpower> powers = superpowerDao.getAll();
        for(Superpower power : powers){
            superpowerDao.deleteSuperpower(power.getId());
        }
        
        List<Hero> heroes = heroDao.getAll();
        for(Hero hero : heroes){
            heroDao.deleteHero(hero.getId());
        }
        
        List<Location> locations = locationDao.getAll();
        for(Location location : locations){
            locationDao.deleteLocation(location.getId());
        }
    }

    /**
     * Test of addSighting method, of class SightingDaoDB.
     */
    @Test
    public void testAddGetSighting() {
        // Assemble
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
        
        //ACT
        sighting = sightingDao.addSighting(sighting);
        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        
        //ASSERT
        assertEquals(sighting, fromDao);
    }

    /**
     * Test of getAll method, of class SightingDaoDB.
     */
    @Test
    public void testGetAll() {
        // ASSEMBLE
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
        
        LocalDate date2 = LocalDate.parse("2021-10-22");
        Sighting sighting2 = new Sighting();
        sighting2.setHeroes(heroes);
        sighting2.setDate(date2);
        sighting2.setLocation(location);
        sighting2 = sightingDao.addSighting(sighting2);
        
        
        // ACT
        List<Sighting> fromDao = sightingDao.getAll();
        
        // ASSERT
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(sighting));
        assertTrue(fromDao.contains(sighting2));
    }

    /**
     * Test of updateSighting method, of class SightingDaoDB.
     */
    @Test
    public void testUpdateSighting() {
        // ASSEMBLE
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
        
        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        
        assertEquals(sighting, fromDao);
        
        Location location2 = new Location();
        location2.setName("dust2");
        location2.setDescription("dust2");
        location2.setLatitude(BigDecimal.ZERO.setScale(6));
        location2.setLongitude(BigDecimal.ZERO.setScale(6));
        location2 = locationDao.addLocation(location2);
        
        LocalDate date2 = LocalDate.parse("2021-10-22");
        sighting.setHeroes(heroes);
        sighting.setDate(date2);
        sighting.setLocation(location2);
        
        // ACT
        sightingDao.updateSighting(sighting);
        assertNotEquals(sighting, fromDao);
        
        fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);
    }

    /**
     * Test of deleteSighting method, of class SightingDaoDB.
     */
    @Test
    public void testDeleteSighting() {
        // ASSEMBLE
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
        
        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        
        assertEquals(sighting, fromDao);
        
        // ACT
        sightingDao.deleteSighting(sighting.getId());
        fromDao = sightingDao.getSightingById(sighting.getId());
        
        // ASSERT
        assertNull(fromDao);
    }
    
    @Test
    public void getSightingsForDate(){
        // ASSEMBLE
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
        
        LocalDate date = LocalDate.parse("2021-10-22");
        Sighting sighting = new Sighting();
        sighting.setHeroes(heroes);
        sighting.setDate(date);
        sighting.setLocation(location);
        sighting = sightingDao.addSighting(sighting);
        
        LocalDate date2 = LocalDate.parse("2021-10-25");
        Sighting sighting2 = new Sighting();
        sighting2.setHeroes(heroes);
        sighting2.setDate(date2);
        sighting2.setLocation(location);
        sighting2 = sightingDao.addSighting(sighting2);
        
        List<Sighting> fromDao = sightingDao.getSightingsForDate(date);
        
        assertEquals(1, fromDao.size());
        assertTrue(fromDao.contains(sighting));
        assertFalse(fromDao.contains(sighting2));
    }
    
    @Test
    public void getLastTenSightings(){
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
        
        LocalDate date = LocalDate.parse("2021-10-22");
        Sighting sighting = new Sighting();
        sighting.setHeroes(heroes);
        sighting.setDate(date);
        sighting.setLocation(location);
        sighting = sightingDao.addSighting(sighting);
        
        LocalDate date2 = LocalDate.parse("2021-10-25");
        Sighting sighting2 = new Sighting();
        sighting2.setHeroes(heroes);
        sighting2.setDate(date2);
        sighting2.setLocation(location);
        sighting2 = sightingDao.addSighting(sighting2);
        
        Sighting sighting3 = new Sighting();
        sighting3.setHeroes(heroes);
        sighting3.setDate(date2);
        sighting3.setLocation(location);
        sighting3 = sightingDao.addSighting(sighting3);
        
        Sighting sighting4 = new Sighting();
        sighting4.setHeroes(heroes);
        sighting4.setDate(date2);
        sighting4.setLocation(location);
        sighting4 = sightingDao.addSighting(sighting4);
        
        Sighting sighting5 = new Sighting();
        sighting5.setHeroes(heroes);
        sighting5.setDate(date2);
        sighting5.setLocation(location);
        sighting5 = sightingDao.addSighting(sighting5);
        
        Sighting sighting6 = new Sighting();
        sighting6.setHeroes(heroes);
        sighting6.setDate(date2);
        sighting6.setLocation(location);
        sighting6 = sightingDao.addSighting(sighting6);
        
        Sighting sighting7 = new Sighting();
        sighting7.setHeroes(heroes);
        sighting7.setDate(date2);
        sighting7.setLocation(location);
        sighting7 = sightingDao.addSighting(sighting7);
        
        Sighting sighting8 = new Sighting();
        sighting8.setHeroes(heroes);
        sighting8.setDate(date2);
        sighting8.setLocation(location);
        sighting8 = sightingDao.addSighting(sighting8);
        
        Sighting sighting9 = new Sighting();
        sighting9.setHeroes(heroes);
        sighting9.setDate(date2);
        sighting9.setLocation(location);
        sighting9 = sightingDao.addSighting(sighting9);
        
        Sighting sighting10 = new Sighting();
        sighting10.setHeroes(heroes);
        sighting10.setDate(date2);
        sighting10.setLocation(location);
        sighting10 = sightingDao.addSighting(sighting10);
        
        Sighting sighting11 = new Sighting();
        sighting11.setHeroes(heroes);
        sighting11.setDate(date2);
        sighting11.setLocation(location);
        sighting11 = sightingDao.addSighting(sighting11);
        
        
        // ACT
        List<Sighting> fromDao = sightingDao.getLastTenSightings();
        
        System.out.println(fromDao);
        
        assertEquals(10, fromDao.size());
        assertTrue(fromDao.contains(sighting2));
        assertTrue(fromDao.contains(sighting3));
        assertTrue(fromDao.contains(sighting4));
        assertTrue(fromDao.contains(sighting5));
        assertTrue(fromDao.contains(sighting6));
        assertTrue(fromDao.contains(sighting7));
        assertTrue(fromDao.contains(sighting8));
        assertTrue(fromDao.contains(sighting9));
        assertTrue(fromDao.contains(sighting10));
        assertTrue(fromDao.contains(sighting11));
    }
    
}
