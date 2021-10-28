/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.TestApplicationConfiguration;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Superpower;
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
public class SuperpowerDaoDBTest {
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @Autowired
    HeroDao heroDao;

    @BeforeEach
    public void setUp() {
        List<Hero> heroes = heroDao.getAll();
        for(Hero hero : heroes){
            heroDao.deleteHero(hero.getId());
        }
        
        List<Superpower> powers = superpowerDao.getAll();
        for(Superpower power : powers){
            superpowerDao.deleteSuperpower(power.getId());
        }
    }
    
    /**
     * Test of addSuperpower method, of class SuperpowerDaoDB.
     */
    @Test
    public void testAddGetSuperpower() {
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        
        power = superpowerDao.addSuperpower(power);
        Superpower fromDao = superpowerDao.getSuperpowerById(power.getId());
        
        assertEquals(power, fromDao);
    }
    
    /**
     * Test of getAll method, of class SuperpowerDaoDB.
     */
    @Test
    public void testGetAll() {
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        power = superpowerDao.addSuperpower(power);
        
        Superpower power2 = new Superpower();
        power2.setName("SONIC BOOM");
        power2.setDescription("[4]6P");
        power2 = superpowerDao.addSuperpower(power2);
        
        List<Superpower> fromDao = superpowerDao.getAll();
        
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(power));
        assertTrue(fromDao.contains(power2));
    }

    /**
     * Test of updateSuperpower method, of class SuperpowerDaoDB.
     */
    @Test
    public void testUpdateSuperpower() {
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        power = superpowerDao.addSuperpower(power);
        Superpower fromDao = superpowerDao.getSuperpowerById(power.getId());
        
        assertEquals(power, fromDao);
        
        power.setName("SONIC BOOM");
        power.setDescription("[4]6P");
        
        superpowerDao.updateSuperpower(power);
        assertNotEquals(power, fromDao);
        
        fromDao = superpowerDao.getSuperpowerById(power.getId());
        assertEquals(power, fromDao);
    }

    /**
     * Test of deleteSuperpower method, of class SuperpowerDaoDB.
     */
    @Test
    public void testDeleteSuperpower() {
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        power = superpowerDao.addSuperpower(power);
        
        Superpower fromDao = superpowerDao.getSuperpowerById(power.getId());
        
        assertEquals(power, fromDao);
        
        superpowerDao.deleteSuperpower(power.getId());
        
        fromDao = superpowerDao.getSuperpowerById(power.getId());
        
        assertNull(fromDao);
    }
    
}
