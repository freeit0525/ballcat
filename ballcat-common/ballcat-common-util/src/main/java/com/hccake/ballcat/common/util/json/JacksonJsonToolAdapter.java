package com.hccake.ballcat.common.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * @author lingting 2021/2/25 21:04
 */
public class JacksonJsonToolAdapter implements JsonTool {

	@Getter
	@Setter
	static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public void config(Consumer<ObjectMapper> consumer) {
		consumer.accept(mapper);
	}

	@SneakyThrows(JsonProcessingException.class)
	@Override
	public String toJson(Object obj) {
		return mapper.writeValueAsString(obj);
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, Class<T> r) {
		return mapper.readValue(json, r);
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, Type t) {
		return mapper.readValue(json, mapper.constructType(t));
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, TypeReference<T> t) {
		return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<T>() {
			@Override
			public Type getType() {
				return t.getType();
			}
		});
	}

}
