package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.RelevanceThresholdItemDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.dto.RelevanceThresholdsListDto
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.enums.ThresholdType
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.RelevanceThresholdRepository

@Service
class RelevanceThresholdService(
	private val relevanceThresholdRepository: RelevanceThresholdRepository,
) {

	fun list(): RelevanceThresholdsListDto {
		val rows = relevanceThresholdRepository.findAll()
		if (rows.isEmpty()) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND)
		}
		return RelevanceThresholdsListDto(
			list = rows.map { row ->
				RelevanceThresholdItemDto(
					value = row.value.toDouble(),
					type = row.type.name,
					createdAt = row.createdAt,
					updatedAt = row.updatedAt,
				)
			},
		)
	}

	fun get(type: ThresholdType): RelevanceThresholdItemDto {
		val row = relevanceThresholdRepository.findByType(type)
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		return RelevanceThresholdItemDto(
			value = row.value.toDouble(),
			type = row.type.name,
			createdAt = row.createdAt,
			updatedAt = row.updatedAt,
		)
	}

	@Transactional
	fun setThreshold(type: ThresholdType, value: Double) {
		val v = value.toFloat()
		val existing = relevanceThresholdRepository.findByType(type)
		if (existing == null) {
			relevanceThresholdRepository.insert(type, v)
		} else {
			relevanceThresholdRepository.update(type, v)
		}
	}
}
