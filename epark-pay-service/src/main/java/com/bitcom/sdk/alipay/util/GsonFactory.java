package com.bitcom.sdk.alipay.util;

import com.bitcom.sdk.alipay.model.hb.EquipStatus;
import com.bitcom.sdk.alipay.model.hb.EquipStatusAdapter;
import com.bitcom.sdk.alipay.model.hb.ExceptionInfo;
import com.bitcom.sdk.alipay.model.hb.ExceptionInfoAdapter;
import com.bitcom.sdk.alipay.model.hb.TradeInfo;
import com.bitcom.sdk.alipay.model.hb.TradeInfoAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonFactory {
    public static Gson getGson() {
        return GsonHolder.gson;
    }

    private static class GsonHolder {
        private static Type exceptionListType = new TypeToken() {
        }
                .getType();
        private static Type tradeInfoListType = new TypeToken() {
        }
                .getType();

        private static Gson gson = new GsonBuilder()
                .registerTypeAdapter(exceptionListType, new ExceptionInfoAdapter())
                .registerTypeAdapter(tradeInfoListType, new TradeInfoAdapter())
                .registerTypeAdapter(EquipStatus.class, new EquipStatusAdapter())
                .create();
    }
}