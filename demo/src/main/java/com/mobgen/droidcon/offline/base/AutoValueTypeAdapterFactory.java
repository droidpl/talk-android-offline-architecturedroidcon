package com.mobgen.droidcon.offline.base;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class AutoValueTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        AutoGson annotation = rawType.getAnnotation(AutoGson.class);
        if(annotation == null) {
            return null;
        }
        return (TypeAdapter<T>) gson.getAdapter(annotation.autoClass());
    }
}