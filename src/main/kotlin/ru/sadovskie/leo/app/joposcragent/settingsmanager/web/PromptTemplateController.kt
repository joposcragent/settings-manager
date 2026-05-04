package ru.sadovskie.leo.app.joposcragent.settingsmanager.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.PromptTemplateDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.service.PromptTemplateService

@RestController
class PromptTemplateController(
	private val promptTemplateService: PromptTemplateService,
) {

	@GetMapping("/prompt-template", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun get(): PromptTemplateDto = promptTemplateService.get()

	@PostMapping(
		"/prompt-template",
		consumes = [MediaType.TEXT_PLAIN_VALUE],
		produces = [MediaType.APPLICATION_JSON_VALUE],
	)
	fun post(@RequestBody body: String): PromptTemplateDto =
		promptTemplateService.setTemplate(body)
}
