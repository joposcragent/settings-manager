package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryCreateRequest
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryPatchRequest
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.SearchQueriesRecord
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.SearchQueryRepository
import java.util.UUID

@Service
class SearchQueryService(
	private val searchQueryRepository: SearchQueryRepository,
) {

	fun list(activeOnly: Boolean = false): List<SearchQueryItemDto> =
		searchQueryRepository.findAll(activeOnly).map { row -> toDto(row) }

	fun get(uuid: UUID): SearchQueryItemDto {
		val row = searchQueryRepository.findByUuid(uuid)
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		return toDto(row)
	}

	private fun toDto(row: SearchQueriesRecord) =
		SearchQueryItemDto(
			uuid = row.uuid,
			name = row.name,
			query = row.query,
			contentRelevance = row.contentRelevance,
			notificationRelevance = row.notificationRelevance,
			isActive = row.isActive,
			isLazyScraping = row.isLazyScraping,
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)

	@Transactional
	fun create(uuid: UUID, body: SearchQueryCreateRequest) {
		if (searchQueryRepository.findByUuid(uuid) != null) {
			throw ResponseStatusException(HttpStatus.CONFLICT)
		}
		val name = body.name.trim()
		val query = body.query.trim()
		val isActive = body.isActive ?: true
		val isLazyScraping = body.isLazyScraping ?: false
		searchQueryRepository.insert(
			uuid = uuid,
			name = name,
			query = query,
			contentRelevance = body.contentRelevance,
			notificationRelevance = body.notificationRelevance,
			isActive = isActive,
			isLazyScraping = isLazyScraping,
		)
	}

	@Transactional
	fun update(uuid: UUID, body: SearchQueryPatchRequest) {
		if (searchQueryRepository.findByUuid(uuid) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		val nameIn = body.name
		val queryIn = body.query
		val hasAny =
			nameIn != null || queryIn != null ||
				body.contentRelevance != null || body.notificationRelevance != null ||
				body.isActive != null || body.isLazyScraping != null
		if (!hasAny) {
			throw ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Укажите хотя бы одно поле для обновления",
			)
		}
		val name = nameIn?.trim()
		val query = queryIn?.trim()
		if (nameIn != null && name.isNullOrEmpty()) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле name не может быть пустым")
		}
		if (queryIn != null && query.isNullOrEmpty()) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле query не может быть пустым")
		}
		body.contentRelevance?.let { requireRelevanceInRange(it, "contentRelevance") }
		body.notificationRelevance?.let { requireRelevanceInRange(it, "notificationRelevance") }
		searchQueryRepository.update(
			uuid,
			name = name,
			query = query,
			contentRelevance = body.contentRelevance,
			notificationRelevance = body.notificationRelevance,
			isActive = body.isActive,
			isLazyScraping = body.isLazyScraping,
		)
	}

	private fun requireRelevanceInRange(value: Double, field: String) {
		if (!value.isFinite() || value < 0.0 || value > 1.0) {
			throw ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Поле $field должно быть числом в диапазоне [0, 1]",
			)
		}
	}

	@Transactional
	fun delete(uuid: UUID) {
		if (searchQueryRepository.findByUuid(uuid) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		searchQueryRepository.delete(uuid)
	}
}
