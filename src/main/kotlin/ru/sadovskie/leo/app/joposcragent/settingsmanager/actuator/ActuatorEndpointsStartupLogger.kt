package ru.sadovskie.leo.app.joposcragent.settingsmanager.actuator

import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoints
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ActuatorEndpointsStartupLogger(
	private val pathMappedEndpoints: PathMappedEndpoints,
) {
	private val log = LoggerFactory.getLogger(javaClass)

	@EventListener(ApplicationReadyEvent::class)
	fun logEndpoints() {
		val base = pathMappedEndpoints.basePath.trim().trimStart('/')
		log.info("Actuator web base path: /{}", base)
		pathMappedEndpoints.allPaths.sorted().forEach { path ->
			log.info("Actuator endpoint: {}", path)
		}
	}
}
