package ru.sadovskie.leo.app.joposcragent.settingsmanager.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
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
		objectMapper: ObjectMapper,
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
				converters.add(MappingJackson2HttpMessageConverter(objectMapper))
			}
			.build()
	}
}
