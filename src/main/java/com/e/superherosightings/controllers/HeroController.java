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
import com.e.superherosightings.dto.Organization;
import com.e.superherosightings.dto.Sighting;
import com.e.superherosightings.dto.Superpower;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
public class HeroController {
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
    
    @GetMapping("heroes")
    public String displayHeroes(Model model){
        List<Hero> heroes = heroDao.getAll();
        List<Superpower> superpowers = superpowerDao.getAll();
        
        model.addAttribute("heroes", heroes);
        model.addAttribute("superpowers", superpowers);
        return "heroes";
    }
    
    @PostMapping("addHero")
    public String addHero(Hero hero, HttpServletRequest request){
        int superpowerId = Integer.parseInt(request.getParameter("superpowerId"));
        
        hero.setSuperpowerId(superpowerId);
        hero.setSuperpower(superpowerDao.getSuperpowerById(superpowerId));
        heroDao.addHero(hero);
        
        return "redirect:/heroes";
    }
    
    @GetMapping("editHero")
    public String editHero(Integer id, Model model){
        List<Superpower> superpowers = superpowerDao.getAll();
        Hero hero = heroDao.getHeroById(id);
        
        model.addAttribute("hero",hero);
        model.addAttribute("superpowers", superpowers);
        return "editHero";
    }
    
    @PostMapping("editHero")
    public String performEditHero(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        Hero hero = heroDao.getHeroById(id);
        
        hero.setName(request.getParameter("name"));
        hero.setDescription(request.getParameter("description"));
        int superpowerId = Integer.parseInt(request.getParameter("superpowerId"));
        hero.setSuperpowerId(superpowerId);
        hero.setSuperpower(superpowerDao.getSuperpowerById(superpowerId));
        
        heroDao.updateHero(hero);
        
        return "redirect:/heroes";
    }
    
    @GetMapping("deleteHero")
    public String deleteHero(Integer id){
        Hero hero = heroDao.getHeroById(id);
        
        List<Organization> orgs = organizationDao.getOrganizationsForHero(hero);
        for(Organization org : orgs){
            if(org.getHeroes().size() <= 1){
                organizationDao.deleteOrganization(org.getId());
            }
        }
        
        List<Sighting> sightings  = sightingDao.getSightingsForHero(hero);
        for(Sighting sighting : sightings){
            if(sighting.getHeroes().size() <= 1){
                sightingDao.deleteSighting(sighting.getId());
            }
        }
        
        heroDao.deleteHero(id);
        return "redirect:/heroes";
    }
    
    @GetMapping("heroDetail")
    public String displayHeroDetails(Integer id, Model model){
        Hero hero = heroDao.getHeroById(id);
        List<Organization> organizations = organizationDao.getAll();
        List<Location> locations = locationDao.getLocationsForHero(hero);
        model.addAttribute("hero", hero);
        model.addAttribute("organizations", organizations);
        model.addAttribute("locations", locations);
        
        return "heroDetail";
    }
}
