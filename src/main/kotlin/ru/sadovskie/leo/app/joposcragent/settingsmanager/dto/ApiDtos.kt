package ru.sadovskie.leo.app.joposcragent.settingsmanager.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.OffsetDateTime
import java.util.UUID

data class ReferenceContextDto(
	val context: String,
	val vector: List<Float>,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

/** Тело ответа POST /reference-context (без текста контекста). */
data class ReferenceContextPersistedDto(
	val vector: List<Float>,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

data class PromptTemplateDto(
	val template: String?,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

data class SearchQueryItemDto(
	val uuid: UUID,
	val name: String,
	val query: String,
	val contentRelevance: Double,
	val notificationRelevance: Double,
	@get:JsonProperty("isActive")
	val isActive: Boolean,
	@get:JsonProperty("isLazyScraping")
	val isLazyScraping: Boolean,
	val createdAt: OffsetDateTime,
	@JsonProperty("updatedAt")
	val updatedAt: OffsetDateTime?,
)

data class SearchQueryCreateRequest(
	@field:NotBlank
	@field:Size(max = 512)
	val name: String,
	@field:NotBlank
	val query: String,
	@field:NotNull
	@field:DecimalMin("0")
	@field:DecimalMax("1")
	val contentRelevance: Double,
	@field:NotNull
	@field:DecimalMin("0")
	@field:DecimalMax("1")
	val notificationRelevance: Double,
	@field:JsonProperty("isActive")
	val isActive: Boolean? = null,
	@field:JsonProperty("isLazyScraping")
	val isLazyScraping: Boolean? = null,
)

data class SearchQueryPatchRequest(
	@field:Size(max = 512)
	val name: String? = null,
	val query: String? = null,
	@field:DecimalMin("0")
	@field:DecimalMax("1")
	val contentRelevance: Double? = null,
	@field:DecimalMin("0")
	@field:DecimalMax("1")
	val notificationRelevance: Double? = null,
	@field:JsonProperty("isActive")
	val isActive: Boolean? = null,
	@field:JsonProperty("isLazyScraping")
	val isLazyScraping: Boolean? = null,
)
