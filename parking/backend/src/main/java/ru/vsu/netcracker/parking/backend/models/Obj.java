package ru.vsu.netcracker.parking.backend.models;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@Component
public class Obj {

    private long id;
    private String name;
    private long typeId;
    private String description;

    private Map<Long, String> values;
    private Map<Long, Timestamp> dateValues;
    private Map<Long, String> listValues;
    private Map<Long, Long> references;

    public Obj() {
    }

    public Obj(long id, String name, long typeId) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long objectId) {
        this.id = objectId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long objectTypeId) {
        this.typeId = objectTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Long, String> getValues() {
        return values;
    }

    public void setValues(Map<Long, String> values) {
        this.values = values;
    }

    public Map<Long, Timestamp> getDateValues() {
        return dateValues;
    }

    public void setDateValues(Map<Long, Timestamp> dateValues) {
        this.dateValues = dateValues;
    }

    public Map<Long, String> getListValues() {
        return listValues;
    }

    public void setListValues(Map<Long, String> listValues) {
        this.listValues = listValues;
    }

    public Map<Long, Long> getReferences() {
        return references;
    }

    public void setReferences(Map<Long, Long> references) {
        this.references = references;
    }

    public void setValue(long attribute, String value){
        this.values.put(attribute,value);
    }

    public String getValue(long attribute){
        return values.get(attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obj obj = (Obj) o;
        return typeId == obj.typeId &&
                Objects.equals(id, obj.id) &&
                Objects.equals(name, obj.name) &&
                Objects.equals(description, obj.description) &&
                Objects.equals(values, obj.values) &&
                Objects.equals(dateValues, obj.dateValues) &&
                Objects.equals(listValues, obj.listValues) &&
                Objects.equals(references, obj.references);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, typeId, description, values, dateValues, listValues, references);
    }

    @Override
    public String toString() {
        return "Obj{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", typeId=" + typeId +
                ", description='" + description + '\'' +
                ", values=" + values +
                ", dateValues=" + dateValues +
                ", listValues=" + listValues +
                ", references=" + references +
                '}';
    }
}
