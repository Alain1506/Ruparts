package com.ruparts.helperclasses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class LibraryMaps {

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
    @JsonProperty("id_reference_type")
    public HashMap<String, String> idReferenceType;

    public LibraryMaps() {
        taskTypes = new HashMap<>();;
        userRoles = new HashMap<>();
        userRolesEditable = new HashMap<>();
        implementer = new HashMap<>();
        status = new HashMap<>();
        idReferenceType = new HashMap<>();
    }

}
