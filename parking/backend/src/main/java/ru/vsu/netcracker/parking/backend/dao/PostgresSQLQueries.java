package ru.vsu.netcracker.parking.backend.dao;

public class PostgresSQLQueries {

    public static final String GET_GUID = "SELECT generate_guid();";

    public static final String GET_PARKING_ID = "SELECT nextval('parking_id_sequence')";

    public static final String INSERT_OBJECT_QUERY =
            "INSERT INTO \"OBJECTS\"(object_id, object_type_id, name, description)\n" +
                    "VALUES (?, ?, ?, ?, ?)";

    //----удаление объекта -----toDo
    public static final String DELETE_OBJECT_QUERY =
            "DELETE FROM public.\"OBJECTS\"\n" +
                    "WHERE object_id = ?";

    public static final String GET_LIST_OF_PARKING_SPOTS_BY_CUSTOMER_ID_QUERY =
            "SELECT object_id FROM \"REFERENCES\"\n" +
                    "WHERE reference = ?";

    public static final String CHANGE_PARKING_STATUS_QUERY =
            "UPDATE \"PARAMS\"\n" +
                    "SET list_value_id = ?\n" +
                    "WHERE attr_id = ? AND object_id = ?";
    //-----конец удаления объекта-----

    public static final String UPDATE_OBJECT_NAME_QUERY =
            "UPDATE \"OBJECTS\"\n" +
                    "SET name=?\n" +
                    "WHERE object_id=?";

    public static final String GET_PARAM_QUERY =
            "SELECT value\n" +
                    "FROM \"PARAMS\"\n" +
                    "WHERE attr_id = ? AND object_id = ?";

    public static final String SET_PARAM_QUERY =
            "INSERT INTO \"PARAMS\" (attr_id, object_id, value) values (?, ?, ?)\n" +
                    "ON CONFLICT (attr_id, object_id) DO UPDATE SET value = ?";

    public static final String GET_DATE_PARAM_QUERY =
            "SELECT date_value\n" +
                    "FROM \"PARAMS\"\n" +
                    "WHERE attr_id = ? AND object_id = ?";

    public static final String SET_DATE_PARAM_QUERY =
            "INSERT INTO \"PARAMS\" (attr_id, object_id, date_value) values (?, ?, ?)\n" +
                    "ON CONFLICT (attr_id, object_id) DO UPDATE SET date_value = ?;";

    public static final String GET_LIST_PARAM_QUERY =
            "SELECT  lv.value\n" +
                    "FROM   \"PARAMS\" p\n" +
                    "JOIN \"LIST_VALUES\" lv ON p.list_value_id = lv.list_value_id\n" +
                    "WHERE p.attr_id = ? AND p.object_id = ?";

    public static final String SET_LIST_PARAM_QUERY =
            "INSERT INTO \"PARAMS\" (attr_id, object_id, list_value_id) values (?, ?, (SELECT  lv.list_value_id\n" +
                    "                FROM \"LIST_VALUES\" lv\n" +
                    "                WHERE lv.attr_id = ? AND lv.value = ?))\n" +
                    "ON CONFLICT (attr_id, object_id) DO UPDATE SET list_value_id = (SELECT  lv.list_value_id\n" +
                    "                FROM \"LIST_VALUES\" lv\n" +
                    "                WHERE lv.attr_id = ? AND lv.value = ?)";

    public static final String GET_REFERENCE_PARAM_QUERY =
            "SELECT reference\n" +
                    "FROM \"REFERENCES\" r\n" +
                    "WHERE r.attr_id = ? AND r.object_id = ?";

    public static final String SET_REFERENCE_PARAM_QUERY =
            "INSERT INTO \"REFERENCES\" (attr_id, reference, object_id) values (?, ?, ?)\n" +
                    "ON CONFLICT (attr_id, reference, object_id) DO UPDATE SET reference = ?";

    public static final String GET_OBJECT_INFO_BY_ID_QUERY =
            "SELECT o.name,\n" +
                    "o.object_type_id,\n" +
                    "o.description\n" +
                    "FROM \"OBJECTS\" o\n" +
                    "WHERE o.object_id = ?";

    public static final String GET_VALUES_BY_OBJECT_ID_QUERY =
            "SELECT  a.attr_id,\n" +
                    "a.name,\n" +
                    "p.value,\n" +
                    "p.date_value,\n" +
                    "lv.value as list_value\n" +
                    "FROM   \"ATTRIBUTES\" a \n" +
                    "JOIN \"PARAMS\" p ON a.attr_id = p.attr_id AND p.object_id = ?\n" +
                    "LEFT JOIN \"LIST_VALUES\" lv ON p.list_value_id = lv.list_value_id";

    public static final String GET_REFERENCE_VALUES_BY_OBJECT_ID_QUERY =
            "SELECT  a.attr_id,\n" +
                    "a.name,\n" +
                    "ref.reference,\n" +
                    "o.name as referenced_object_name\n" +
                    "FROM \"ATTRIBUTES\" a\n" +
                    "JOIN \"REFERENCES\" ref ON ref.object_id = ? AND a.attr_id = ref.attr_id\n" +
                    "JOIN \"OBJECTS\" o ON ref.object_id = ? AND ref.reference = o.object_id";

    public static final String GET_LIST_OF_OBJECTS_BY_OBJECT_TYPE =
            "SELECT object_id FROM \"OBJECTS\" o \n" +
                    "JOIN \"OBJECT_TYPES\" ot ON o.object_type_id = ot.object_type_id AND ot.name = ?";
}
