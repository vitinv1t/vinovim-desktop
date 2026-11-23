package com.vinovim.app.features.servers

import com.vinovim.app.resources.Res
import com.vinovim.app.resources.servers_error_failed
import com.vinovim.app.resources.servers_error_forbidden
import com.vinovim.app.resources.servers_failure_auth
import com.vinovim.app.resources.servers_failure_incomplete
import com.vinovim.app.resources.servers_failure_not_found
import com.vinovim.app.resources.servers_failure_remote_playback
import com.vinovim.app.resources.servers_failure_unreachable
import com.vinovim.app.resources.servers_failure_unsupported
import com.vinovim.app.resources.servers_play_method_direct_play
import com.vinovim.app.resources.servers_play_method_direct_stream
import com.vinovim.app.resources.servers_play_method_transcode
import com.vinovim.app.resources.servers_playback_failed
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

internal fun Throwable.serverFailure(): ServerFailure = (this as? ServerException)?.failure ?: ServerFailure.FAILED

internal suspend fun Throwable.serverMessage(): String? = (this as? ServerException)?.let { getString(it.failure.message()) }

internal suspend fun Throwable.serverPlaybackMessage(): String = getString(
    serverFailure().takeUnless { it == ServerFailure.FAILED }?.message() ?: Res.string.servers_playback_failed,
)

internal fun ServerFailure.message(): StringResource = when (this) {
    ServerFailure.AUTH_REQUIRED -> Res.string.servers_failure_auth
    ServerFailure.UNREACHABLE -> Res.string.servers_failure_unreachable
    ServerFailure.NOT_FOUND -> Res.string.servers_failure_not_found
    ServerFailure.INCOMPLETE -> Res.string.servers_failure_incomplete
    ServerFailure.FORBIDDEN -> Res.string.servers_error_forbidden
    ServerFailure.UNSUPPORTED -> Res.string.servers_failure_unsupported
    ServerFailure.REMOTE_PLAYBACK_BLOCKED -> Res.string.servers_failure_remote_playback
    ServerFailure.FAILED -> Res.string.servers_error_failed
}

internal fun ServerPlayMethod.label(): StringResource = when (this) {
    ServerPlayMethod.DIRECT_PLAY -> Res.string.servers_play_method_direct_play
    ServerPlayMethod.DIRECT_STREAM -> Res.string.servers_play_method_direct_stream
    ServerPlayMethod.TRANSCODE -> Res.string.servers_play_method_transcode
}

internal fun readableTranscodeReason(reason: String): String =
    reason.replace(WORD_BOUNDARY, " ").lowercase().replaceFirstChar(Char::uppercase)

private val WORD_BOUNDARY = Regex("(?<=[a-z])(?=[A-Z])")
