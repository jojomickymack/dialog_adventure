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

class TitleScr : KtxScreen {
    private val bgImage = Image(Texture(Gdx.files.internal("titlebg.png")))
    private val title = Image(Texture(Gdx.files.internal("title01.png")))
    private val guy = Image(Texture(Gdx.files.internal("guy.png")))

    override fun show() {
        Gdx.input.inputProcessor = AppObj.hudStg
        AppObj.ic.aPressed = false
        AppObj.dialogMode = true

        bgImage.setSize(AppObj.cam.viewportWidth, AppObj.cam.viewportHeight)
        bgImage.color.a = 0f
        bgImage.addAction(sequence(
                Actions.fadeIn(2f),
                delay(3f)
        ))
        title.setSize(AppObj.cam.viewportWidth / 1.2f, AppObj.cam.viewportHeight / 1.2f)
        title.setPosition(AppObj.cam.viewportWidth / 2 - title.width / 2, AppObj.cam.viewportHeight)
        title.addAction(sequence(
                Actions.moveTo(AppObj.cam.viewportWidth / 2 - title.width / 2, AppObj.cam.viewportHeight / 2 - title.height / 2, 3f),
                delay(2f)
        ))
        guy.setSize(8f, 20f)
        guy.setPosition(AppObj.cam.viewportWidth / 8, 0f - guy.height)
        guy.addAction(sequence(
                delay(3f),
                Actions.moveTo(AppObj.cam.viewportWidth / 8, 0f - 5f, 3f)
        ))
        AppObj.stg.addActor(bgImage)
        AppObj.stg.addActor(title)
        AppObj.stg.addActor(guy)
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