package com.central.acs_sys

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array
import com.central.AppObj
import com.central.acs_comp.DialogComp
import com.central.acs_comp.DialogListenerComp
import com.central.acs_comp.PhysicsComp
import com.central.dialog.DialogBox
import com.central.dialog.TypewriterAct

class DialogListenerSys : EntitySystem() {
    private var speakers: ImmutableArray<Entity> = ImmutableArray(Array<Entity>())
    private var listeners: ImmutableArray<Entity> = ImmutableArray(Array<Entity>())

    private val pm = ComponentMapper.getFor(PhysicsComp::class.java)
    private val dm = ComponentMapper.getFor(DialogComp::class.java)
    private val dlm = ComponentMapper.getFor(DialogListenerComp::class.java)

    init {

    }

    override fun addedToEngine(engine: Engine) {
        speakers = engine.getEntitiesFor(Family.all(DialogComp::class.java).get())
        listeners = engine.getEntitiesFor(Family.all(DialogListenerComp::class.java).get())
    }

    override fun update(deltaTime: Float) {

        listeners.forEach {
            val myListener = dlm.get(it)
            val myListenerPhys = pm.get(it)

            if (myListener.doneListening) {
                speakers.forEach {
                    val mySpeaker = dm.get(it)
                    val mySpeakerPhys = pm.get(it)

                    if (myListenerPhys.rect.overlaps(mySpeakerPhys.rect)) {
                        if (!mySpeaker.triggered) {
                            mySpeaker.triggered = true
                            AppObj.paused = true
                            AppObj.dialogMode = true
                            myListener.doneListening = false
                            mySpeaker.showDialog()
                        }
                    }
                }
            } else {
                var noOverlap = true
                speakers.forEach {
                    val mySpeakerPhys = pm.get(it)

                    if (myListenerPhys.rect.overlaps(mySpeakerPhys.rect)) {
                        noOverlap = false
                    }
                }
                if (noOverlap) myListener.doneListening = true
            }
        }
    }
}