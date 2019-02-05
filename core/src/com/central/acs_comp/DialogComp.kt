package com.central.acs_comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.central.AppObj
import com.central.dialog.DialogBox
import com.central.dialog.TypewriterAct

class DialogComp(lines: ArrayList<String>) : Component {
    var triggered = false

    var myLines = lines
    var mySequence = sequence()

    init {

    }

    fun showDialog() {
        AppObj.dialogStg.actors.forEach {
            if (it is DialogBox) {
                it.dialogLabel.clearActions()
                it.isVisible = true
                mySequence = sequence()
                myLines.forEach {
                    mySequence.addAction(TypewriterAct(it))
                }
                this.mySequence.addAction(Actions.run {
                    it.isVisible = false
                    AppObj.paused = false
                    AppObj.dialogMode = false
                    this.triggered = false
                    AppObj.ic.aPressed = false
                })
                it.dialogLabel.addAction(this.mySequence)
            }
        }
    }
}
