/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.TestApplicationConfiguration;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Organization;
import com.e.superherosightings.dto.Superpower;
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
public class OrganizationDaoDBTest {
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    public OrganizationDaoDBTest() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Hero> heroes = heroDao.getAll();
        for(Hero hero : heroes){
            heroDao.deleteHero(hero.getId());
        }
        
        List<Organization> orgs = organizationDao.getAll();
        for(Organization org : orgs){
            organizationDao.deleteOrganization(org.getId());
        }
        
        List<Superpower> powers = superpowerDao.getAll();
        for(Superpower power : powers){
            superpowerDao.deleteSuperpower(power.getId());
        }
    }

    /**
     * Test of addOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testAddGetOrganization() {
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
        Organization fromDao = organizationDao.getOrganizationById(organization.getId());
        
        assertEquals(organization, fromDao);
    }

    /**
     * Test of getAll method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetAll() {
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        power = superpowerDao.addSuperpower(power);
        
        Hero hero = new Hero();
        hero.setName("Test Hero");
        hero.setDescription("test desc");
        hero.setSuperpowerId(power.getId());
        hero.setSuperpower(power);
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        hero = heroDao.addHero(hero);
        
        Organization organization = new Organization();
        organization.setName("Test Org");
        organization.setDescription("Deee");
        organization.setAddress("123 Weee");
        organization.setEmail("eeee@gmail.com");
        organization.setPhone("1234567890");
        organization.setHeroes(heroes);
        organization = organizationDao.addOrganization(organization);
        
        Organization organization2 = new Organization();
        organization2.setName("Test Org 2");
        organization2.setDescription("Teeeee");
        organization2.setAddress("555 dsadas");
        organization2.setEmail("bbb@gmail.com");
        organization2.setPhone("3334445555");
        organization2.setHeroes(heroes);
        organization2 = organizationDao.addOrganization(organization2);
        
        List<Organization> fromDao = organizationDao.getAll();
        
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(organization));
        assertTrue(fromDao.contains(organization2));
    }

    /**
     * Test of updateOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testUpdateOrganization() {
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
        
        Organization fromDao = organizationDao.getOrganizationById(organization.getId());
        assertEquals(organization, fromDao);
        
        Hero hero2 = new Hero();
        hero2.setName("Best Hero");
        hero2.setDescription("Best desc");
        hero2.setSuperpowerId(power.getId());
        hero2.setSuperpower(power);
        List<Hero> heroes2 = new ArrayList<>();
        heroes2.add(hero2);
        heroDao.addHero(hero2);
        
        organization.setName("Test Org 2");
        organization.setDescription("Teeeee");
        organization.setAddress("555 dsadas");
        organization.setEmail("bbb@gmail.com");
        organization.setPhone("3334445555");
        organization.setHeroes(heroes2);
        organizationDao.updateOrganization(organization);
        
        assertNotEquals(organization, fromDao);
        
        fromDao = organizationDao.getOrganizationById(organization.getId());
        
        assertEquals(organization, fromDao);
    }

    /**
     * Test of deleteOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testDeleteOrganization() {
        Superpower power = new Superpower();
        power.setName("OPTIC BLAST");
        power.setDescription("OPTIC BLAST");
        power = superpowerDao.addSuperpower(power);
        List<Superpower> powers = new ArrayList<>();
        powers.add(power);
        
        
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
        
        Organization fromDao = organizationDao.getOrganizationById(organization.getId());
        assertEquals(organization, fromDao);
        
        organizationDao.deleteOrganization(organization.getId());
        
        fromDao = organizationDao.getOrganizationById(organization.getId());
        assertNull(fromDao);
    }
    
    @Test
    public void testGetOrganizationsForHero(){
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
        
        Organization organization2 = new Organization();
        organization2.setName("Test Org 2");
        organization2.setDescription("Teeeee");
        organization2.setAddress("555 dsadas");
        organization2.setEmail("bbb@gmail.com");
        organization2.setPhone("3334445555");
        organization2.setHeroes(heroes);
        organization2 = organizationDao.addOrganization(organization2);
        
        List<Organization> fromDao = organizationDao.getOrganizationsForHero(hero);
        
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(organization));
        assertTrue(fromDao.contains(organization2));
    }
    
}
