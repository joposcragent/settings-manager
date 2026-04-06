package ru.sadovskie.leo.app.joposcragent.settingsmanager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import tools.jackson.databind.json.JsonMapper
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.nio.charset.StandardCharsets
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class HttpClientConfig {

	@Bean
	fun sentenceTransformerRestClient(
		properties: SentenceTransformerProperties,
		jsonMapper: JsonMapper,
	): RestClient {
		val httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.build()
		val factory = JdkClientHttpRequestFactory(httpClient)
		factory.setReadTimeout(Duration.ofSeconds(120))
		return RestClient.builder()
			.baseUrl(properties.baseUrl.trimEnd('/'))
			.requestFactory(factory)
			.messageConverters { converters ->
				converters.add(StringHttpMessageConverter(StandardCharsets.UTF_8))
				converters.add(JacksonJsonHttpMessageConverter(jsonMapper))
			}
			.build()
	}
}
