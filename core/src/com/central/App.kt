package com.central

import com.badlogic.gdx.Screen
import com.central.screens.*
import ktx.app.KtxGame
import ktx.async.enableKtxCoroutines

class App : KtxGame<Screen>() {

    override fun create() {
        enableKtxCoroutines(asynchronousExecutorConcurrencyLevel = 1)

        AppObj.app = this

        addScreen(PreTitleScr())
        addScreen(TitleScr())
        addScreen(GameScr())
        addScreen(EndScr())

        setScreen<PreTitleScr>()

        AppObj.music.isLooping = true
        // AppObj.music.play()
    }
}
