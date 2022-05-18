package com.alberto.twitter.util;

import lombok.experimental.UtilityClass;
import org.json.JSONObject;

@UtilityClass
public class JsonNodeUtil {

  public static <T> T getJsonObject(JSONObject jsonObject, String... path) {
    Object innerObject = jsonObject;
    for(String route : path){
      innerObject = ((JSONObject)innerObject).get(route);
    }
    return (T) innerObject;
  }

}
