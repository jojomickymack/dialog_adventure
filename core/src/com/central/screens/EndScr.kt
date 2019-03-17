package com.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.central.AppObj
import ktx.actors.plusAssign
import ktx.app.KtxScreen

class EndScr : KtxScreen {
    private val endImage = Image(AppObj.endBgTex)

    override fun show() {
        Gdx.input.inputProcessor = AppObj.hudStg
        AppObj.ic.aPressed = false
        AppObj.dialogMode = true

        endImage.setSize(AppObj.cam.viewportWidth, AppObj.cam.viewportHeight)

        endImage.color.a = 0f
        endImage += sequence(
                fadeIn(2f),
                delay(3f),
                fadeOut(2f),
                Actions.run {
                    AppObj.dialogMode = false
                    AppObj.app.setScreen<PreTitleScr>()
                }
        )
        AppObj.stg += endImage
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
        endImage.clearActions()
        AppObj.stg.clear()
    }

    fun checkInput() {
        if (AppObj.ic.aPressed) {
            AppObj.ic.aPressed = false
            AppObj.app.setScreen<PreTitleScr>()
        }
    }
}