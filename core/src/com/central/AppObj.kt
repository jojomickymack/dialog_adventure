package com.central

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.central.input.GamepadCtl
import com.central.input.InputCtl
import com.central.input.OnScreenGamepad

object AppObj {
    var width = Gdx.graphics.height.toFloat()
    var height = Gdx.graphics.width.toFloat()

    var paused = false
    var dialogMode = false

    val skin = Skin(Gdx.files.internal("skin/skin/skinui.json"))

    val desiredWidth = 480f
    val desiredHeight = 480f

    val sb = SpriteBatch()
    val cam = OrthographicCamera(width, height)
    val cam2 = OrthographicCamera(width, height)
    val view = StretchViewport(desiredWidth, desiredHeight, cam)
    val stg = Stage(view, sb)

    val hudSb = SpriteBatch()
    val hudCam = OrthographicCamera(width, height)
    val hudView = StretchViewport(desiredWidth, desiredHeight, hudCam)
    val hudStg = Stage(hudView , hudSb)

    val dialogSb = SpriteBatch()
    val dialogCam = OrthographicCamera(width, height)
    val dialogView = StretchViewport(desiredWidth, desiredHeight, dialogCam)
    val dialogStg = Stage(dialogView , dialogSb)

    val ic = InputCtl()
    val gpc = GamepadCtl()
    val osc = OnScreenGamepad()

    // the width of my tiles in the tilemap - makes things sane so you can use tile coordinates
    val unitScale = 1 / 16f
    val grav = 10f

    val labelStyle = LabelStyle()
    val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"))
    val fontParameters = FreeTypeFontGenerator.FreeTypeFontParameter()
    val customFont = fontGenerator.generateFont(fontParameters)

    val dialogSnd = Gdx.audio.newSound(Gdx.files.internal("dialog.ogg"))
    val jumpSnd = Gdx.audio.newSound(Gdx.files.internal("jump.ogg"))
    val music = Gdx.audio.newMusic(Gdx.files.internal("background_song.ogg"))

    lateinit var app: App

    init {
        val tilesWide = 12f
        val tilesHigh = 12f
        cam.setToOrtho(false, tilesWide, tilesHigh)
        cam2.setToOrtho(false, tilesWide, tilesHigh)

        with(fontParameters) {
            size = 24
            color = WHITE
            borderWidth = 2f
            borderColor = BLACK
            borderStraight = true
            minFilter = TextureFilter.Linear
            magFilter = TextureFilter.Linear
        }

        labelStyle.font = customFont
    }
}