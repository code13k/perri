package org.code13k.perri.model;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BasicModel implements Cloneable {
    // Logger
    private Logger mLogger = null;

    /**
     * getLogger();
     */
    public Logger getLogger() {
        if (mLogger == null) {
            mLogger = LoggerFactory.getLogger(this.getClass());
        }
        return mLogger;
    }

    /**
     * toMap()
     */
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Field[] fields = ((Object) this).getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (field.getName().equals("mLogger") == false) {
                        field.setAccessible(true);
                        result.put(field.getName(), field.get(this));
                        field.setAccessible(false);
                    }
                } catch (IllegalAccessException ex) {
                    // Nothing
                }
            }
        } catch (Exception e) {
            // Nothing
        } finally {
            return result;
        }
    }

    /**
     * toString()
     */
    @Override
    public String toString() {
        return toMap().toString();
    }

    /**
     * toJsonString()
     */
    public String toJsonString() {
        Map<String, Object> map = toMap();
        if (map != null) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(map);
            return jsonString;
        }
        return null;
    }

    /**
     * clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

