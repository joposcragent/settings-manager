package ru.sadovskie.leo.app.joposcragent.settingsmanager.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("settings.sentence-transformer")
data class SentenceTransformerProperties(
	val baseUrl: String,
)
