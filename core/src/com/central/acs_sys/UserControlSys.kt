package com.central.acs_sys

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.central.AppObj
import com.central.acs_comp.PhysicsComp
import com.central.acs_comp.UserControlComp
import com.central.dialog.DialogBox
import com.central.dialog.TypewriterAct

class UserControlSys : IteratingSystem(Family.all(PhysicsComp::class.java, UserControlComp::class.java).get()) {

    private val pm = ComponentMapper.getFor(PhysicsComp::class.java)

    init {

    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        with(physics) {
            if (AppObj.ic.lPressed) {
                goLeft = true
                goRight = false
            }
            if (AppObj.ic.rPressed) {
                goLeft = false
                goRight = true
            }
            if (!AppObj.ic.lPressed and !AppObj.ic.rPressed) {
                goLeft = false
                goRight = false
            }
        }

        if (!AppObj.dialogMode) physics.jumping = AppObj.ic.aPressed
        else {
            if (AppObj.ic.aPressed) {
                AppObj.ic.aPressed = false
                AppObj.dialogStg.actors.forEach {
                    if (it is DialogBox) {
                        if (it.dialogLabel.actions.size > 0) {
                            val seqAction = it.dialogLabel.actions.first() as SequenceAction
                            val actions = seqAction.actions
                            for (action in actions) {
                                if (action is TypewriterAct) {
                                    if (action.completed) seqAction.actions.removeValue(action, true)
                                    else action.completed = true
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }

        if (physics.goLeft) {
            if (physics.grounded) physics.state = PhysicsComp.PhysicalStates.WALKING
            physics.facesRight = false
            physics.vel.x = -physics.topSpeed
        }
        else if (physics.goRight) {
            if (physics.grounded) physics.state = PhysicsComp.PhysicalStates.WALKING
            physics.facesRight = true
            physics.vel.x = physics.topSpeed
        }
        if (physics.jumping && physics.grounded) {
            AppObj.jumpSnd.play()
            physics.state = PhysicsComp.PhysicalStates.JUMPING
            physics.jumping = false
            physics.grounded = false
            physics.jumpTimer = physics.jumpTimerMax
            physics.vel.y = physics.initialJumpVelocity
        }
        if (physics.vel.y > 0 && AppObj.ic.aPressed && physics.state == PhysicsComp.PhysicalStates.JUMPING && physics.jumpTimer > 0) {
            physics.vel.y += physics.jumpVelocity
            physics.jumpTimer--
        }
    }
}