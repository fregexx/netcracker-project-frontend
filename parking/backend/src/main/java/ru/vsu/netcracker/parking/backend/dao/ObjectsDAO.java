package ru.vsu.netcracker.parking.backend.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.vsu.netcracker.parking.backend.models.Obj;

public interface ObjectsDAO {

    public void createObj(Obj obj);
    public void updateObj(Obj obj);
    public void delete(long objectId);
    public Obj getObj(long objectId);
    public JsonNode getObjAsJSON(long objectId);
    public ArrayNode getAllObjAsJSON(String objectType);
}