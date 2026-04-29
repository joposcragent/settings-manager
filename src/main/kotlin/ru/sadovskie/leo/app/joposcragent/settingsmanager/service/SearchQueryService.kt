package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryCreateRequest
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.SearchQueryPatchRequest
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
				name = row.name,
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
			name = row.name,
			query = row.query,
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)
	}

	@Transactional
	fun create(uuid: UUID, body: SearchQueryCreateRequest) {
		if (searchQueryRepository.findByUuid(uuid) != null) {
			throw ResponseStatusException(HttpStatus.CONFLICT)
		}
		val name = body.name.trim()
		val query = body.query.trim()
		searchQueryRepository.insert(uuid, name, query)
	}

	@Transactional
	fun update(uuid: UUID, body: SearchQueryPatchRequest) {
		if (searchQueryRepository.findByUuid(uuid) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		val nameIn = body.name
		val queryIn = body.query
		if (nameIn == null && queryIn == null) {
			throw ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Укажите хотя бы одно из полей: name или query",
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
		searchQueryRepository.update(
			uuid,
			name = name,
			query = query,
		)
	}

	@Transactional
	fun delete(uuid: UUID) {
		if (searchQueryRepository.findByUuid(uuid) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		searchQueryRepository.delete(uuid)
	}
}
