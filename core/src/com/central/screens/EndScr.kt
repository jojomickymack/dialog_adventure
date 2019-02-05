package com.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.central.App
import com.central.AppObj
import ktx.app.KtxScreen

class EndScr : KtxScreen {
    private val endImage = Image(Texture(Gdx.files.internal("end_bg.png")))

    override fun show() {
        Gdx.input.inputProcessor = AppObj.hudStg
        AppObj.ic.aPressed = false
        AppObj.dialogMode = true

        endImage.setSize(AppObj.cam.viewportWidth, AppObj.cam.viewportHeight)

        endImage.color.a = 0f
        endImage.addAction(sequence(
                Actions.fadeIn(2f),
                delay(3f),
                Actions.fadeOut(2f),
                Actions.run {
                    AppObj.dialogMode = false
                    AppObj.app.setScreen<PreTitleScr>()
                }
        ))
        AppObj.stg.addActor(endImage)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        AppObj.stg.act(delta)
        AppObj.stg.draw()

        AppObj.hudStg.act(delta)
        AppObj.hudStg.draw()

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