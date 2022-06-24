package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.xss.cleaner.JsoupXssCleaner;
import com.hccake.ballcat.common.xss.core.XssStringJsonDeserializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author hccake
 */
@Slf4j
public class Test {

	@Data
	static class DemoBean {

		private Integer pageNum;

		private Integer pageSize;

		private String createDate;

		private Integer isRead;

	}

	public static void main(String[] args) throws JsonProcessingException {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.deserializerByType(String.class, new XssStringJsonDeserializer(new JsoupXssCleaner()));
		ObjectMapper objectMapper = builder.build();

		DemoBean demoBean = objectMapper.readValue("{\n" + "    \"pageNum\": 1,\n" + "    \"pageSize\": 15,\n" + " \n"
				+ "    \"createDate\":[\"12\"],\n" + "       \"system\": \"1\",\n" + "       \"isRead\": 1,\n"
				+ "    \"issue\": \"qweqweq\"\n" + "}", DemoBean.class);
		log.info("demoBean:{}", demoBean);
	}

}
