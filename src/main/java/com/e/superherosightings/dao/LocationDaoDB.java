/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
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
public class LocationDaoDB implements LocationDao{

    @Autowired
    private JdbcTemplate jdbc;
    
    @Override
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO location(name,description,latitude,longitude) VALUES (?,?,?,?);";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getLatitude(),
                location.getLongitude());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        return location;
    }

    @Override
    public Location getLocationById(int id) {
        try{
            final String SELECT_LOCATION = "SELECT * FROM location WHERE id = ?;";
            Location location = jdbc.queryForObject(SELECT_LOCATION, new LocationMapper(), id);
            return location;
        }catch(DataAccessException dae){
            return null;
        }
    }

    @Override
    public List<Location> getAll() {
        final String SELECT_LOCATIONS = "SELECT * FROM location;";
        return jdbc.query(SELECT_LOCATIONS, new LocationMapper());
    }

    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE location SET "
            + "name = ?, description = ?, latitude = ?, longitude = ? WHERE id = ?;";
        jdbc.update(UPDATE_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getLatitude(),
                location.getLongitude(),
                location.getId());
    }

    @Override
    @Transactional
    public void deleteLocation(int id) {
        final String DELETE_HERO_SIGHITNG = "DELETE hs.* FROM heroSighting hs "
                + "JOIN sighting s ON s.id = hs.sightingId "
                + "JOIN location l ON l.id = s.locationId "
                + "WHERE l.id = ?;";
        jdbc.update(DELETE_HERO_SIGHITNG, id);
        
        final String DELETE_SIGHTING_FOR_LOCATION = "DELETE FROM sighting "
                + "WHERE locationId = ?;";
        jdbc.update(DELETE_SIGHTING_FOR_LOCATION,id);

        final String DELETE_LOCATION = "DELETE FROM location WHERE id = ?;";
        jdbc.update(DELETE_LOCATION, id);
    }
    
    @Override
    public List<Location> getLocationsForHero(Hero hero){
        final String SELECT_LOCATIONS_FOR_HERO = "SELECT l.* FROM hero h "
                + "JOIN heroSighting hs ON hs.heroId = h.id "
                + "JOIN sighting s ON s.id = hs.sightingId "
                + "JOIN location l ON l.id = s.locationId WHERE h.id = ?;";
        return jdbc.query(SELECT_LOCATIONS_FOR_HERO, new LocationMapper(), hero.getId());
    }
    
    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setName(rs.getString("name"));
            location.setDescription(rs.getString("description"));
            location.setLatitude(rs.getBigDecimal("latitude"));
            location.setLongitude(rs.getBigDecimal("longitude"));
            return location;
        }
        
    }
}
