package utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JSONUtils {

    public static JSONObject getJSON(String fileName) {
        try {
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(new FileReader("src/main/resources/testdata/" + fileName));
        } catch (Exception e) {
            throw new RuntimeException("JSON file read error: " + fileName);
        }
    }

    public static JSONObject getJSONObject(String fileName, String objectKey) {
        Object object = getJSON(fileName).get(objectKey);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new RuntimeException("JSON object not found: " + objectKey + " in file: " + fileName);
    }

    public static JSONArray getJSONArray(String fileName, String arrayKey) {
        Object object = getJSON(fileName).get(arrayKey);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        throw new RuntimeException("JSON array not found: " + arrayKey + " in file: " + fileName);
    }

    public static String getString(JSONObject jsonObject, String key) {
        Object value = jsonObject.get(key);
        if (value == null) {
            throw new RuntimeException("JSON value not found for key: " + key);
        }
        return String.valueOf(value);
    }
}
