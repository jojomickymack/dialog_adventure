package com.central.acs_sys

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.central.AppObj
import com.central.acs_comp.CameraFollowComp
import com.central.acs_comp.PhysicsComp
import java.math.RoundingMode

class CameraFollowSys : IteratingSystem(Family.all(PhysicsComp::class.java, CameraFollowComp::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComp::class.java)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        AppObj.cam.position.x = (physics.pos.x * AppObj.unitScale + physics.w * AppObj.unitScale / 2).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
        AppObj.cam2.position.x = (physics.pos.x * AppObj.unitScale * 0.35f).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()

        AppObj.cam.position.y = (physics.pos.y * AppObj.unitScale + physics.h * AppObj.unitScale / 2).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
        AppObj.cam2.position.y = (physics.pos.y * AppObj.unitScale * 0.35f).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
        AppObj.cam.update()
        AppObj.cam2.update()
    }
}