package ru.vsu.netcracker.parking.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.netcracker.parking.backend.dao.ObjectsDAO;
import ru.vsu.netcracker.parking.backend.models.Obj;

@Service
public class ObjService {

    private ObjectsDAO dao;

    @Autowired
    public ObjService(ObjectsDAO objectsDAO){
        this.dao = objectsDAO;
    }

    public Obj getObj(long objectId){
        return dao.getObj(objectId);
    }

    public void updateObj(Obj obj){
        dao.updateObj(obj);
    }

    public void deleteObj(long objectId){
        dao.delete(objectId);
    }

    public JsonNode getObjAsJson(long objectId){
        return dao.getObjAsJSON(objectId);
    }

    public ArrayNode getAllObjAsJson(String objectType){
        return dao.getAllObjAsJSON(objectType);
    }
}
