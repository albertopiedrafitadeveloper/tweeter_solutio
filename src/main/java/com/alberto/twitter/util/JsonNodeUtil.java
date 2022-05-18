package com.alberto.twitter.util;

import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.camel.util.json.JsonObject;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;

@UtilityClass
public class JsonNodeUtil {

  public static <T> T getJsonObject(JSONObject jsonObject, String... path) {
    Object innerObject = jsonObject;
    for(String route : path){
      Object o = ((JSONObject)innerObject).get(route);
      if(Objects.isNull(o)){
        innerObject = Strings.EMPTY;
        break;
      }
      else{
        innerObject = o;
      }
    }
    return (T) innerObject;
  }

}
