package com.example.navigationdrawer.helperclasses;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class LibraryMaps {

    @JsonProperty("task_types")
    public HashMap<String, String> task_types;
    @JsonProperty("user_roles")
    public HashMap<String, String> user_roles;
    @JsonProperty("user_roles_editable")
    public HashMap<String, String> user_roles_editable;
    @JsonProperty("implementer")
    public HashMap<String, String> implementer;
    @JsonProperty("tatus")
    public HashMap<String, String> status;
    @JsonProperty("id_reference_type")
    public HashMap<String, String> id_reference_type;

    public LibraryMaps() {
        task_types = new HashMap<>();;
        user_roles = new HashMap<>();
        user_roles_editable = new HashMap<>();
        implementer = new HashMap<>();
        status = new HashMap<>();
        id_reference_type = new HashMap<>();
    }

}
