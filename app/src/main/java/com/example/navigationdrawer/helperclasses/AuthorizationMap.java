package com.example.navigationdrawer.helperclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationMap {

    public Map<String, String> authCodes;

    public AuthorizationMap(){
        authCodes = new HashMap<>();
        authCodes.put("mypass", "012345");
    }

}
