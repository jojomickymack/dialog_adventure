package com.central.acs_comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComp : Component {
    var defaultTex = Texture(Gdx.files.internal("default.png"))
    var region = TextureRegion(defaultTex, 0, 0, defaultTex.width, defaultTex.height)

    var regions: Array<TextureRegion> = region.split(defaultTex.width, defaultTex.height)[0]
    var stand: Animation<TextureRegion> = Animation(0f, regions[0])
    var walk: Animation<TextureRegion> = Animation(0f, regions[0])
    var jump: Animation<TextureRegion> = Animation(0f, regions[0])

    fun loadSheet(tex: Texture, numRegions: Int) {
        defaultTex = tex
        region = TextureRegion(defaultTex, 0, 0, defaultTex.width, defaultTex.height)
        regions = region.split(defaultTex.width / numRegions, defaultTex.height)[0]
    }
}