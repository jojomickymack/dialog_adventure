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

        // this RoundingMode.DOWN thing fixes an issue where little lines are flickering between some tiles because of weird calculations caused by the camera
        with(AppObj) {
            cam.position.x = (physics.pos.x * unitScale + physics.w * unitScale / 2).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
            cam2.position.x = (physics.pos.x * unitScale * 0.35f).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()

            cam.position.y = (physics.pos.y * unitScale + physics.h * unitScale / 2).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
            cam2.position.y = (physics.pos.y * unitScale * 0.35f).toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
            cam.update()
            cam2.update()
        }
    }
}