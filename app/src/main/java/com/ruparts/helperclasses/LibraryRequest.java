package com.ruparts.helperclasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LibraryRequest {
    @JsonProperty("id")
    public String libraryRequestId;
    @JsonProperty("action")
    public String libraryRequestAction;
    @JsonProperty("data")
    public String libraryRequestData;
}
