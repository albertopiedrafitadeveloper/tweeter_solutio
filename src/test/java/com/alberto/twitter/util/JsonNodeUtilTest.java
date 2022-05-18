package com.alberto.twitter.util;

import java.util.stream.Stream;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class JsonNodeUtilTest {

  private static final String STRING_VALUE_JSON = "{\"key\":\"value\"}";
  private static final String INT_VALUE_JSON = "{\"key\":1}";
  private static final String BOOL_VALUE_JSON = "{\"key\":true}";

  @ParameterizedTest(
      name = "{index} = expectedInstance={0},expectedStringvalue{1},inputString={2},path={3}")
  @MethodSource("providerData")
  void validateCardsMaxLimit(Class<?> expectedInstance, String expectedStringvalue,
      String inputString,
      String path) throws JSONException {
    JSONObject jsonObject = new JSONObject(inputString);
    String stringResult = Strings.EMPTY;
    if (expectedInstance.equals(String.class)) {
      String result = JsonNodeUtil.getJsonObject(jsonObject, path);
      stringResult = result;
    } else if (expectedInstance.equals(Integer.class)) {
      Integer result = JsonNodeUtil.getJsonObject(jsonObject, path);
      stringResult = result.toString();
    } else if (expectedInstance.equals(Boolean.class)) {
      Boolean result = JsonNodeUtil.getJsonObject(jsonObject, path);
      stringResult = result.toString();
    }
    Assertions.assertEquals(expectedStringvalue, stringResult);
  }

  private static Stream<Arguments> providerData() {
    return Stream.of(
        Arguments.of(String.class, "value", STRING_VALUE_JSON, "key"),
        Arguments.of(Integer.class, "1", INT_VALUE_JSON, "key"),
        Arguments.of(Boolean.class, "true", BOOL_VALUE_JSON, "key")
    );
  }

}
