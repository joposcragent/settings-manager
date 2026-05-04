package ru.sadovskie.leo.app.joposcragent.settingsmanager.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.Tables.PROMPT_TEMPLATE
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.PromptTemplateRecord
import java.util.UUID

@Repository
class PromptTemplateRepository(
	private val dsl: DSLContext,
) {

	fun fetchOne(): PromptTemplateRecord? =
		dsl.selectFrom(PROMPT_TEMPLATE).limit(1).fetchOne()

	fun insert(template: String) {
		dsl.insertInto(PROMPT_TEMPLATE)
			.columns(
				PROMPT_TEMPLATE.TEMPLATE,
				PROMPT_TEMPLATE.CREATED_AT,
				PROMPT_TEMPLATE.UPDATED_AT,
			)
			.values(
				DSL.`val`(template, PROMPT_TEMPLATE.TEMPLATE),
				DSL.currentOffsetDateTime(),
				DSL.`val`(null, PROMPT_TEMPLATE.UPDATED_AT),
			)
			.execute()
	}

	fun update(uuid: UUID, template: String) {
		dsl.update(PROMPT_TEMPLATE)
			.set(PROMPT_TEMPLATE.TEMPLATE, template)
			.set(PROMPT_TEMPLATE.UPDATED_AT, DSL.currentOffsetDateTime())
			.where(PROMPT_TEMPLATE.UUID.eq(uuid))
			.execute()
	}
}
