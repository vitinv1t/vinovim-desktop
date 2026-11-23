package com.vinovim.app.features.settings

import com.vinovim.app.nativebridge.VinovimIsCurrentAlternateAppIcon
import com.vinovim.app.nativebridge.VinovimSetAlternateAppIconName
import com.vinovim.app.nativebridge.VinovimSupportsAlternateAppIcons
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSUserDefaults
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
internal actual object AppIconPlatform {
    actual val isSupported: Boolean = true
    actual val requiresCloseConfirmation: Boolean = false

    private const val selectedIconKey = "selected_app_icon"
    private var pendingChange: CancellableContinuation<Boolean>? = null

    actual fun loadSelectedIconKey(): String? =
        NSUserDefaults.standardUserDefaults.stringForKey(selectedIconKey)

    actual fun saveSelectedIconKey(key: String) {
        NSUserDefaults.standardUserDefaults.setObject(key, forKey = selectedIconKey)
    }

    actual fun currentIconName(): String? = AppIconOption.entries
        .mapNotNull(AppIconOption::platformName)
        .firstOrNull { name -> VinovimIsCurrentAlternateAppIcon(name) }

    actual suspend fun activateIcon(name: String?): Boolean {
        if (!VinovimSupportsAlternateAppIcons()) return false

        return suspendCancellableCoroutine { continuation ->
            if (pendingChange != null) {
                continuation.resume(false)
                return@suspendCancellableCoroutine
            }

            pendingChange = continuation
            continuation.invokeOnCancellation {
                if (pendingChange === continuation) {
                    pendingChange = null
                }
            }

            VinovimSetAlternateAppIconName(name, appIconCompletion)
        }
    }

    fun completeChange(success: Boolean) {
        val continuation = pendingChange ?: return
        pendingChange = null
        if (continuation.isActive) {
            continuation.resume(success)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private val appIconCompletion: CPointer<CFunction<(Boolean) -> Unit>> =
    staticCFunction { success -> AppIconPlatform.completeChange(success) }
