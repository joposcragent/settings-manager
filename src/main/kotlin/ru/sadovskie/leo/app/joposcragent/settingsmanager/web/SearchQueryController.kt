package ru.sadovskie.leo.app.joposcragent.settingsmanager.web

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryCreateRequest
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryPatchRequest
import ru.sadovskie.leo.app.joposcragent.settingsmanager.service.SearchQueryService
import java.util.UUID

@RestController
class SearchQueryController(
	private val searchQueryService: SearchQueryService,
) {

	@GetMapping("/search-query/list", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun list(): List<SearchQueryItemDto> = searchQueryService.listAll()

	@GetMapping("/search-query/{entityUuid}", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun get(@PathVariable entityUuid: UUID): SearchQueryItemDto =
		searchQueryService.get(entityUuid)

	@PostMapping(
		"/search-query/{entityUuid}",
		consumes = [MediaType.APPLICATION_JSON_VALUE],
	)
	fun post(
		@PathVariable entityUuid: UUID,
		@RequestBody @Valid body: SearchQueryCreateRequest,
	) {
		searchQueryService.create(entityUuid, body)
	}

	@PatchMapping(
		"/search-query/{entityUuid}",
		consumes = [MediaType.APPLICATION_JSON_VALUE],
	)
	fun patch(
		@PathVariable entityUuid: UUID,
		@RequestBody body: SearchQueryPatchRequest,
	) {
		searchQueryService.update(entityUuid, body)
	}

	@DeleteMapping("/search-query/{entityUuid}")
	fun delete(@PathVariable entityUuid: UUID) {
		searchQueryService.delete(entityUuid)
	}
}
