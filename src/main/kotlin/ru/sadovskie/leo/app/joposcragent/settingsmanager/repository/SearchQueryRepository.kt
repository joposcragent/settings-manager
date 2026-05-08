package ru.sadovskie.leo.app.joposcragent.settingsmanager.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.Tables.SEARCH_QUERIES
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.SearchQueriesRecord
import java.util.UUID

@Repository
class SearchQueryRepository(
	private val dsl: DSLContext,
) {

	fun findAll(): List<SearchQueriesRecord> =
		dsl.selectFrom(SEARCH_QUERIES).orderBy(SEARCH_QUERIES.CREATED_AT).fetch()

	fun findByUuid(uuid: UUID): SearchQueriesRecord? =
		dsl.selectFrom(SEARCH_QUERIES).where(SEARCH_QUERIES.UUID.eq(uuid)).fetchOne()

	fun insert(
		uuid: UUID,
		name: String,
		query: String,
		contentRelevance: Double,
		notificationRelevance: Double,
		isActive: Boolean,
		isLazyScraping: Boolean,
	) {
		dsl.insertInto(SEARCH_QUERIES)
			.columns(
				SEARCH_QUERIES.UUID,
				SEARCH_QUERIES.NAME,
				SEARCH_QUERIES.QUERY,
				SEARCH_QUERIES.CONTENT_RELEVANCE,
				SEARCH_QUERIES.NOTIFICATION_RELEVANCE,
				SEARCH_QUERIES.IS_ACTIVE,
				SEARCH_QUERIES.IS_LAZY_SCRAPING,
			)
			.values(
				uuid,
				name,
				query,
				contentRelevance,
				notificationRelevance,
				isActive,
				isLazyScraping,
			)
			.execute()
	}

	fun update(
		uuid: UUID,
		name: String?,
		query: String?,
		contentRelevance: Double?,
		notificationRelevance: Double?,
		isActive: Boolean?,
		isLazyScraping: Boolean?,
	) {
		var step = dsl.update(SEARCH_QUERIES)
			.set(SEARCH_QUERIES.UPDATED_AT, DSL.currentOffsetDateTime())
		if (query != null) {
			step = step.set(SEARCH_QUERIES.QUERY, query)
		}
		if (name != null) {
			step = step.set(SEARCH_QUERIES.NAME, name)
		}
		if (contentRelevance != null) {
			step = step.set(SEARCH_QUERIES.CONTENT_RELEVANCE, contentRelevance)
		}
		if (notificationRelevance != null) {
			step = step.set(SEARCH_QUERIES.NOTIFICATION_RELEVANCE, notificationRelevance)
		}
		if (isActive != null) {
			step = step.set(SEARCH_QUERIES.IS_ACTIVE, isActive)
		}
		if (isLazyScraping != null) {
			step = step.set(SEARCH_QUERIES.IS_LAZY_SCRAPING, isLazyScraping)
		}
		step.where(SEARCH_QUERIES.UUID.eq(uuid)).execute()
	}

	fun delete(uuid: UUID) {
		dsl.deleteFrom(SEARCH_QUERIES).where(SEARCH_QUERIES.UUID.eq(uuid)).execute()
	}
}
