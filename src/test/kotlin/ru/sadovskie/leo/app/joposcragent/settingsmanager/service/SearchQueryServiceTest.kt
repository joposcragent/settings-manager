package ru.sadovskie.leo.app.joposcragent.settingsmanager.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import ru.sadovskie.leo.app.joposcragent.settingsmanager.jooq.tables.records.SearchQueriesRecord
import ru.sadovskie.leo.app.joposcragent.settingsmanager.repository.SearchQueryRepository
import java.time.OffsetDateTime
import java.util.UUID

class SearchQueryServiceTest {

	private val uuid: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

	@Test
	fun `create throws 409 when uuid already exists`() {
		val repo = mockk<SearchQueryRepository>()
		val existing = mockk<SearchQueriesRecord>()
		every { repo.findByUuid(uuid) } returns existing
		val service = SearchQueryService(repo)
		val ex = assertThrows(ResponseStatusException::class.java) {
			service.create(uuid, "q")
		}
		assertEquals(HttpStatus.CONFLICT, ex.statusCode)
		verify(exactly = 0) { repo.insert(any(), any()) }
	}

	@Test
	fun `get returns dto when row exists`() {
		val repo = mockk<SearchQueryRepository>()
		val row = mockk<SearchQueriesRecord>()
		val created = OffsetDateTime.parse("2026-01-01T12:00:00Z")
		every { row.uuid } returns uuid
		every { row.query } returns "https://example.com"
		every { row.createdAt } returns created
		every { row.updatedAt } returns null
		every { repo.findByUuid(uuid) } returns row
		val service = SearchQueryService(repo)
		val dto = service.get(uuid)
		assertEquals(uuid, dto.uuid)
		assertEquals("https://example.com", dto.query)
		assertEquals(created, dto.createdAt)
	}
}
