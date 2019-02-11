package com.central.acs_sys

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import com.central.AppObj
import com.central.acs_comp.TextureComp
import com.central.acs_comp.PhysicsComp

class RenderSys : EntitySystem() {
    private var textures: ImmutableArray<Entity> = ImmutableArray(Array<Entity>())

    private val mm = ComponentMapper.getFor(PhysicsComp::class.java)
    private val tm = ComponentMapper.getFor(TextureComp::class.java)

    init {

    }

    override fun addedToEngine(engine: Engine) {
        textures = engine.getEntitiesFor(Family.all(TextureComp::class.java).get())
    }

    override fun update(deltaTime: Float) {
        val map = engine.getSystem(MapSys::class.java)

        with(map) {
            mr.setView(AppObj.cam)
            mr2.setView(AppObj.cam2)
            //map.mr.render()

            mr2.batch.begin()
            mr2.renderTileLayer(background)
            mr2.batch.end()

            mr.batch.begin()
            mr.renderTileLayer(behind)
            mr.renderTileLayer(solid)
            mr.batch.end()
        }

        AppObj.stg.batch.projectionMatrix = AppObj.cam.combined

        AppObj.stg.batch.begin()

        textures.forEach {
            val physics = mm.get(it)
            val texture = tm.get(it)
            val flip = if (physics.facesRight) 1 else -1
            val shift = if (physics.facesRight) 0f else -physics.w

            val tex = when (physics.state) {
                PhysicsComp.PhysicalStates.STANDING -> texture.stand.getKeyFrame(physics.stateTime, true)
                PhysicsComp.PhysicalStates.WALKING -> texture.walk.getKeyFrame(physics.stateTime, true)
                PhysicsComp.PhysicalStates.JUMPING -> texture.jump.getKeyFrame(physics.stateTime, true)
            }

            with(physics) {
                AppObj.stg.batch.draw(tex,
                        pos.x * AppObj.unitScale + shift * AppObj.unitScale, pos.y * AppObj.unitScale,
                        w * AppObj.unitScale, h * AppObj.unitScale,
                        w * AppObj.unitScale, h * AppObj.unitScale,
                        scl.x * flip, scl.y,
                        rot)
            }
        }

        AppObj.stg.batch.end()

        with(map) {
            mr.batch.begin()
            mr.renderTileLayer(inFront)
            mr.batch.end()
        }
    }
}