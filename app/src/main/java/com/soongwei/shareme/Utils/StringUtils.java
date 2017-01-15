package com.soongwei.shareme.Utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by SoongWei on 15-Jan-17.
 */

public class StringUtils {

    public static String convertObjectToString(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        }catch (JsonMappingException e){
            Log.w(StringUtils.class.getSimpleName(),"convertObjectToString", e );
        }catch (JsonProcessingException e){
            Log.w(StringUtils.class.getSimpleName(),"convertObjectToString", e );
        }

        return "";
    }

    public static  Object convertStringToObject(String value, Class<?> classOfT){

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(value, classOfT);
        }catch (IOException e){
            Log.w(StringUtils.class.getSimpleName(),"convertStringToObject", e );
        }

        return null;

    }
}
