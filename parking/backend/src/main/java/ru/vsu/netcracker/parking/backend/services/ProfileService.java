package ru.vsu.netcracker.parking.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.netcracker.parking.backend.dao.ObjectsDAO;
import ru.vsu.netcracker.parking.backend.models.Obj;

@Service
public class ProfileService {

    private ObjectsDAO dao;

    @Autowired
    public ProfileService(ObjectsDAO objectsDAO){
        this.dao = objectsDAO;
    }

    public JsonNode getProfileAsJson(long profileId){
        return dao.getObjAsJSON(profileId);
    }

    public ArrayNode getAllProfilesAsJson(){
        return dao.getAllObjAsJSON("User");
    }

    public void updateProfile(Obj obj){
        dao.updateObj(obj);
    }

    public void deleteProfile(long profileId){
        dao.delete(profileId);
    }
}