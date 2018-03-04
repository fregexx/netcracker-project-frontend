package ru.vsu.netcracker.parking.backend.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vsu.netcracker.parking.backend.json.JsonConverter;
import ru.vsu.netcracker.parking.backend.models.Obj;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostgresObjectsDAO implements ObjectsDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Get new GUID from database function in format "YYMMDDSSMS + random'
     *
     * @return GUID
     */
    private Long getGUID() {
        return this.jdbcTemplate.queryForObject(PostgresSQLQueries.GET_GUID, Long.class);
    }

    /**
     * Get next value from database sequence for Parking objects
     *
     * @return new Parking Id
     */
    private Long getParkingId() {
        return this.jdbcTemplate.queryForObject(PostgresSQLQueries.GET_PARKING_ID, Long.class);
    }

    /**
     * Inserts values, On conflict updates values
     * @param obj
     */
    private void insertOrUpdateValues(Obj obj){
        if (obj.getValues() != null) {
            obj.getValues().forEach((attrId, value) -> {
                jdbcTemplate.update(PostgresSQLQueries.SET_PARAM_QUERY, new Object[]{attrId, obj.getId(), value, value});
            });
        }
        if (obj.getDateValues() != null) {
            obj.getDateValues().forEach((attrId, value) -> {
                jdbcTemplate.update(PostgresSQLQueries.SET_DATE_PARAM_QUERY, new Object[]{attrId, obj.getId(), value, value});
            });
        }
        if (obj.getListValues() != null) {
            obj.getListValues().forEach((attrId, value) -> {
                jdbcTemplate.update(PostgresSQLQueries.SET_LIST_PARAM_QUERY, new Object[]{attrId, obj.getId(), attrId, value, attrId, value});
            });
        }
        if (obj.getReferences() != null) {
            obj.getReferences().forEach((attrId, value) -> {
                jdbcTemplate.update(PostgresSQLQueries.SET_REFERENCE_PARAM_QUERY, new Object[]{attrId, value, obj.getId(), value});
            });
        }
    }

    /**
     * toDo
     *
     * @param obj
     */
    @Override
    public void createObj(Obj obj) {
        Long objectId = getGUID();
        if (obj.getTypeId() == 3) {
            obj.setName(getParkingId().toString());    //  sets unique id from db sequence for parking object
        }
        jdbcTemplate.update(PostgresSQLQueries.INSERT_OBJECT_QUERY, new Object[]{objectId, obj.getTypeId(), obj.getName(), obj.getDescription()});

        insertOrUpdateValues(obj);
    }

    /**
     * toDo
     *
     * @param obj
     */
    @Override
    public void updateObj(Obj obj) {
        if (obj.getTypeId() == 2) {   // sets new name for User
            jdbcTemplate.update(PostgresSQLQueries.UPDATE_OBJECT_NAME_QUERY, new Object[]{obj.getName(), obj.getId()});
        }
        insertOrUpdateValues(obj);
    }

    /**
     * toDo
     *
     * @param objectId
     */
    @Override
    public void delete(long objectId) {
        List<Long> listOfParkingSpots = jdbcTemplate.queryForList(PostgresSQLQueries.GET_LIST_OF_PARKING_SPOTS_BY_CUSTOMER_ID_QUERY, new Object[]{objectId}, Long.class);
        if (listOfParkingSpots.size() > 0) {  //если пользователь арендовал парковки, освободить их
            for (Long spotId : listOfParkingSpots) {
                jdbcTemplate.update(PostgresSQLQueries.CHANGE_PARKING_STATUS_QUERY, new Object[]{2, 503, spotId});
            }
        }
        jdbcTemplate.update(PostgresSQLQueries.DELETE_OBJECT_QUERY, new Object[]{objectId});
    }

    /**
     * Reads basic information about object such as name, objectTypeId and description from the database by specified objectId
     *
     * @param objectId
     * @return Obj populated with basic information such as name, objectTypeId, parentId and description
     */
    private Obj getBasicObjInfo(long objectId) {
        return this.jdbcTemplate.queryForObject(PostgresSQLQueries.GET_OBJECT_INFO_BY_ID_QUERY, new Object[]{objectId}, (resultSet, i) -> {
            String name = resultSet.getString("name");
            long typeId = resultSet.getLong("object_type_id");
            String description = resultSet.getString("description");
            Obj object = new Obj(objectId, name, typeId);
            object.setDescription(description);
            return object;
        });
    }

    /**
     * toDo
     *
     * @param objectId
     * @return Obj
     */
    @Override
    public Obj getObj(long objectId) {
        Obj obj = getBasicObjInfo(objectId);
        Map<Long, String> values = new HashMap<>();
        Map<Long, Timestamp> dateValues = new HashMap<>();
        Map<Long, String> listValues = new HashMap<>();
        Map<Long, Long> references = new HashMap<>();

        this.jdbcTemplate.query(PostgresSQLQueries.GET_VALUES_BY_OBJECT_ID_QUERY, new Object[]{objectId}, resultSet -> {
            while (resultSet.next()) {
                if (resultSet.getString("value") != null) {
                    values.put(resultSet.getLong("attr_id"), resultSet.getString("value"));
                } else if (resultSet.getString("date_value") != null) {
                    dateValues.put(resultSet.getLong("attr_id"), resultSet.getTimestamp("date_value"));
                } else {
                    listValues.put(resultSet.getLong("attr_id"), resultSet.getString("list_value"));
                }
            }
            return null;
        });
        jdbcTemplate.query(PostgresSQLQueries.GET_REFERENCE_VALUES_BY_OBJECT_ID_QUERY, new Object[]{objectId, objectId}, resultSet -> {
            while (resultSet.next()) {
                references.put(resultSet.getLong("attr_id"), resultSet.getLong("reference"));
            }
            return null;
        });
        obj.setValues(values);
        obj.setDateValues(dateValues);
        obj.setListValues(listValues);
        obj.setReferences(references);
        return obj;
    }

    /**
     * toDo
     *
     * @param objectId
     * @return JsonNode
     */
    @Override
    public JsonNode getObjAsJSON(long objectId) {
        Obj obj = getBasicObjInfo(objectId);
        Map<Long, String> values = new HashMap<>();
        Map<Long, Timestamp> dateValues = new HashMap<>();
        Map<Long, String> listValues = new HashMap<>();
        Map<Long, Long> references = new HashMap<>();
        Map<Long, String> refNames = new HashMap<>();
        Map<Long, String> attrNames = new HashMap<>();

        jdbcTemplate.query(PostgresSQLQueries.GET_VALUES_BY_OBJECT_ID_QUERY, new Object[]{objectId}, resultSet -> {
            while (resultSet.next()) {
                if (resultSet.getString("value") != null) {
                    values.put(resultSet.getLong("attr_id"), resultSet.getString("value"));
                } else if (resultSet.getString("date_value") != null) {
                    dateValues.put(resultSet.getLong("attr_id"), resultSet.getTimestamp("date_value"));
                } else {
                    listValues.put(resultSet.getLong("attr_id"), resultSet.getString("list_value"));
                }
                attrNames.put(resultSet.getLong("attr_id"), resultSet.getString("name"));
            }
            return null;
        });

        jdbcTemplate.query(PostgresSQLQueries.GET_REFERENCE_VALUES_BY_OBJECT_ID_QUERY, new Object[]{objectId, objectId}, resultSet -> {
            while (resultSet.next()) {
                references.put(resultSet.getLong("attr_id"), resultSet.getLong("reference"));
                refNames.put(resultSet.getLong("attr_id"), resultSet.getString("referenced_object_name"));
                attrNames.put(resultSet.getLong("attr_id"), resultSet.getString("name"));
            }
            return null;
        });
        obj.setValues(values);
        obj.setDateValues(dateValues);
        obj.setListValues(listValues);
        obj.setReferences(references);

        JsonConverter jsonConverter = new JsonConverter();
        return jsonConverter.writeJson(obj, attrNames, refNames);
    }

    /**
     * toDo
     *
     * @param objectType
     * @return
     */
    @Override
    public ArrayNode getAllObjAsJSON(String objectType) {
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ArrayNode jsonObjectsList = factory.arrayNode();
        List<Long> objectsList = jdbcTemplate.queryForList(PostgresSQLQueries.GET_LIST_OF_OBJECTS_BY_OBJECT_TYPE, new Object[]{objectType}, Long.class);
        for (Long objectId : objectsList) {
            JsonNode node = getObjAsJSON(objectId);
            jsonObjectsList.add(node);
        }
        return jsonObjectsList;
    }
}
