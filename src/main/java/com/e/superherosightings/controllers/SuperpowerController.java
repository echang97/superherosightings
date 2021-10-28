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
public class SuperpowerController {
    
    @Autowired
    SuperpowerDao superpowerDao;
    
    @GetMapping("superpowers")
    public String displaySuperpowers(Model model){
        List<Superpower> superpowers = superpowerDao.getAll();
        
        model.addAttribute("superpowers", superpowers);
        
        return "superpowers";
    }
    
    @PostMapping("addSuperpower")
    public String addSuperpower(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        Superpower superpower = new Superpower();
        superpower.setName(name);
        superpower.setDescription(description);
        
        superpowerDao.addSuperpower(superpower);
        return "redirect:/superpowers";
    }
    
    @GetMapping("editSuperpower")
    public String editSuperpower(HttpServletRequest request, Model model){
        int id = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        
        model.addAttribute("superpower", superpower);
        
        return "editSuperpower";
    }
    
    @PostMapping("editSuperpower")
    public String performEditSuperpower(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        
        superpower.setName(request.getParameter("name"));
        superpower.setDescription(request.getParameter("description"));
        
        superpowerDao.updateSuperpower(superpower);
        
        return "redirect:/superpowers";
    }
    
    @GetMapping("deleteSuperpower")
    public String deleteSuperpower(Integer id){
        superpowerDao.deleteSuperpower(id);
        
        return "redirect:/superpowers";
    }
}
