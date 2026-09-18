package dev.hexworks.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class PlayerData {

    private final Map<String, Object> data = new HashMap<>();

    public void setString(String key, String value) {
        data.put(key, value);
    }

    public String getString(String key) {
        Object value = data.get(key);

        if (value instanceof String stringValue) {
            return stringValue;
        }

        return null;
    }

    public void setInt(String key, int value) {
        data.put(key, value);
    }

    public int getInt(String key) {
        Object value = data.get(key);

        if (value instanceof Integer intValue) {
            return intValue;
        }

        return 0;
    }

    public void setBoolean(String key, boolean value) {
        data.put(key, value);
    }

    public boolean getBoolean(String key) {
        Object value = data.get(key);

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        return false;
    }

    public void setDouble(String key, double value) {
        data.put(key, value);
    }

    public double getDouble(String key) {
        Object value = data.get(key);

        if (value instanceof Double doubleValue) {
            return doubleValue;
        }

        return 0.0;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public void remove(String key) {
        data.remove(key);
    }
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(data);
    }
    public void set(String key, Object value) {
        data.put(key, value);
    }

}