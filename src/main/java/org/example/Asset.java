package org.example;

import com.owlike.genson.annotation.JsonProperty;

public class Asset {
    final String value;

    public Asset(@JsonProperty("value") String value) {
        this.value = value;
    }
}
