package com.vinovim.app.features.player

// libass uses com.nuvio.tv.libass package (JNI binding - cannot rename .so symbols)
// These aliases keep the rest of the codebase free of nuvio references.
import com.nuvio.tv.libass.media.AssHandler as _AssHandler
import com.nuvio.tv.libass.media.extractor.AssMatroskaExtractor as _AssMatroskaExtractor
import com.nuvio.tv.libass.media.parser.AssSubtitleParserFactory as _AssSubtitleParserFactory
import com.nuvio.tv.libass.media.render.AssRenderersFactory as _AssRenderersFactory
import com.nuvio.tv.libass.media.widget.AssSubtitleSurfaceView as _AssSubtitleSurfaceView
import com.nuvio.tv.libass.media.parser.AssFullSubtitleParser as _AssFullSubtitleParser

internal typealias AssHandler = _AssHandler
internal typealias AssMatroskaExtractor = _AssMatroskaExtractor
internal typealias AssSubtitleParserFactory = _AssSubtitleParserFactory
internal typealias AssRenderersFactory = _AssRenderersFactory
internal typealias AssSubtitleSurfaceView = _AssSubtitleSurfaceView
internal typealias AssFullSubtitleParser = _AssFullSubtitleParser
