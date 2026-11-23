package com.vinovim.app.features.library

internal actual object LibraryClock {
    actual fun nowEpochMs(): Long = System.currentTimeMillis()
}
