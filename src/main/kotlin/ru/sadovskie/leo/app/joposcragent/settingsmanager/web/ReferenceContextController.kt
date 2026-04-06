package ru.sadovskie.leo.app.joposcragent.settingsmanager.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.ReferenceContextDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.service.ReferenceContextService

@RestController
class ReferenceContextController(
	private val referenceContextService: ReferenceContextService,
) {

	@GetMapping("/reference-context", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun get(): ReferenceContextDto = referenceContextService.get()

	@PostMapping("/reference-context", consumes = [MediaType.TEXT_PLAIN_VALUE])
	fun post(@RequestBody body: String) {
		referenceContextService.setContext(body)
	}
}
