package com.example.funding.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CreatorType {
    GENERAL,
    INDIVIDUAL,
    CORPORATION;

    @JsonCreator
    public static CreatorType from(String v) {
        if (v == null) throw new IllegalArgumentException("creatorType is required");
        try { return CreatorType.valueOf(v.trim().toUpperCase()); }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("creatorType must be one of [GENERAL, INDIVIDUAL, CORPORATION]");
        }
    }
}
