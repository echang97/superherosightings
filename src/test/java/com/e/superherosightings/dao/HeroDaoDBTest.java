/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.TestApplicationConfiguration;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Organization;
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
public class HeroDaoDBTest {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    LocationDao locationDao;
    
    public HeroDaoDBTest() {
    }

    @BeforeEach
    public void setUp() {
        List<Organization> organizations = organizationDao.getAll();
        for(Organization organization : organizations){
            organizationDao.deleteOrganization(organization.getId());
        }
        
        List<Sighting> sightings = sightingDao.getAll();
        for(Sighting sighting : sightings){
            sightingDao.deleteSighting(sighting.getId());
        }
        
        List<Location> locations = locationDao.getAll();
        for(Location location : locations){
            locationDao.deleteLocation(location.getId());
        }
        
        List<Hero> heroes = heroDao.getAll();
        for(Hero hero : heroes){
            heroDao.deleteHero(hero.getId());
        }
        
        List<Superpower> superpowers = superpowerDao.getAll();
        for(Superpower superpower : superpowers){
            superpowerDao.deleteSuperpower(superpower.getId());
        }
    }

    /**
     * Test of addHero method, of class HeroDaoDB.
     */
    @Test
    public void testAddGetHero() {
        // ASSEMBLE
        Superpower superpower = new Superpower();
        superpower.setName("Flight");
        superpower.setDescription("Enables flight");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("He's Superman");
        hero.setSuperpowerId(superpower.getId());
        hero.setSuperpower(superpower);
        
        // ACT
        hero = heroDao.addHero(hero);
        Hero fromDao = heroDao.getHeroById(hero.getId());
        
        // ASSERT
        assertEquals(hero, fromDao);
    }

    /**
     * Test of getAll method, of class HeroDaoDB.
     */
    @Test
    public void testGetAll() {
        // ASSEMBLE        
        Superpower superpower = new Superpower();
        superpower.setName("Flight");
        superpower.setDescription("Enables flight");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Hero hero = new Hero();
        hero.setName("Green Lantern");
        hero.setDescription("greeen");
        hero.setSuperpowerId(superpower.getId());
        hero.setSuperpower(superpower);
        
        Hero hero2 = new Hero();
        hero2.setName("Superman");
        hero2.setDescription("He's Superman");
        hero2.setSuperpowerId(superpower.getId());
        hero2.setSuperpower(superpower);
        
        // ACT
        hero = heroDao.addHero(hero);
        hero2 = heroDao.addHero(hero2);
        List<Hero> heroes = heroDao.getAll();
        
        // ASSERT
        assertEquals(2, heroes.size());
        assertTrue(heroes.contains(hero));
        assertTrue(heroes.contains(hero2));
    }

    /**
     * Test of updateHero method, of class HeroDaoDB.
     */
    @Test
    public void testUpdateHero() {
        // Make initial variables
        Superpower superpower = new Superpower();
        superpower.setName("Flight");
        superpower.setDescription("Enables flight");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("He's Superman");
        hero.setSuperpowerId(superpower.getId());
        hero.setSuperpower(superpower);
        hero = heroDao.addHero(hero);
        
        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
        
        // Update variables 
        Superpower superpower2 = new Superpower();
        superpower2.setName("Lasers");
        superpower2.setDescription("Shoots lasers");
        superpower2 = superpowerDao.addSuperpower(superpower2);
        
        hero.setName("New Superman");
        hero.setDescription("Souper man");
        hero.setSuperpowerId(superpower2.getId());
        hero.setSuperpower(superpower2);
        
        heroDao.updateHero(hero);
        
        assertNotEquals(hero, fromDao);
        
        // ASSERT
        fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
    }

    /**
     * Test of deleteHero method, of class HeroDaoDB.
     */
    @Test
    public void testDeleteHero() {
        // ASSEMBLE
        Superpower superpower = new Superpower();
        superpower.setName("Flight");
        superpower.setDescription("Enables flight");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("He's Superman");
        hero.setSuperpowerId(superpower.getId());
        hero.setSuperpower(superpower);
        hero = heroDao.addHero(hero);
        
        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
        
        heroDao.deleteHero(hero.getId());
        
        fromDao = heroDao.getHeroById(hero.getId());
        assertNull(fromDao);
    }
    
    @Test
    public void testGetHeroesForOrganization(){
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        power = superpowerDao.addSuperpower(power);
        
        Hero hero = new Hero();
        hero.setName("Test Hero");
        hero.setDescription("test desc");
        hero.setSuperpowerId(power.getId());
        hero.setSuperpower(power);
        hero = heroDao.addHero(hero);
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        
        Organization organization = new Organization();
        organization.setName("Test Org");
        organization.setDescription("Deee");
        organization.setAddress("123 Weee");
        organization.setEmail("eeee@gmail.com");
        organization.setPhone("1234567890");
        organization.setHeroes(heroes);
        
        organization = organizationDao.addOrganization(organization);
        List<Hero> fromDao = heroDao.getHeroesForOrganization(organization);
        
        assertEquals(heroes, fromDao);
    }
    
    @Test
    public void testGetHeroesForSighting(){
        Superpower superpower = new Superpower();
        superpower.setName("Flight");
        superpower.setDescription("Enables flight");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("He's Superman");
        hero.setSuperpowerId(superpower.getId());
        hero.setSuperpower(superpower);
        hero = heroDao.addHero(hero);
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        
        Location location = new Location();
        location.setName("Neo New York");
        location.setDescription("Hamburger Sheriff");
        location.setLatitude(BigDecimal.ONE);
        location.setLongitude(BigDecimal.ONE);
        location = locationDao.addLocation(location);
        
        LocalDate date = LocalDate.parse("2021-12-12");
        Sighting sighting = new Sighting();
        sighting.setLocation(location);
        sighting.setDate(date);
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);
        
        List<Hero> fromDao = heroDao.getHeroesForSighting(sighting);
        assertEquals(heroes, fromDao);
    }
    
    @Test
    public void testGetHeroesForLocation(){
        Superpower superpower = new Superpower();
        superpower.setName("Flight");
        superpower.setDescription("Enables flight");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Hero hero = new Hero();
        hero.setName("Superman");
        hero.setDescription("He's Superman");
        hero.setSuperpowerId(superpower.getId());
        hero.setSuperpower(superpower);
        hero = heroDao.addHero(hero);
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        
        Location location = new Location();
        location.setName("Neo New York");
        location.setDescription("Hamburger Sheriff");
        location.setLatitude(BigDecimal.ONE);
        location.setLongitude(BigDecimal.ONE);
        location = locationDao.addLocation(location);
        
        LocalDate date = LocalDate.parse("2021-12-12");
        Sighting sighting = new Sighting();
        sighting.setLocation(location);
        sighting.setDate(date);
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);
        
        List<Hero> fromDao = heroDao.getHeroesForLocation(location);
        assertEquals(heroes, fromDao);
    }
}
