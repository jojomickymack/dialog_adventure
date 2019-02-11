package com.central.acs_sys

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.central.App
import com.central.AppObj
import com.central.acs_comp.PhysicsComp
import com.central.screens.EndScr
import java.math.RoundingMode

class PhysicsSys : IteratingSystem(Family.all(PhysicsComp::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComp::class.java)
    private val dampening = 0.9f

    public override fun processEntity(entity: Entity, deltaTime: Float) {

        if (!AppObj.paused) {
            val physics = pm.get(entity)

            with(physics) {
                // progresses animation for getKeyFrame calls
                stateTime += deltaTime

                vel.y -= AppObj.grav

                vel.x *= dampening
                if (grounded) state = PhysicsComp.PhysicalStates.WALKING

                if (Math.abs(vel.x) < 10) {
                    vel.x = 0f
                    if (grounded) state = PhysicsComp.PhysicalStates.STANDING
                }

                pos.x += vel.x * 0.04f
                pos.y += vel.y * 0.04f

                // since the camera is dependent on the position of the player I'm using this
                // fix to avoid this weird problem with transparent lines flickering between
                // tiles - it's caused by some weird calculations that occur with tilemaps
                // and the camera
                pos.x = pos.x.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
                pos.y = pos.y.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()

                rect.set(pos.x, pos.y, w, h)

                // go to the game end screen if you fall off the screen

                if (pos.y < -100) AppObj.app.setScreen<EndScr>()
            }
        }
    }
}