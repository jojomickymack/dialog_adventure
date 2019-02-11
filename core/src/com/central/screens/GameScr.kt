package com.central.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.central.AppObj
import com.central.acs_comp.*
import com.central.acs_sys.*
import com.central.dialog.DialogBox
import ktx.actors.plusAssign


fun spawnEnemies(engine: Engine) {

    var dialogs = arrayListOf(DialogComp(arrayListOf("Hi, I'm the skeleton dude. I am a student at Aureus Ludus Academy.",
            "I just rose from the grave.",
            "Now I must rome around the world of the living until the end of time.")),
            DialogComp(arrayListOf("My bones sure are heavy.",
            "I just had a big meal.",
            "Bone meal.")),
            DialogComp(arrayListOf("I'm on my way to the bone shop.",
            "I need a tune up.",
            "When your bones start creakin, you need to take 'em to the shop.")))


    val aiControlled = AiControlComp()

    val map = engine.getSystem(MapSys::class.java)
    val enemies = map.enemyLayer.objects

    for (i in 0 until(enemies.count)) {
        if (enemies[i] is TextureMapObject && enemies[i].name == "zombie") {
            var enemy = Entity()
            val enemyTex = TextureComp()
            val enemyPhys = PhysicsComp()

            with(enemyTex) {
                loadSheet(Texture(Gdx.files.internal("zombie.png")), 5)
                stand = Animation(0f, regions[0])
                walk = Animation(0.15f, regions[0], regions[1], regions[2], regions[3], regions[4])
                jump = Animation(0f, regions[0])
            }

            with(enemyPhys) {
                w = 10f
                h = 20f
                topSpeed = 13f
                pos.set(enemies[i].properties["x"].toString().toFloat(), enemies[i].properties["y"].toString().toFloat())
            }

            with(enemy) {
                add(enemyTex)
                add(enemyPhys)
                add(aiControlled)
                add(dialogs[i])
            }

            engine.addEntity(enemy)
        }
    }
}

class GameScr : KtxScreen {

    val bkground = Texture(Gdx.files.internal("clouds.png"))
    var bkgroundOffSet = 0

    var ashleyEngine = Engine()

    var alex = Entity()

    val alexTex = TextureComp()
    val alexPhys = PhysicsComp()
    val alexDialogListener = DialogListenerComp()

    val userControlled = UserControlComp()
    val cameraFollow = CameraFollowComp()

    var dialogBox = DialogBox()

    override fun show() {
        Gdx.input.inputProcessor = AppObj.hudStg
        AppObj.dialogMode = false

        bkground.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge)

        with(ashleyEngine) {
            addSystem(PhysicsSys())
            addSystem(RenderSys())
            addSystem(UserControlSys())
            addSystem(AiControlSys())
            addSystem(MapSys())
            addSystem(CameraFollowSys())
            addSystem(DialogListenerSys())
        }

        with(alexTex) {
            loadSheet(Texture(Gdx.files.internal("adventurer_sheet.png")), 4)
            stand = Animation(0f, alexTex.regions[0])
            walk = Animation(0.15f, alexTex.regions[1], alexTex.regions[2])
            jump = Animation(0f, alexTex.regions[3])
        }

        with(alexPhys) {
            w = 10f
            h = 25f
            pos.set(450f, 200f)
            topSpeed = 32f
        }

        with(alex) {
            add(alexTex)
            add(alexPhys)
            add(userControlled)
            add(cameraFollow)
            add(alexDialogListener)
        }

        ashleyEngine.addEntity(alex)

        spawnEnemies(ashleyEngine)

        dialogBox.setDialogSize(AppObj.dialogCam.viewportWidth, AppObj.dialogCam.viewportHeight / 2, 0f, AppObj.dialogCam.viewportHeight - AppObj.dialogCam.viewportHeight / 2)
        dialogBox.isVisible = false

        AppObj.dialogStg += dialogBox
        AppObj.hudStg += AppObj.osc
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        bkgroundOffSet += 1

        with (AppObj.hudStg.batch) {
            begin()
            draw(bkground, 0f, 0f, bkgroundOffSet / 4, 0, AppObj.hudStg.width.toInt(), AppObj.hudStg.height.toInt())
            end()
        }

        ashleyEngine.update(delta)

        with(AppObj) {
            dialogStg.act(delta)
            dialogStg.draw()

            hudStg.act(delta)
            hudStg.draw()
        }
    }

    override fun resize(width: Int, height: Int) {
        with(AppObj) {
            hudView.update(width, height)
            dialogView.update(width, height)
        }
    }

    override fun hide() {
        ashleyEngine.removeAllEntities()
        with(AppObj) {
            stg.clear()
            hudStg.actors.forEach { it.remove() }
            dialogStg.clear()
            cam.position.x = cam.viewportWidth / 2
            cam.position.y = cam.viewportHeight / 2
        }
    }
}
