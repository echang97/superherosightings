/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dao.HeroDaoDB.HeroMapper;
import com.e.superherosightings.dao.LocationDaoDB.LocationMapper;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Sighting;
import com.e.superherosightings.dto.Superpower;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
public class SightingDaoDB implements SightingDao{

    @Autowired
    private JdbcTemplate jdbc;
    
    @Transactional
    @Override
    public Sighting addSighting(Sighting sighting) {
        try{
            final String INSERT_SIGHTING = "INSERT INTO sighting(date, locationId) VALUES(?,?);";
            jdbc.update(INSERT_SIGHTING, sighting.getDate(), sighting.getLocation().getId());
            int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            sighting.setId(newId);
            insertHeroSighting(sighting);
            return sighting;
        }catch(DataAccessException dae){
            return null;
        }
    }
    
    private void insertHeroSighting(Sighting sighting){
        final String INSERT_HERO_SIGHTING = "INSERT INTO heroSighting(heroId, sightingId) VALUES (?,?);";
        for(Hero hero : sighting.getHeroes()){
            jdbc.update(INSERT_HERO_SIGHTING, hero.getId(), sighting.getId());
        }
    }

    @Override
    public Sighting getSightingById(int id) {
        try{
            final String SELECT_SIGHTING = "SELECT * FROM sighting WHERE id = ?;";
            Sighting sighting = jdbc.queryForObject(SELECT_SIGHTING, new SightingMapper(), id);
            sighting.setHeroes(getHeroesForSighting(id));
            sighting.setLocation(getLocationForSighting(id));
            return sighting;
        }catch(DataAccessException dae){
            return null;
        }
    }
    
    private List<Hero> getHeroesForSighting(int id){
        final String SELECT_HEROES_FOR_SIGHTING = "SELECT hero.* FROM hero JOIN "
                + "heroSighting hs ON hs.heroId = hero.id WHERE hs.sightingId = ?;";
        List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_SIGHTING, new HeroMapper(), id);
        final String SELECT_SUPERPOWER = "SELECT * FROM superpower WHERE id = ?;";
        for(Hero hero : heroes){
           Superpower power = jdbc.queryForObject(SELECT_SUPERPOWER, new SuperpowerDaoDB.SuperpowerMapper(), hero.getSuperpowerId());
           hero.setSuperpower(power);
       }
       return heroes;
    }

    private Location getLocationForSighting(int id){
        try{
            final String SELECT_LOCATION_FOR_SIGHTING = "SELECT l.* FROM location l JOIN "
                + "sighting s ON s.locationId = l.id WHERE s.id = ?;";
            return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationMapper(), id);
        }catch(DataAccessException dae){
            return null;
        }
    }

    @Override
    public List<Sighting> getAll() {
        final String SELECT_SIGHTINGS = "SELECT * FROM sighting;";
        List<Sighting> sightings = jdbc.query(SELECT_SIGHTINGS, new SightingMapper());
        associateHeroesAndLocation(sightings);
        return sightings;
    }
    
    private void associateHeroesAndLocation(List<Sighting> sightings){
        for(Sighting sighting : sightings){
            sighting.setHeroes(getHeroesForSighting(sighting.getId()));
            sighting.setLocation(getLocationForSighting(sighting.getId()));
        }
    }

    @Override
    @Transactional
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET "
            + "date = ?,"
            + "locationId = ? "
            + "WHERE id = ?;";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getDate(),
                sighting.getLocation().getId(),
                sighting.getId());
        final String DELETE_HERO_SIGHITNG = "DELETE FROM heroSighting WHERE sightingId = ?;";
        jdbc.update(DELETE_HERO_SIGHITNG, sighting.getId());
        insertHeroSighting(sighting);
    }

    
    @Override
    @Transactional
    public void deleteSighting(int id) {
        final String DELETE_HERO_SIGHTING = "DELETE FROM heroSighting WHERE sightingId = ?;";
        jdbc.update(DELETE_HERO_SIGHTING, id);

        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE id = ?;";
        jdbc.update(DELETE_SIGHTING, id);
    }
    
    @Override
    public List<Sighting> getSightingsForHero(Hero hero){
        final String SELECT_SIGHTING_FOR_HERO = "SELECT * FROM sighting "
                + "JOIN heroSighting hs ON hs.sightingId = sighting.id "
                + "WHERE hs.heroId = ?";
        List<Sighting> sightings = jdbc.query(SELECT_SIGHTING_FOR_HERO, new SightingMapper(), hero.getId());
        associateHeroesAndLocation(sightings);
        return sightings;
    }
    
    @Override
    public List<Sighting> getSightingsForDate(LocalDate date){
        final String SELECT_SIGHTINGS_FOR_DATE = "SELECT * FROM sighting WHERE date = ?;";
        List<Sighting> sightings = jdbc.query(SELECT_SIGHTINGS_FOR_DATE, new SightingMapper(), date);
        associateHeroesAndLocation(sightings);
        return sightings;
    }
    
    @Override
    public List<Sighting> getLastTenSightings(){
        final String SELECT_LAST_10_SIGHTINGS = "SELECT * FROM sighting "
                + "ORDER BY date DESC, id DESC LIMIT 10;";
        List<Sighting> sightings = jdbc.query(SELECT_LAST_10_SIGHTINGS, new SightingMapper());
        associateHeroesAndLocation(sightings);
        return sightings;
    }
    
    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("id"));
            sighting.setDate(rs.getDate("date").toLocalDate());
            return sighting;
        }
        
    }
}
