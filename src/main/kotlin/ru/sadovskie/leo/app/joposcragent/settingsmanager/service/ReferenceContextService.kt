package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.ReferenceContextDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.integration.SentenceTransformerClient
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.ReferenceContextRepository

@Service
class ReferenceContextService(
	private val referenceContextRepository: ReferenceContextRepository,
	private val sentenceTransformerClient: SentenceTransformerClient,
) {

	fun get(): ReferenceContextDto {
		val row = referenceContextRepository.fetchOne()
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		val vec = row.vector
		return ReferenceContextDto(
			context = row.text,
			vector = vec.map { it },
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)
	}

	@Transactional
	fun setContext(context: String): ReferenceContextDto {
		val vector = sentenceTransformerClient.vectorize(context)
		val existing = referenceContextRepository.fetchOne()
		if (existing == null) {
			referenceContextRepository.insert(context, vector)
		} else {
			referenceContextRepository.update(existing.uuid, context, vector)
		}
		val row = referenceContextRepository.fetchOne()
			?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
		val vec = row.vector
		return ReferenceContextDto(
			context = row.text,
			vector = vec.map { it },
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)
	}
}
