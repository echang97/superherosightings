/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.dao;

import com.e.superherosightings.dto.Superpower;
import java.util.List;

/**
 *
 * @author Edward Chang
 */
public interface SuperpowerDao {
    Superpower addSuperpower(Superpower superpower);
    
    Superpower getSuperpowerById(int id);
    
    List<Superpower> getAll();
    
    void updateSuperpower(Superpower superpower);
    
    void deleteSuperpower(int id);
}
