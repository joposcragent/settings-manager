package ru.sadovskie.leo.app.joposcragent.settingsmanager.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.Tables.CSS_QUERY_SELECTORS
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.enums.CssQuerySelectorType
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.CssQuerySelectorsRecord

@Repository
class CssQuerySelectorRepository(
	private val dsl: DSLContext,
) {

	fun findAll(): List<CssQuerySelectorsRecord> =
		dsl.selectFrom(CSS_QUERY_SELECTORS).orderBy(CSS_QUERY_SELECTORS.TYPE).fetch()

	fun findByType(type: CssQuerySelectorType): CssQuerySelectorsRecord? =
		dsl.selectFrom(CSS_QUERY_SELECTORS).where(CSS_QUERY_SELECTORS.TYPE.eq(type)).fetchOne()

	fun updateSelector(type: CssQuerySelectorType, selector: String) {
		dsl.update(CSS_QUERY_SELECTORS)
			.set(CSS_QUERY_SELECTORS.SELECTOR, selector)
			.set(CSS_QUERY_SELECTORS.UPDATED_AT, DSL.currentOffsetDateTime())
			.where(CSS_QUERY_SELECTORS.TYPE.eq(type))
			.execute()
	}

	fun delete(type: CssQuerySelectorType) {
		dsl.deleteFrom(CSS_QUERY_SELECTORS).where(CSS_QUERY_SELECTORS.TYPE.eq(type)).execute()
	}
}
