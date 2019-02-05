package com.central.acs_sys

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.central.acs_comp.PhysicsComp
import com.central.acs_comp.AiControlComp

class AiControlSys : IteratingSystem(Family.all(PhysicsComp::class.java, AiControlComp::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComp::class.java)

    init {

    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        physics.vel.x = physics.topSpeed * physics.direction
    }
}