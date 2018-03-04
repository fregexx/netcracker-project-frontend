package ru.vsu.netcracker.parking.backend.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.vsu.netcracker.parking.backend.models.Obj;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class JsonConverter {

    /**
     * toDo
     * @param obj
     * @param attrNames
     * @param refNames
     * @return
     */
    public static JsonNode writeJson(Obj obj, Map<Long, String> attrNames, Map<Long, String> refNames) {

        JsonNodeFactory factory = new JsonNodeFactory(false);
        ObjectNode json = factory.objectNode();

        json.put("id", obj.getId());
        json.put("name", obj.getName());
        json.put("typeId", obj.getTypeId());
        json.put("description", obj.getDescription());

        ArrayNode values = factory.arrayNode();
        if (!obj.getValues().isEmpty()) {
            obj.getValues().forEach((k, v) -> {
                ObjectNode value = factory.objectNode();
                value.put("id", k);
                value.put("name", attrNames.get(k));
                value.put("value", v);
                values.add(value);
            });
            json.put("values", values);
        }

        ArrayNode dateValues = factory.arrayNode();
        if (!obj.getDateValues().isEmpty()) {
            obj.getDateValues().forEach((k, v) -> {
                ObjectNode value = factory.objectNode();
                value.put("id", k);
                value.put("name", attrNames.get(k));
                value.put("value", v.toString());
                dateValues.add(value);
            });
            json.put("dateValues", dateValues);
        }

        ArrayNode listValues = factory.arrayNode();
        if (!obj.getListValues().isEmpty()) {
            obj.getListValues().forEach((k, v) -> {
                ObjectNode value = factory.objectNode();
                value.put("id", k);
                value.put("name", attrNames.get(k));
                value.put("value", v);
                listValues.add(value);
            });
            json.put("listValues", listValues);
        }

        ArrayNode references = factory.arrayNode();
        if (!obj.getReferences().isEmpty()) {
            obj.getReferences().forEach((k, v) -> {
                ObjectNode value = factory.objectNode();
                value.put("id", k);
                value.put("name", attrNames.get(k));
                value.put("reference", v);
                value.put("referenceName", refNames.get(k));
                references.add(value);
            });
            json.put("references", references);
        }
        // HATEOAS
        ArrayNode links = addLinksToJson(obj);
        json.put("links", links);

        return json;
    }

    /**
     * toDo
     * @param obj
     * @return
     */
    private static ArrayNode addLinksToJson(Obj obj) {
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ArrayNode links = factory.arrayNode();
        ObjectNode link = factory.objectNode();

        if (obj.getTypeId() == 2) {
            link.put("rel", "self");
            link.put("href", "http://localhost:8080/backend/profiles/" + obj.getId());
            links.add(link);
        } else if (obj.getTypeId() == 3) {
            link = factory.objectNode();
            link.put("rel", "self");
            link.put("href", "http://localhost:8080/backend/parkings/" + obj.getId());
            links.add(link);
            link = factory.objectNode();
            link.put("rel", "owner");
            link.put("href", "http://localhost:8080/backend/profiles/" + obj.getReferences().get(Long.valueOf(300)));
            links.add(link);
        }
        return links;
    }

    /**
     * toDo
     * @param json
     * @return
     */
    public static Obj readJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser(json);
            JsonNode root = mapper.readTree(parser);

            long id = root.path("id").asLong();
            String name = root.path("name").asText();
            long typeId = root.path("typeId").asLong();
            String description = root.path("description").asText();
            Map<Long, String> values = new HashMap<>();
            Map<Long, Timestamp> dateValues = new HashMap<>();
            Map<Long, String> listValues = new HashMap<>();
            Map<Long, Long> references = new HashMap<>();

            JsonNode valuesNode = root.path("values");
            for (JsonNode value : valuesNode) {
                values.put(value.path("id").asLong(), value.path("value").asText());
            }

            JsonNode dateValuesNode = root.path("dateValues");
            for (JsonNode value : dateValuesNode) {
                String date = value.path("value").asText();
                dateValues.put(value.path("id").asLong(), Timestamp.valueOf(date));
            }

            JsonNode listValuesNode = root.path("listValues");
            for (JsonNode value : listValuesNode) {
                listValues.put(value.path("id").asLong(), value.path("value").asText());
            }

            JsonNode referencesNode = root.path("references");
            for (JsonNode value : referencesNode) {
                references.put(value.path("id").asLong(), value.path("reference").asLong());
            }

            Obj obj = new Obj(id, name, typeId);
            obj.setDescription(description);
            obj.setValues(values);
            obj.setDateValues(dateValues);
            obj.setListValues(listValues);
            obj.setReferences(references);

            return obj;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
