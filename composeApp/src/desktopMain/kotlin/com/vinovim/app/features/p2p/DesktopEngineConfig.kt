package com.vinovim.app.features.p2p

// Engine types are aliased in EngineAliases.kt to avoid direct references to the engine package
import java.io.File

internal fun buildStreamingEngineConfig(
    stateDirectory: File,
    cacheDirectory: File,
    uploadEnabled: Boolean,
    torrentProfile: P2pTorrentProfile,
    diskCacheCapacityBytes: Long,
): StreamingEngineConfig = StreamingEngineConfig(
    dataDirectory = stateDirectory,
    cacheDirectory = cacheDirectory,
    diskCacheCapacityBytes = diskCacheCapacityBytes,
    torrentProfile = when (torrentProfile) {
        P2pTorrentProfile.SOFT -> StreamingTorrentProfile.Soft
        P2pTorrentProfile.BALANCED -> StreamingTorrentProfile.Balanced
        P2pTorrentProfile.FAST -> StreamingTorrentProfile.Fast
    },
    uploadMode = if (uploadEnabled) {
        StreamingUploadMode.Unlimited
    } else {
        StreamingUploadMode.Disabled
    },
    streamInactivityTimeoutMilliseconds = 0,
)

internal fun migrateNestedPayloadDirectory(cacheDirectory: File) {
    val payloadDirectory = File(cacheDirectory, "payload")
    val nestedDirectory = File(payloadDirectory, "payload")
    val entries = nestedDirectory.listFiles() ?: return
    entries.forEach { entry ->
        val target = File(payloadDirectory, entry.name)
        if (target.exists() || !entry.renameTo(target)) {
            entry.deleteRecursively()
        }
    }
    nestedDirectory.deleteRecursively()
}

internal fun unexpectedStreamStopError(
    requestId: Long,
    eventStreamId: String?,
    currentStreamId: String?,
    message: String?,
    fallbackMessage: String,
): P2pStreamingState.Error? {
    if (requestId != 0L || currentStreamId == null || eventStreamId != currentStreamId) {
        return null
    }
    return P2pStreamingState.Error(message?.trim()?.takeIf(String::isNotEmpty) ?: fallbackMessage)
}

internal fun unexpectedTorrentError(
    requestId: Long,
    eventTorrentId: String?,
    currentTorrentId: String?,
    message: String?,
    fallbackMessage: String,
): P2pStreamingState.Error? {
    if (requestId != 0L ||
        eventTorrentId == null ||
        currentTorrentId == null ||
        eventTorrentId != currentTorrentId
    ) {
        return null
    }
    return P2pStreamingState.Error(message?.trim()?.takeIf(String::isNotEmpty) ?: fallbackMessage)
}
