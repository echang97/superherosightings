/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.controllers;

import com.e.superherosightings.dao.SightingDao;
import com.e.superherosightings.dto.Sighting;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Edward Chang
 */
@Controller
public class IndexController {
    
    @Autowired
    SightingDao sightingDao;
    
    @GetMapping("/")
    public String displayLastTen(Model model){
        List<Sighting> sightings = sightingDao.getLastTenSightings();
        
        model.addAttribute("sightings", sightings);
        return "index";
    }
}
