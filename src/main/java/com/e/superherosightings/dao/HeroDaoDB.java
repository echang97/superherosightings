/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dao.SuperpowerDaoDB.SuperpowerMapper;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Organization;
import com.e.superherosightings.dto.Sighting;
import com.e.superherosightings.dto.Superpower;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Edward Chang
 */
@Repository
public class HeroDaoDB implements HeroDao{
    
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String INSERT_HERO = "INSERT INTO hero (name, description, superpowerId) "
                + "VALUES (?,?,?);";
        jdbc.update(INSERT_HERO, hero.getName(), hero.getDescription(),hero.getSuperpower().getId());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(newId);
        return hero;
    }
    
    @Override
    public Hero getHeroById(int id) {
        try{
            final String SELECT_HERO = "SELECT * FROM hero WHERE id = ?;";
            Hero hero = jdbc.queryForObject(SELECT_HERO, new HeroMapper(), id);
            hero.setSuperpower(getSuperpowerForHero(id));
            return hero;
        }catch(DataAccessException dae){
            return null;
        }
    }
    
    private Superpower getSuperpowerForHero(int id){
        final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* FROM superpower s "
                + "JOIN hero h ON h.superpowerId = s.id WHERE h.id = ?;";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerMapper(), id);
    }

    @Override
    public List<Hero> getAll() {
        final String SELECT_HEROES = "SELECT * FROM hero;";
        List<Hero> heroes = jdbc.query(SELECT_HEROES, new HeroMapper());
        associatePowers(heroes);
        return heroes;
    }
    
    private void associatePowers(List<Hero> heroes){
        for(Hero hero: heroes){
            hero.setSuperpower(getSuperpowerForHero(hero.getId()));
        }
    }
    
    @Override
    @Transactional
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE hero SET "
            + "name = ?,"
            + "description = ?,"
            + "superpowerId = ? "
            + "WHERE id = ?;";
        jdbc.update(UPDATE_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperpower().getId(),
                hero.getId());
    }

    @Override
    @Transactional
    public void deleteHero(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM heroOrganization WHERE heroId = ?;";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);
        
        final String DELETE_HERO_SIGHTING = "DELETE FROM heroSighting WHERE heroId = ?;";
        jdbc.update(DELETE_HERO_SIGHTING, id);

        final String DELETE_HERO = "DELETE FROM hero WHERE id = ?;";
        jdbc.update(DELETE_HERO, id);
    }
    
    @Override
    public List<Hero> getHeroesForOrganization(Organization org){
       final String SELECT_HEROES_FOR_ORGANIZATION = "SELECT hero.* FROM hero JOIN "
                + "heroOrganization ho ON ho.heroId = hero.id WHERE ho.organizationId = ?;";
       List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_ORGANIZATION, new HeroMapper(), org.getId());
       associatePowers(heroes);
       return heroes;
    }
    
    @Override
    public List<Hero> getHeroesForSighting(Sighting sighting){
        final String SELECT_HEROES_FOR_SIGHTING = "SELECT hero.* FROM hero JOIN "
                + "heroSighting hs ON hs.heroId = hero.id WHERE hs.sightingId = ?;";
        List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_SIGHTING, new HeroMapper(), sighting.getId());
        associatePowers(heroes);
        return heroes;
    }
    
    @Override
    public List<Hero> getHeroesForLocation(Location location){
        final String SELECT_HEROES_FOR_LOCATION = "SELECT hero.* FROM hero "
                + "JOIN heroSighting hs ON hs.heroId = hero.id "
                + "JOIN sighting s ON s.id = hs.sightingId "
                + "JOIN location l ON l.id = s.locationId "
                + "WHERE l.id = ?;";
        List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_LOCATION, new HeroMapper(), location.getId());
        associatePowers(heroes);
        return heroes;
    }

    public static final class HeroMapper implements RowMapper<Hero> {
        @Override
        public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hero hero = new Hero();
            hero.setId(rs.getInt("id"));
            hero.setName(rs.getString("name"));
            hero.setDescription(rs.getString("description"));
            hero.setSuperpowerId(rs.getInt("superpowerId"));
            return hero;
        }
    }
}
