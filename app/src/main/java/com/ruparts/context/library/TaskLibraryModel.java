package com.ruparts.context.library;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;

public class TaskLibraryModel implements Serializable {

    @JsonProperty("task_types")
    public HashMap<String, String> taskTypes;
    @JsonProperty("user_roles")
    public HashMap<String, String> userRoles;
    @JsonProperty("user_roles_editable")
    public HashMap<String, String> userRolesEditable;
    @JsonProperty("implementer")
    public HashMap<String, String> implementer;
    @JsonProperty("status")
    public HashMap<String, String> status;
    @JsonProperty("attribute_type")
    public HashMap<String, String> attributeType;
    @JsonProperty("id_reference_type")
    public HashMap<String, String> idReferenceType;

    public TaskLibraryModel() {
        taskTypes = new HashMap<>();
        userRoles = new HashMap<>();
        userRolesEditable = new HashMap<>();
        implementer = new HashMap<>();
        status = new HashMap<>();
        attributeType = new HashMap<>();
        idReferenceType = new HashMap<>();
    }

}
