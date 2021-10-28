/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.e.superherosightings.controllers;

import com.e.superherosightings.dao.LocationDao;
import com.e.superherosightings.dto.Location;
import java.math.BigDecimal;
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
public class LocationController {

    @Autowired
    LocationDao locationDao;
    
    @GetMapping("locations")
    public String displayLocations(Model model){
        List<Location> locations = locationDao.getAll();
        model.addAttribute("locations", locations);
        return "locations";
    }
    
    @PostMapping("addLocation")
    public String addLocation(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String latitudeString = request.getParameter("latitude");
        if(latitudeString.length() < 1){
            latitudeString = "0.000000";
        }
        BigDecimal latititude = new BigDecimal(latitudeString);
        String longitudeString = request.getParameter("longitude");
        if(longitudeString.length() < 1){
            longitudeString = "0.000000";
        }
        BigDecimal longitude = new BigDecimal(longitudeString);
        
        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        location.setLatitude(latititude);
        location.setLongitude(longitude);
        
        locationDao.addLocation(location);
        return "redirect:/locations";
    }
    
    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id){
        locationDao.deleteLocation(id);
        
        return "redirect:/locations";
    }
 
    @GetMapping("editLocation")
    public String editLocation(HttpServletRequest request, Model model){
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocationById(id);
        
        model.addAttribute("location", location);
        return "editLocation";
    }
    
    @PostMapping("editLocation")
    public String performEditLocation(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocationById(id);
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String latitudeString = request.getParameter("latitude");
        if(latitudeString.length() < 1){
            latitudeString = "0.000000";
        }
        String longitudeString = request.getParameter("longitude");
        if(longitudeString.length() < 1){
            longitudeString = "0.000000";
        }
        
        location.setName(name);
        location.setDescription(description);
        location.setLatitude(new BigDecimal(latitudeString));
        location.setLongitude(new BigDecimal(longitudeString));
        
        locationDao.updateLocation(location);
        
        return "redirect:/locations";
    }
}
