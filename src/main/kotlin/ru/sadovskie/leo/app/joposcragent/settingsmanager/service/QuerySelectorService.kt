package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.CssQuerySelectorItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.CssQuerySelectorsListDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.enums.CssQuerySelectorType
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.CssQuerySelectorRepository

@Service
class QuerySelectorService(
	private val cssQuerySelectorRepository: CssQuerySelectorRepository,
) {

	fun list(): CssQuerySelectorsListDto {
		val rows = cssQuerySelectorRepository.findAll()
		if (rows.isEmpty()) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		return CssQuerySelectorsListDto(
			list = rows.map { row ->
				CssQuerySelectorItemDto(
					selector = row.selector,
					type = row.type.name,
					createdAt = row.createdAt,
					updatedAt = row.updatedAt,
				)
			},
		)
	}

	fun get(type: CssQuerySelectorType): CssQuerySelectorItemDto {
		val row = cssQuerySelectorRepository.findByType(type)
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		return CssQuerySelectorItemDto(
			selector = row.selector,
			type = row.type.name,
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)
	}

	@Transactional
	fun setSelector(type: CssQuerySelectorType, selector: String) {
		if (cssQuerySelectorRepository.findByType(type) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		cssQuerySelectorRepository.updateSelector(type, selector)
	}

	@Transactional
	fun delete(type: CssQuerySelectorType) {
		if (cssQuerySelectorRepository.findByType(type) == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		cssQuerySelectorRepository.delete(type)
	}
}
