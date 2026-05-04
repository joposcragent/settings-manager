package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.PromptTemplateDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.PromptTemplateRepository

@Service
class PromptTemplateService(
	private val promptTemplateRepository: PromptTemplateRepository,
) {

	fun get(): PromptTemplateDto {
		val row = promptTemplateRepository.fetchOne()
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		return PromptTemplateDto(
			template = row.template,
			createdAt = row.createdAt!!,
			updatedAt = row.updatedAt,
		)
	}

	@Transactional
	fun setTemplate(template: String): PromptTemplateDto {
		val existing = promptTemplateRepository.fetchOne()
		if (existing == null) {
			promptTemplateRepository.insert(template)
		} else {
			promptTemplateRepository.update(existing.uuid!!, template)
		}
		val row = promptTemplateRepository.fetchOne()
			?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
		return PromptTemplateDto(
			template = row.template,
			createdAt = row.createdAt!!,
			updatedAt = row.updatedAt,
		)
	}
}
