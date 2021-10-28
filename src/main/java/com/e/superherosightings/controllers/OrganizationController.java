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
import com.e.superherosightings.dto.Organization;
import java.util.ArrayList;
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
public class OrganizationController {
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
    
    @GetMapping("organizations")
    public String displayOrganizations(Model model){
        List<Organization> organizations = organizationDao.getAll();
        List<Hero> heroes = heroDao.getAll();
        
        model.addAttribute("organizations", organizations);
        model.addAttribute("heroes",heroes);
        return "organizations";
    }
    
    @PostMapping("addOrganization")
    public String addOrganization(Organization organization, HttpServletRequest request){        
        String [] heroIds = request.getParameterValues("heroId");
        List<Hero> heroes = new ArrayList<>();
        for(String id : heroIds){
            heroes.add(heroDao.getHeroById(Integer.parseInt(id)));
        }
        organization.setHeroes(heroes);
        
        organizationDao.addOrganization(organization);
        
        return "redirect:/organizations";
    }
    
    @GetMapping("deleteOrganization")
    public String deleteOrganization(Integer id){
        organizationDao.deleteOrganization(id);
        return "redirect:/organizations";
    }
    
    @GetMapping("editOrganization")
    public String editOrganization(Integer id, Model model){
        List<Hero> heroes = heroDao.getAll();
        Organization organization = organizationDao.getOrganizationById(id);
        
        model.addAttribute("organization", organization);
        model.addAttribute("heroes", heroes);
        return "editOrganization";
    }
    
    @PostMapping("editOrganization")
    public String performEditOrganization(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String [] heroIds = request.getParameterValues("heroes");
        
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationDao.getOrganizationById(id);
        organization.setName(name);
        organization.setDescription(description);
        organization.setAddress(address);
        organization.setEmail(email);
        organization.setPhone(phone);
        List<Hero> heroes = new ArrayList<>();
        for(String heroId : heroIds){
            heroes.add(heroDao.getHeroById(Integer.parseInt(heroId)));
        }
        organization.setHeroes(heroes);
        
        organizationDao.updateOrganization(organization);
        
        return "redirect:/organizations";
    }
    
    @GetMapping("organizationDetail")
    public String displayOrganizationDetails(Integer id, Model model){
        Organization organization = organizationDao.getOrganizationById(id);
        model.addAttribute("organization", organization);
        return "organizationDetail";
    }
}
