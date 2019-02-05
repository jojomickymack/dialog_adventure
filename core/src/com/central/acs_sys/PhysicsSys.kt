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

            // progresses animation for getKeyFrame calls
            physics.stateTime += deltaTime

            physics.vel.y -= AppObj.grav

            physics.vel.x *= dampening
            if (physics.grounded) physics.state = PhysicsComp.PhysicalStates.WALKING

            if (Math.abs(physics.vel.x) < 10) {
                physics.vel.x = 0f
                if (physics.grounded) physics.state = PhysicsComp.PhysicalStates.STANDING
            }

            physics.pos.x += physics.vel.x * 0.04f
            physics.pos.y += physics.vel.y * 0.04f

            physics.pos.x = physics.pos.x.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
            physics.pos.y = physics.pos.y.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()

            physics.rect.set(physics.pos.x, physics.pos.y, physics.w, physics.h)

            // go to the game end screen if you fall off the screen

            if (physics.pos.y < -100) AppObj.app.setScreen<EndScr>()
        }
    }
}