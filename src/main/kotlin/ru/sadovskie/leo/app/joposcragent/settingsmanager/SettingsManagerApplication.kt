package ru.sadovskie.leo.app.joposcragent.settingsmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SettingsManagerApplication

fun main(args: Array<String>) {
	runApplication<SettingsManagerApplication>(*args)
}
