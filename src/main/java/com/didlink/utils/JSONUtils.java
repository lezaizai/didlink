package com.didlink.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class JSONUtils {
    
    public static Object toJSON(Object object) throws JSONException {
        if (object instanceof HashMap) {
            JSONObject json = new JSONObject();
            HashMap map = (HashMap) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable)object)) {
                json.put(value);
            }
            return json;
        } else {
            return object;
        }
    }

    public static boolean isEmptyObject(JSONObject object) {
        return object.names() == null;
    }

    public static HashMap<String, String> getMap(JSONObject object, String key) throws JSONException {
        return toMap(object.getJSONObject(key));
    }

    public static ArrayList getList(JSONObject object, String key) throws JSONException {
        return toList(object.getJSONArray(key));
    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, (String)fromJson(object.get(key)));
        }
        return map;
    }

    public static ArrayList toList(JSONArray array) throws JSONException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }
}
