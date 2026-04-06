package ru.sadovskie.leo.app.joposcragent.settingsmanager.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.Tables.REFERENCE_CONTEXT
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.ReferenceContextRecord
import java.util.UUID

@Repository
class ReferenceContextRepository(
	private val dsl: DSLContext,
) {

	fun fetchOne(): ReferenceContextRecord? =
		dsl.selectFrom(REFERENCE_CONTEXT).limit(1).fetchOne()

	fun insert(text: String, vector: FloatArray) {
		val boxed: Array<Float> = Array(vector.size) { vector[it] }
		dsl.insertInto(REFERENCE_CONTEXT)
			.columns(
				REFERENCE_CONTEXT.TEXT,
				REFERENCE_CONTEXT.VECTOR,
				REFERENCE_CONTEXT.CREATED_AT,
				REFERENCE_CONTEXT.UPDATED_AT,
			)
			.values(
				DSL.`val`(text, REFERENCE_CONTEXT.TEXT),
				DSL.`val`(boxed, REFERENCE_CONTEXT.VECTOR),
				DSL.currentOffsetDateTime(),
				DSL.`val`(null, REFERENCE_CONTEXT.UPDATED_AT),
			)
			.execute()
	}

	fun update(uuid: UUID, text: String, vector: FloatArray) {
		val boxed: Array<Float> = Array(vector.size) { vector[it] }
		dsl.update(REFERENCE_CONTEXT)
			.set(REFERENCE_CONTEXT.TEXT, text)
			.set(REFERENCE_CONTEXT.VECTOR, boxed)
			.set(REFERENCE_CONTEXT.UPDATED_AT, DSL.currentOffsetDateTime())
			.where(REFERENCE_CONTEXT.UUID.eq(uuid))
			.execute()
	}
}
