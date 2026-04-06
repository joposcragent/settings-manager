package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.SearchQueryRepository
import java.util.UUID

@Service
class SearchQueryService(
	private val searchQueryRepository: SearchQueryRepository,
) {

	fun listAll(): List<SearchQueryItemDto> =
		searchQueryRepository.findAll().map { row ->
			SearchQueryItemDto(
				uuid = row.uuid,
				query = row.query,
				createdAt = row.createdAt,
				updatedAt = row.updatedAt,
			)
		}

	fun get(uuid: UUID): SearchQueryItemDto {
		val row = searchQueryRepository.findByUuid(uuid)
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		return SearchQueryItemDto(
			uuid = row.uuid,
			query = row.query,
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)
	}

	@Transactional
	fun create(uuid: UUID, query: String) {
		if (searchQueryRepository.findByUuid(uuid) != null) {
			throw ResponseStatusException(HttpStatus.CONFLICT)
		}
		searchQueryRepository.insert(uuid, query)
	}

	@Transactional
	fun update(uuid: UUID, query: String) {
		if (searchQueryRepository.findByUuid(uuid) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		searchQueryRepository.updateQuery(uuid, query)
	}

	@Transactional
	fun delete(uuid: UUID) {
		if (searchQueryRepository.findByUuid(uuid) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		searchQueryRepository.delete(uuid)
	}
}
