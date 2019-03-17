package com.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.central.AppObj
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.app.KtxScreen


class TitleScr : KtxScreen {
    private val bgImage = Image(AppObj.bgTex)
    private val title = Image(AppObj.titleTex)
    private val guy = Image(AppObj.guyTex)

    override fun show() {
        Gdx.input.inputProcessor = AppObj.hudStg
        AppObj.ic.aPressed = false
        AppObj.dialogMode = true

        with(bgImage) {
            setSize(AppObj.cam.viewportWidth, AppObj.cam.viewportHeight)
            alpha = 0f
            this += sequence(fadeIn(2f), delay(3f))
        }

        with(title) {
            setSize(AppObj.cam.viewportWidth / 1.2f, AppObj.cam.viewportHeight / 1.2f)
            setPosition(AppObj.cam.viewportWidth / 2 - title.width / 2, AppObj.cam.viewportHeight)
            this += sequence(
                    moveTo(AppObj.cam.viewportWidth / 2 - title.width / 2, AppObj.cam.viewportHeight / 2 - title.height / 2, 3f),
                    delay(2f)
            )
        }

        with(guy) {
            setSize(8f, 20f)
            setPosition(AppObj.cam.viewportWidth / 8, 0f - guy.height)
            this += sequence(
                    delay(3f),
                    moveTo(AppObj.cam.viewportWidth / 8, 0f - 5f, 3f)
            )
        }

        with(AppObj.stg) {
            this += bgImage
            this += title
            this += guy
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        with(AppObj) {
            stg.act(delta)
            stg.draw()

            hudStg.act(delta)
            hudStg.draw()
        }

        checkInput()
    }

    override fun hide() {
        bgImage.clearActions()
        title.clearActions()
        guy.clearActions()
        AppObj.stg.clear()
    }

    fun checkInput() {
        if (AppObj.ic.aPressed) {
            AppObj.ic.aPressed = false
            AppObj.app.setScreen<GameScr>()
        }
    }
}