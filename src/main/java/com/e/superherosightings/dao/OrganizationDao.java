/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Organization;
import java.util.List;

/**
 *
 * @author Edward Chang
 */
public interface OrganizationDao {
    Organization addOrganization(Organization organization);
    Organization getOrganizationById(int id);
    List<Organization> getAll();
    void updateOrganization(Organization organization);
    void deleteOrganization(int id);
    
    List<Organization> getOrganizationsForHero(Hero hero);
}
