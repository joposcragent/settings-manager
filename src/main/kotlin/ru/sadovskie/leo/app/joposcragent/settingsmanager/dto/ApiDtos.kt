package ru.sadovskie.leo.app.joposcragent.settingsmanager.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime
import java.util.UUID

data class ReferenceContextDto(
	val context: String,
	val vector: List<Float>,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

data class SearchQueryItemDto(
	val uuid: UUID,
	val query: String,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

data class RelevanceThresholdsListDto(
	val list: List<RelevanceThresholdItemDto>,
)

data class RelevanceThresholdItemDto(
	val value: Double,
	val type: String,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

data class RelevanceThresholdSetRequest(
	val value: Double,
)
