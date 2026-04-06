package ru.sadovskie.leo.app.joposcragent.settingsmanager.web

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.RelevanceThresholdItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.RelevanceThresholdSetRequest
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.RelevanceThresholdsListDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.enums.ThresholdType
import ru.sadovskie.leo.app.joposcragent.settingsmanager.service.RelevanceThresholdService

@RestController
class RelevanceThresholdController(
	private val relevanceThresholdService: RelevanceThresholdService,
) {

	@GetMapping("/relevance-thresholds/list", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun list(): RelevanceThresholdsListDto = relevanceThresholdService.list()

	@GetMapping("/relevance-thresholds/{type}", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun get(@PathVariable type: String): RelevanceThresholdItemDto =
		relevanceThresholdService.get(parseType(type))

	@PostMapping("/relevance-thresholds/{type}", consumes = [MediaType.APPLICATION_JSON_VALUE])
	fun post(@PathVariable type: String, @RequestBody body: RelevanceThresholdSetRequest) {
		relevanceThresholdService.setThreshold(parseType(type), body.value)
	}

	private fun parseType(raw: String): ThresholdType =
		try {
			ThresholdType.valueOf(raw)
		} catch (_: IllegalArgumentException) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type: $raw")
		}
}
