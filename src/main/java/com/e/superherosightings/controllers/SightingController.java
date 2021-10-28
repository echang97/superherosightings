/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.controllers;

import com.e.superherosightings.dao.HeroDao;
import com.e.superherosightings.dao.LocationDao;
import com.e.superherosightings.dao.OrganizationDao;
import com.e.superherosightings.dao.SightingDao;
import com.e.superherosightings.dao.SuperpowerDao;
import com.e.superherosightings.dto.Hero;
import com.e.superherosightings.dto.Location;
import com.e.superherosightings.dto.Sighting;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Edward Chang
 */
@Controller
public class SightingController {
    
    Set<ConstraintViolation<Sighting>> violations = new HashSet<>();
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @GetMapping("sightings")
    public String displaySightings(Model model){
        List<Sighting> sightings = sightingDao.getAll();
        List<Hero> heroes = heroDao.getAll();
        List<Location> locations = locationDao.getAll();
        
        model.addAttribute("errors", violations);
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("sightings", sightings);
        
        return "sightings";
    }
    
    @PostMapping("addSighting")
    public String addSighting(HttpServletRequest request, Model model){
        String [] heroIds = request.getParameterValues("heroIds");
        int locationId = Integer.parseInt(request.getParameter("locationId"));
        
        Sighting sighting = new Sighting();
        sighting.setDate(LocalDate.parse(request.getParameter("dateString")));
        sighting.setLocation(locationDao.getLocationById(locationId));
        List<Hero> heroes = new ArrayList<>();
        for(String id : heroIds){
            heroes.add(heroDao.getHeroById(Integer.parseInt(id)));
        }
        sighting.setHeroes(heroes);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);
        
        if(violations.isEmpty()){
            sightingDao.addSighting(sighting);
        
            return "redirect:/sightings";
        }
        
        model.addAttribute("errors", violations);
        model.addAttribute("locations", locationDao.getAll());
        model.addAttribute("heroes", heroDao.getAll());
        model.addAttribute("sightings", sightingDao.getAll());
        return "sightings";
    }
    
    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model){
        List<Hero> heroes = heroDao.getAll();
        List<Location> locations = locationDao.getAll();
        Sighting sighting = sightingDao.getSightingById(id);
        
        model.addAttribute("errors", violations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("sighting", sighting);

        return "editSighting";
    }
    
    @PostMapping("editSighting")
    public String performEditSighting(HttpServletRequest request, Model model){
        String locationId = request.getParameter("locationId");
        String [] heroIds = request.getParameterValues("heroIds");
        String date = request.getParameter("dateString");
        
        int id = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = sightingDao.getSightingById(id);
        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        sighting.setDate(LocalDate.parse(date));
        List<Hero> heroes = new ArrayList<>();
        for(String heroId : heroIds){
            heroes.add(heroDao.getHeroById(Integer.parseInt(heroId)));
        }
        sighting.setHeroes(heroes);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);
        
        if(violations.isEmpty()){
            sightingDao.updateSighting(sighting);
        
            return "redirect:/sightings";
        }
        
        model.addAttribute("errors", violations);
        model.addAttribute("locations", locationDao.getAll());
        model.addAttribute("heroes", heroDao.getAll());
        model.addAttribute("sighting", sighting);
        return "editSighting";
    }
    
    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id){
        sightingDao.deleteSighting(id);
        return "redirect:/sightings";
    }
    
    @GetMapping("sightingDetail")
    public String displaySightingDetails(Integer id, Model model){
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute(sighting);
        return "sightingDetail";
    }
    
    @GetMapping("displaySightingsByDate")
    public String displaySightingsByDate(HttpServletRequest request, Model model){
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        List<Sighting> sightings = sightingDao.getSightingsForDate(date);
        List<Location> locations = locationDao.getAll();
        
        model.addAttribute("errors", violations);
        model.addAttribute("locations", locations);
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroDao.getAll());
        
        return "sightings";
    }
}
