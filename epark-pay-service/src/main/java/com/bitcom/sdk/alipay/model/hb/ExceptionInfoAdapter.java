package com.bitcom.sdk.alipay.model.hb;

import com.bitcom.sdk.alipay.util.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class ExceptionInfoAdapter
        implements JsonSerializer<List<ExceptionInfo>> {
    public JsonElement serialize(List<ExceptionInfo> exceptionInfos, Type type, JsonSerializationContext jsonSerializationContext) {
        if (Utils.isListEmpty(exceptionInfos)) {
            return null;
        }

        return (JsonElement) new JsonPrimitive(StringUtils.join(exceptionInfos, "|"));
    }
}



