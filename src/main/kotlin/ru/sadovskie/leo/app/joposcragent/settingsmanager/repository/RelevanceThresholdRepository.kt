package ru.sadovskie.leo.app.joposcragent.settingsmanager.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.Tables.RELEVANCE_THRESHOLDS
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.enums.ThresholdType
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.RelevanceThresholdsRecord

@Repository
class RelevanceThresholdRepository(
	private val dsl: DSLContext,
) {

	fun findAll(): List<RelevanceThresholdsRecord> =
		dsl.selectFrom(RELEVANCE_THRESHOLDS).orderBy(RELEVANCE_THRESHOLDS.TYPE).fetch()

	fun findByType(type: ThresholdType): RelevanceThresholdsRecord? =
		dsl.selectFrom(RELEVANCE_THRESHOLDS).where(RELEVANCE_THRESHOLDS.TYPE.eq(type)).fetchOne()

	fun insert(type: ThresholdType, value: Float) {
		dsl.insertInto(RELEVANCE_THRESHOLDS)
			.columns(
				RELEVANCE_THRESHOLDS.TYPE,
				RELEVANCE_THRESHOLDS.VALUE,
				RELEVANCE_THRESHOLDS.CREATED_AT,
				RELEVANCE_THRESHOLDS.UPDATED_AT,
			)
			.values(
				DSL.`val`(type, RELEVANCE_THRESHOLDS.TYPE),
				DSL.`val`(value, RELEVANCE_THRESHOLDS.VALUE),
				DSL.currentOffsetDateTime(),
				DSL.`val`(null, RELEVANCE_THRESHOLDS.UPDATED_AT),
			)
			.execute()
	}

	fun update(type: ThresholdType, value: Float) {
		dsl.update(RELEVANCE_THRESHOLDS)
			.set(RELEVANCE_THRESHOLDS.VALUE, value)
			.set(RELEVANCE_THRESHOLDS.UPDATED_AT, DSL.currentOffsetDateTime())
			.where(RELEVANCE_THRESHOLDS.TYPE.eq(type))
			.execute()
	}
}
