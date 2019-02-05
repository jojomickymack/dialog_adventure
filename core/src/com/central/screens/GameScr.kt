package com.central.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.central.App
import com.central.AppObj
import com.central.acs_comp.*
import com.central.acs_sys.*
import com.central.dialog.DialogBox

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

            enemyTex.loadSheet(Texture(Gdx.files.internal("zombie.png")), 5)
            enemyTex.stand = Animation(0f, enemyTex.regions[0])
            enemyTex.walk = Animation(0.15f, enemyTex.regions[0], enemyTex.regions[1], enemyTex.regions[2], enemyTex.regions[3], enemyTex.regions[4])
            enemyTex.jump = Animation(0f, enemyTex.regions[0])

            enemyPhys.w = 10f
            enemyPhys.h = 20f
            enemyPhys.topSpeed = 13f
            enemyPhys.pos.set(enemies[i].properties["x"].toString().toFloat(), enemies[i].properties["y"].toString().toFloat())

            enemy.add(enemyTex)
            enemy.add(enemyPhys)
            enemy.add(aiControlled)
            enemy.add(dialogs[i])

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

        ashleyEngine.addSystem(PhysicsSys())
        ashleyEngine.addSystem(RenderSys())
        ashleyEngine.addSystem(UserControlSys())
        ashleyEngine.addSystem(AiControlSys())
        ashleyEngine.addSystem(MapSys())
        ashleyEngine.addSystem(CameraFollowSys())
        ashleyEngine.addSystem(DialogListenerSys())

        alexTex.loadSheet(Texture(Gdx.files.internal("adventurer_sheet.png")), 4)
        alexTex.stand = Animation(0f, alexTex.regions[0])
        alexTex.walk = Animation(0.15f, alexTex.regions[1], alexTex.regions[2])
        alexTex.jump = Animation(0f, alexTex.regions[3])

        alexPhys.w = 10f
        alexPhys.h = 25f
        alexPhys.pos.set(450f, 200f)
        alexPhys.topSpeed = 32f

        alex.add(alexTex)
        alex.add(alexPhys)
        alex.add(userControlled)
        alex.add(cameraFollow)
        alex.add(alexDialogListener)

        ashleyEngine.addEntity(alex)

        spawnEnemies(ashleyEngine)

        dialogBox.setDialogSize(AppObj.dialogCam.viewportWidth, AppObj.dialogCam.viewportHeight / 2, 0f, AppObj.dialogCam.viewportHeight - AppObj.dialogCam.viewportHeight / 2)
        dialogBox.isVisible = false

        AppObj.dialogStg.addActor(dialogBox)
        AppObj.hudStg.addActor(AppObj.osc)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        bkgroundOffSet += 1
        AppObj.hudStg.batch.begin()
        AppObj.hudStg.batch.draw(bkground, 0f, 0f, bkgroundOffSet / 4, 0, AppObj.hudStg.width.toInt(), AppObj.hudStg.height.toInt())
        AppObj.hudStg.batch.end()

        ashleyEngine.update(delta)

        AppObj.dialogStg.act(delta)
        AppObj.dialogStg.draw()

        AppObj.hudStg.act(delta)
        AppObj.hudStg.draw()
    }

    override fun resize(width: Int, height: Int) {
        AppObj.hudView.update(width, height)
        AppObj.dialogView.update(width, height)
    }

    override fun hide() {
        ashleyEngine.removeAllEntities()
        AppObj.stg.clear()
        AppObj.hudStg.actors.forEach {
            it.remove()
        }
        AppObj.dialogStg.clear()
        AppObj.cam.position.x = AppObj.cam.viewportWidth / 2
        AppObj.cam.position.y = AppObj.cam.viewportHeight / 2
    }
}
