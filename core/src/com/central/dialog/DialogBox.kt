package com.central.dialog

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.central.AppObj

class DialogBox : Dialog("", AppObj.skin, "default") {
    val dialogLabel = Label("", AppObj.skin, "default")
    private val fadeDuration = 0.5f
    private val outerPadding = 16f
    private val padding = 42f

    init {
        dialogLabel.setWrap(true)
        dialogLabel.setAlignment(Align.center)
        this.setDialogSize(this.width, this.height, 0f, 0f)
        this.contentTable.add(dialogLabel).size(this.width - 2 * this.padding, this.height - 2 * this.padding)
    }

    fun setDialogSize(width: Float, height: Float, x: Float, y: Float) {
        this.setSize(width - outerPadding * 2, height - outerPadding * 2)
        this.setPosition(x + outerPadding, y + outerPadding)
        dialogLabel.setSize(this.width - 2 * this.padding, this.height -2 * this.padding)
        this.contentTable.clear()
        this.contentTable.add(dialogLabel).size(this.width - 2 * this.padding, this.height - 2 * this.padding)
    }
}