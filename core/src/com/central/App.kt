package com.central

import com.badlogic.gdx.Screen
import com.central.screens.*
import ktx.app.KtxGame

class App : KtxGame<Screen>() {

    override fun create() {

        AppObj.app = this

        addScreen(PreTitleScr())
        addScreen(TitleScr())
        addScreen(GameScr())
        addScreen(EndScr())

        setScreen<PreTitleScr>()

        AppObj.music.isLooping = true
        // AppObj.music.play()
    }

    override fun dispose() {
        AppObj.dispose()
        println("all disposable memory freed")
        super.dispose()
    }
}
