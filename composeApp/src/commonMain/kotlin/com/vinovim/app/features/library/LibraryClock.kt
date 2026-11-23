package com.vinovim.app.features.library

internal expect object LibraryClock {
    fun nowEpochMs(): Long
}
