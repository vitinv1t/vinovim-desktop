package com.vinovim.app.features.plugins

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.plugins_manifest_name_missing
import com.vinovim.app.resources.plugins_manifest_no_providers
import com.vinovim.app.resources.plugins_manifest_version_missing
import org.jetbrains.compose.resources.getString

internal object PluginManifestParser {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun parse(payload: String): PluginManifest {
        val manifest = json.decodeFromString<PluginManifest>(payload)
        require(manifest.name.isNotBlank()) {
            runBlocking { getString(Res.string.plugins_manifest_name_missing) }
        }
        require(manifest.version.isNotBlank()) {
            runBlocking { getString(Res.string.plugins_manifest_version_missing) }
        }
        require(manifest.scrapers.isNotEmpty()) {
            runBlocking { getString(Res.string.plugins_manifest_no_providers) }
        }
        return manifest
    }
}
