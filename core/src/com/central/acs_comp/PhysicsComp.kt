package com.central.acs_comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class PhysicsComp : Component {

    enum class PhysicalStates {
        STANDING,
        WALKING,
        JUMPING
    }

    val vel = Vector2()
    val pos = Vector2()
    val scl = Vector2(1.0f, 1.0f)
    var w = 50f
    var h = 50f
    var rot = 0.0f
    var rect = Rectangle()
    var direction = 1
    var topSpeed = 0f

    var goLeft = false
    var goRight = true
    var grounded = false
    var jumping = false
    var facesRight = true

    var jumpTimer = 0
    val jumpTimerMax = 10
    val initialJumpVelocity = 75f
    val jumpVelocity = 15f

    var state = PhysicalStates.WALKING
    var stateTime = 0f
}