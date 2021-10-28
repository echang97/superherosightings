/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dao.HeroDaoDB.HeroMapper;
import com.e.superherosightings.dao.SuperpowerDaoDB.SuperpowerMapper;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Organization;
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
public class OrganizationDaoDB implements OrganizationDao{

    @Autowired
    private JdbcTemplate jdbc;
    
    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORG = "INSERT INTO organization(name, description, address, email, phone) "
                + "VALUES (?,?,?,?,?);";
        jdbc.update(INSERT_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getEmail(),
                organization.getPhone());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setId(newId);
        insertHeroOrganization(organization);
        return organization;
    }
    
    private void insertHeroOrganization(Organization organization){
        final String INSERT_ORGANIZATION_HERO = "INSERT INTO "
                + "heroOrganization(heroId, organizationId) VALUES (?,?);";
        for(Hero hero : organization.getHeroes()){
            jdbc.update(INSERT_ORGANIZATION_HERO, 
                    hero.getId(),
                    organization.getId());
        }
    }

    @Override
    public Organization getOrganizationById(int id) {
        try{
            final String SELECT_ORG = "SELECT * FROM organization "
                + "WHERE id = ?;";
            Organization organization = jdbc.queryForObject(SELECT_ORG, new OrganizationMapper(), id);
            organization.setHeroes(getHeroesForOrganization(id));
            return organization;
        }catch (DataAccessException dae){
            return null;
        }
    }
    
    private List<Hero> getHeroesForOrganization(int id){
       final String SELECT_HEROES_FOR_ORGANIZATION = "SELECT hero.* FROM hero JOIN "
                + "heroOrganization ho ON ho.heroId = hero.id WHERE ho.organizationId = ?;";
       List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_ORGANIZATION, new HeroMapper(), id);
       
       final String SELECT_SUPERPOWER = "SELECT * FROM superpower WHERE id = ?;";
       for(Hero hero : heroes){
           Superpower power = jdbc.queryForObject(SELECT_SUPERPOWER, new SuperpowerMapper(), hero.getSuperpowerId());
           hero.setSuperpower(power);
       }
       return heroes;
    }

    @Override
    public List<Organization> getAll() {
        final String SELECT_ORGANIZATIONS = "SELECT * FROM organization;";
        List<Organization> orgs = jdbc.query(SELECT_ORGANIZATIONS, new OrganizationMapper());
        associateHeroes(orgs);
        return orgs;
    }
    
    private void associateHeroes(List<Organization> orgs){
        for(Organization org : orgs){
            org.setHeroes(getHeroesForOrganization(org.getId()));
        }
    }
            
    @Override
    @Transactional
    public void updateOrganization(Organization organization) {
        final String UPDATE_ORG = "UPDATE organization SET "
            + "name = ?,"
            + "description = ?,"
            + "address = ?,"
            + "phone = ?,"
            + "email = ? "
            + "WHERE id = ?;";
        jdbc.update(UPDATE_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getPhone(),
                organization.getEmail(),
                organization.getId());
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM heroOrganization WHERE organizationId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, organization.getId());
        insertHeroOrganization(organization);
    }

    @Override
    @Transactional
    public void deleteOrganization(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM heroOrganization WHERE organizationId = ?;";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);

        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE id = ?;";
        jdbc.update(DELETE_ORGANIZATION, id);
    }
    
    @Override
    public List<Organization> getOrganizationsForHero(Hero hero){
        final String SELECT_ORGANIZATIONS_FOR_HERO = "SELECT o.* FROM organization o JOIN "
                + "heroOrganization ho ON ho.organizationId = o.id WHERE ho.heroId = ?;";
        List<Organization> orgs = jdbc.query(SELECT_ORGANIZATIONS_FOR_HERO, new OrganizationMapper(), hero.getId());
        associateHeroes(orgs);
        return orgs;
    }
    
    public static final class OrganizationMapper implements RowMapper<Organization> {
        @Override
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization organization = new Organization();
            organization.setId(rs.getInt("id"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setAddress(rs.getString("address"));
            organization.setPhone(rs.getString("phone"));
            organization.setEmail(rs.getString("email"));
            return organization;
        }
    }
}
