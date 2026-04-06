package ru.sadovskie.leo.app.joposcragent.settingsmanager.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import ru.sadovskie.leo.app.joposcragent.settingsmanager.config.SentenceTransformerProperties
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration

@Component
class SentenceTransformerClient(
	private val properties: SentenceTransformerProperties,
	private val objectMapper: ObjectMapper,
) {
	private val httpClient: HttpClient =
		HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(10))
			.build()

	fun vectorize(text: String): FloatArray {
		val base = properties.baseUrl.trimEnd('/')
		val uri = URI.create("$base/").resolve("text/vectorize")
		val payload = objectMapper.writeValueAsString(TextCorpus(text))

		val request = HttpRequest.newBuilder(uri)
			.timeout(Duration.ofSeconds(120))
			.header("Content-Type", "application/json; charset=utf-8")
			.POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
			.build()

		val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
		if (response.statusCode() != 200) {
			throw IllegalStateException("${response.statusCode()} Unprocessable Content: ${response.body()}")
		}

		val boxed: Array<Float> = objectMapper.readValue(response.body(), object : TypeReference<Array<Float>>() {})
		return FloatArray(boxed.size) { i -> boxed[i] }
	}

	private data class TextCorpus(val text: String)
}
