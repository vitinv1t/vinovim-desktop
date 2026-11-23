package com.vinovim.app.features.p2p

// Aliases so internal code never references the original engine package directly.
import com.vinovim.engine.VinovimEngine as _VinovimEngine
import com.vinovim.engine.VinovimEngineConfig as _VinovimEngineConfig
import com.vinovim.engine.VinovimEventType as _VinovimEventType
import com.vinovim.engine.VinovimStream as _VinovimStream
import com.vinovim.engine.VinovimTorrentProfile as _VinovimTorrentProfile
import com.vinovim.engine.VinovimUploadMode as _VinovimUploadMode

internal typealias StreamingEngine = _VinovimEngine
internal typealias StreamingEngineConfig = _VinovimEngineConfig
internal typealias StreamingEventType = _VinovimEventType
internal typealias StreamingStream = _VinovimStream
internal typealias StreamingTorrentProfile = _VinovimTorrentProfile
internal typealias StreamingUploadMode = _VinovimUploadMode
