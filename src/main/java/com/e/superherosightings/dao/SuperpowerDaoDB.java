/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

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
public class SuperpowerDaoDB implements SuperpowerDao{

    @Autowired
    private JdbcTemplate jdbc;
    
    @Override
    public Superpower addSuperpower(Superpower superpower) {
        final String INSERT_SUPERPOWER = "INSERT INTO superpower(name,description) VALUES (?,?);";
        jdbc.update(INSERT_SUPERPOWER, superpower.getName(), superpower.getDescription());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superpower.setId(newId);
        return superpower;
    }

    @Override
    public Superpower getSuperpowerById(int id) {
        try{
            final String SELECT_SUPERPOWER = "SELECT * FROM superpower WHERE id = ?;";
            return jdbc.queryForObject(SELECT_SUPERPOWER, new SuperpowerMapper(), id);
        }catch(DataAccessException dae){
            return null;
        }
    }

    @Override
    public List<Superpower> getAll() {
        final String SELECT_SUPERPOWERS = "SELECT * FROM superpower;";
        return jdbc.query(SELECT_SUPERPOWERS, new SuperpowerMapper());
    }

    @Override
    public void updateSuperpower(Superpower superpower) {
        final String UPDATE_POWER = "UPDATE superpower SET "
            + "name = ?,"
            + "description = ? "
            + "WHERE id = ?;";
        jdbc.update(UPDATE_POWER,
                superpower.getName(),
                superpower.getDescription(),
                superpower.getId());
    }

    @Override
    @Transactional
    public void deleteSuperpower(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE ho FROM heroOrganization ho "
                + "JOIN hero ON hero.id = ho.heroId WHERE hero.superpowerId = ?;";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);
        
        final String DELETE_HERO_WITH_SUPERPOWER = "DELETE FROM hero WHERE superpowerId = ?;";
        jdbc.update(DELETE_HERO_WITH_SUPERPOWER, id);
        
        final String DELETE_SUPERPOWER = "DELETE FROM superpower WHERE id = ?";
        jdbc.update(DELETE_SUPERPOWER, id);
    }
    
    public static final class SuperpowerMapper implements RowMapper<Superpower> {

        @Override
        public Superpower mapRow(ResultSet rs, int rowNum) throws SQLException {
            Superpower power = new Superpower();
            power.setId(rs.getInt("id"));
            power.setName(rs.getString("name"));
            power.setDescription(rs.getString("description"));
            return power;
        }
        
    }
}
