package com.zxh.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by renren on 17/7/25.
 */
public class JacksonUtils {

  public static final Log LOG = LogFactory.getLog(JacksonUtils.class);
  //定义json对象
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * 将对象转换为json字符串
   */
  public static String toJson(Object data) {
    if (data == null) {
      LOG.error("JsonUtils.objectToJson.data is null");
      return null;
    }
    try {
      String string = MAPPER.writeValueAsString(data);
      return string;
    } catch (JsonProcessingException e) {
      LOG.error("JsonUtils.objectToJson.ERROR", e);
    }
    return null;
  }

  /**
   * 对obj对象进行序列话，序列化是依据jsonViewClazz的配置
   */
  public static <T> String toJson(Object obj, Class<T> jsonViewClazz) {
    try {
      return MAPPER.writerWithView(jsonViewClazz).writeValueAsString(
        obj);
    } catch (Exception e) {
      //throw Throwables.propagate(e);
      return "";
    }
  }

  /**
   * 利用Jackson序列化时，指定各种类型及其对应的过滤条件<br>
   * <br>
   * include exclude可以其中之一为空或者同时为空<br>
   * include为空只过滤exclude<br>
   * exclude为空，只根据include的配置输出字段<br>
   * 同时为空时不进行过滤
   *
   * @param obj     需要序列化的对象
   * @param include 指定class序列化时需要包含的属性
   * @param exclude 指定class序列化时需要排除的属性
   * @return 根据include exclude进行属性的过滤后的对象生成的json 串
   */
  @SuppressWarnings({"serial", "rawtypes"})
  public static String toJson(Object obj, Map<Class, Set<String>> include,
                              Map<Class, Set<String>> exclude) {

    if ((null == include || include.isEmpty())
      && (null == exclude || exclude.isEmpty())) {
      toJson(obj);
    }

    ObjectMapper mapper = new ObjectMapper();

    // 设置包含过滤器
    FilterProvider filters = new SimpleFilterProvider();
    if (null != include && !include.isEmpty()) {
      for (Map.Entry<Class, Set<String>> entry : include.entrySet()) {
        Class clazz = entry.getKey();
        Set<String> includeFileds = entry.getValue();
        ((SimpleFilterProvider) filters).addFilter(clazz.getName(),
          SimpleBeanPropertyFilter.filterOutAllExcept(includeFileds));
      }
    }

    // 设置排除过滤器
    if (null != exclude && !exclude.isEmpty()) {
      for (Map.Entry<Class, Set<String>> entry : exclude.entrySet()) {
        Class clazz = entry.getKey();
        Set<String> excludeFileds = entry.getValue();
        ((SimpleFilterProvider) filters).addFilter(clazz.getName(),
          SimpleBeanPropertyFilter.serializeAllExcept(excludeFileds));
      }
    }
    mapper.setFilterProvider(filters);

    // 都是有哪些过滤器名
    final Set<String> filterNames = new HashSet<String>();
    if (null != include && !include.isEmpty()) {
      for (Class clazz : include.keySet()) {
        filterNames.add(clazz.getName());
      }
    }
    if (null != exclude && !exclude.isEmpty()) {
      for (Class clazz : exclude.keySet()) {
        filterNames.add(clazz.getName());
      }
    }

    mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

      public Object findFilterId(Annotated ac) {
        String name = ac.getName();
        if (filterNames.contains(name)) {
          return name;
        } else {
          return null;
        }
      }
    });

    try {
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      //throw Throwables.propagate(e);
      return "ERROR";
    }
  }


  /**
   * 将json数据转换为pojo对象
   */
  public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
    if (jsonData == null || "".equals(jsonData) || beanType == null) {
      LOG.error("JsonUtils.jsonToPojo.jsonDate or beanType is null");
      return null;
    }
    try {
      T t = MAPPER.readValue(jsonData, beanType);
      return t;
    } catch (Exception e) {
      LOG.error("JsonUtils.jsonToPojo.ERROR", e);
    }
    return null;
  }

  /**
   * 将json数据转换为pojo对象list
   */
  public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
    if (jsonData == null || "".equals(jsonData) || beanType == null) {
      LOG.error("JsonUtils.jsonToList.jsonDate or beanType is null");
      return null;
    }
    JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    try {
      List<T> list = MAPPER.readValue(jsonData, javaType);
      return list;
    } catch (Exception e) {
      LOG.error("JsonUtils.jsonToList.ERROR", e);
    }
    return null;
  }

  public static JsonNode toJsonNode(String jsonText) {
    JsonNode jsonNode = null;
    try {
      jsonNode = MAPPER.readTree(jsonText);
    } catch (Exception e) {
      //throw Throwables.propagate(e);
    }
    return jsonNode;
  }
}
