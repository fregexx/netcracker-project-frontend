package ru.vsu.netcracker.parking.backend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vsu.netcracker.parking.backend.models.Obj;
import ru.vsu.netcracker.parking.backend.services.ObjService;

@Controller
@RequestMapping(value = "/objects")
public class ObjController {

    @Autowired
    private ObjService objService;

    @GetMapping("/{objectId}")
    public ModelAndView getObj(@PathVariable long objectId) {
        Obj obj = objService.getObj(objectId);
        return new ModelAndView("objInfo", "obj", obj);
    }

    @PostMapping("")
    public String deleteObj(@ModelAttribute("obj") Obj obj, @RequestParam("button") String submitValue) {
        if (submitValue.equalsIgnoreCase("Save")) {
            objService.updateObj(obj);
            return "redirect:objects/" + obj.getId();
        } else if (submitValue.equalsIgnoreCase("Delete")) {
            objService.deleteObj(obj.getId());
            return "Object_deleted";
        }
        return "error";
    }

    @GetMapping(value = "/json/{objectId}", produces = "application/json")
    @ResponseBody
    public JsonNode getObjAsJson(@PathVariable long objectId){
        return objService.getObjAsJson(objectId);
    }

    @GetMapping(value = "/json/users", produces = "application/json")
    @ResponseBody
    public ArrayNode getAllUsersAsJson(){
        return objService.getAllObjAsJson("User");
    }
}
