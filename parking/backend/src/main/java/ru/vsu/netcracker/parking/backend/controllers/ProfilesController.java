package ru.vsu.netcracker.parking.backend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vsu.netcracker.parking.backend.services.ProfileService;

@RestController
@RequestMapping(value = "/profiles")
public class ProfilesController {

    @Autowired
    private ProfileService profileService;

    @GetMapping(value = "/{profileId}", produces = "application/json")
    public JsonNode getProfile(@PathVariable long profileId){
        return profileService.getProfileAsJson(profileId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ArrayNode getAllProfiles(){
        return profileService.getAllProfilesAsJson();
    }

}