
/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 *
 * THIS FILE IS GENERATED BY SCRIPT. DO NOT EDIT IT.
 */

package com.coinport.f1.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.coinport.f1.DOW;

public class DOWAdapter implements JsonSerializer<DOW>,
        JsonDeserializer<DOW> {

    @Override
    public JsonElement serialize(DOW state, Type arg1,
            JsonSerializationContext arg2) {
        return new JsonPrimitive(state.ordinal());
    }

    @Override
    public DOW deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        return DOW.findByValue(json.getAsInt());
    }
}
