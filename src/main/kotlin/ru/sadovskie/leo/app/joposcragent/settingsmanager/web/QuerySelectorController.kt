package ru.sadovskie.leo.app.joposcragent.settingsmanager.web

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.CssQuerySelectorItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.CssQuerySelectorsListDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.enums.CssQuerySelectorType
import ru.sadovskie.leo.app.joposcragent.settingsmanager.service.QuerySelectorService

@RestController
class QuerySelectorController(
	private val querySelectorService: QuerySelectorService,
) {

	@GetMapping("/query-selector/list", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun list(): CssQuerySelectorsListDto = querySelectorService.list()

	@GetMapping("/query-selector/{selectorType}", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun get(@PathVariable selectorType: String): CssQuerySelectorItemDto =
		querySelectorService.get(parseSelectorType(selectorType))

	@PostMapping("/query-selector/{selectorType}", consumes = [MediaType.TEXT_PLAIN_VALUE])
	fun post(@PathVariable selectorType: String, @RequestBody body: String) {
		querySelectorService.setSelector(parseSelectorType(selectorType), body)
	}

	@DeleteMapping("/query-selector/{selectorType}")
	fun delete(@PathVariable selectorType: String) {
		querySelectorService.delete(parseSelectorType(selectorType))
	}

	private fun parseSelectorType(raw: String): CssQuerySelectorType =
		try {
			CssQuerySelectorType.valueOf(raw)
		} catch (_: IllegalArgumentException) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid selectorType: $raw")
		}
}
