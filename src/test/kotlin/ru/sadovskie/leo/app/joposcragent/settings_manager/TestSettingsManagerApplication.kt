package ru.sadovskie.leo.app.joposcragent.settings_manager

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<SettingsManagerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
