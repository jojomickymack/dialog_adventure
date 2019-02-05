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

class PreTitleScr : KtxScreen {
    private val bgImage = Image(Texture(Gdx.files.internal("sky.png")))
    private val logo = Image(Texture(Gdx.files.internal("logo.png")))


    override fun show() {
        Gdx.input.inputProcessor = AppObj.hudStg
        AppObj.ic.aPressed = false
        AppObj.dialogMode = true

        bgImage.setSize(AppObj.cam.viewportWidth, AppObj.cam.viewportHeight)
        bgImage.color.a = 0f
        bgImage.addAction(sequence(
                Actions.fadeIn(2f),
                delay(3f),
                Actions.fadeOut(2f)
        ))
        logo.setSize(AppObj.cam.viewportWidth / 2, AppObj.cam.viewportHeight / 2)
        logo.setPosition(AppObj.cam.viewportWidth / 2 - logo.width / 2, AppObj.cam.viewportHeight)
        logo.addAction(sequence(
                Actions.moveTo(AppObj.cam.viewportWidth / 2 - logo.width / 2, AppObj.cam.viewportHeight / 2 - logo.height / 2, 3f),
                delay(2f),
                Actions.fadeOut(2f),
                Actions.run {
                    AppObj.app.setScreen<TitleScr>()
                }
        ))
        AppObj.stg.addActor(bgImage)
        AppObj.stg.addActor(logo)
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
        bgImage.clearActions()
        logo.clearActions()
        AppObj.stg.clear()
    }

    fun checkInput() {
        if (AppObj.ic.aPressed) {
            AppObj.ic.aPressed = false
            AppObj.app.setScreen<TitleScr>()
        }
    }
}