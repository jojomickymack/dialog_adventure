package com.central.acs_sys

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.central.AppObj
import com.central.acs_comp.AiControlComp
import com.central.acs_comp.PhysicsComp
import com.central.acs_comp.UserControlComp

class MapSys : EntitySystem() {

    val map = TmxMapLoader().load("map/map.tmx")
    val mr = OrthogonalTiledMapRenderer(map, AppObj.unitScale)
    val mr2 = OrthogonalTiledMapRenderer(map, AppObj.unitScale)

    val inFront = map.layers.get("in_front") as TiledMapTileLayer
    val solid = map.layers.get("solid") as TiledMapTileLayer
    val behind = map.layers.get("behind") as TiledMapTileLayer
    val background = map.layers.get("background") as TiledMapTileLayer
    val enemyLayer = map.layers.get("enemies") as MapLayer

    private val tiles = Array<Rectangle>()

    private var player = ImmutableArray(Array<Entity>())
    private var ai = ImmutableArray(Array<Entity>())

    // create a pool of rectangle objects for collision detection
    private val rectPool = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle {
            return Rectangle()
        }
    }

    init {

    }

    override fun addedToEngine(engine: Engine) {
        player = engine.getEntitiesFor(Family.all(PhysicsComp::class.java, UserControlComp::class.java).get())
        ai = engine.getEntitiesFor(Family.all(PhysicsComp::class.java, AiControlComp::class.java).get())
    }

    fun getTiles(startX: Int, startY: Int, endX: Int, endY: Int, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        rectPool.freeAll(tiles)

        tiles.clear()

        for (y in startY..endY) {
            for (x in startX..endX) {
                val cell = tileLayer.getCell(x, y)
                if (cell != null) {
                    val rect = rectPool.obtain()
                    rect.set(x.toFloat(), y.toFloat(), 1f, 1f)
                    tiles.add(rect)
                }
            }
        }
        return tiles
    }

    fun getHorizNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startY = rect.y.toInt()
        val endY = (rect.y + rect.height).toInt()

        // if the sprite is moving right, get the tiles to its right side
        // if the sprite is moving left, get the tiles to its left side
        val startX = if (velocity.x > 0) (rect.x + rect.width).toInt() else rect.x.toInt()
        val endX = startX

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    fun getVertNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startX = rect.x.toInt()
        val endX = (rect.x + rect.width).toInt()
        // if sprite is moving up, get the tiles above it
        // if sprite is moving down, get the tiles below it
        val startY = if (velocity.y > 0) (rect.y + rect.height).toInt() else rect.y.toInt()
        val endY = startY

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    override fun update(deltaTime: Float) {

        player.forEach {
            val physics = it.getComponent(PhysicsComp::class.java)

            val testRect = Rectangle()

            testRect.set(physics.rect.x * AppObj.unitScale, physics.rect.y * AppObj.unitScale, physics.w * AppObj.unitScale, physics.h * AppObj.unitScale)

            var myTiles = getVertNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    with(physics) {
                        if (vel.y > 0) {
                            pos.y = it.y / AppObj.unitScale - testRect.height / AppObj.unitScale
                            rect.y = it.y - rect.height
                        } else if (vel.y < 0) {
                            pos.y = it.y / AppObj.unitScale + it.height / AppObj.unitScale
                            rect.y = it.y + it.height
                            grounded = true
                            AppObj.ic.aPressed = false
                        }
                        vel.y = 0f
                    }
                }
            }

            testRect.set(physics.pos.x * AppObj.unitScale, physics.pos.y * AppObj.unitScale, physics.w * AppObj.unitScale, physics.h * AppObj.unitScale)

            myTiles = getHorizNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    physics.rect.x -= physics.vel.x * AppObj.unitScale * 0.8f
                    physics.vel.x = 0f
                }
            }
        }

        ai.forEach {
            val physics = it.getComponent(PhysicsComp::class.java)

            val testRect = Rectangle()

            testRect.set(physics.rect.x * AppObj.unitScale, physics.rect.y * AppObj.unitScale, physics.w * AppObj.unitScale, physics.h * AppObj.unitScale)

            var myTiles = getVertNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    with(physics) {
                        if (vel.y > 0) {
                            pos.y = it.y / AppObj.unitScale - testRect.height / AppObj.unitScale
                            rect.y = it.y - rect.height
                        } else if (vel.y < 0) {
                            pos.y = it.y / AppObj.unitScale + it.height / AppObj.unitScale
                            rect.y = it.y + it.height
                            grounded = true
                        }
                        vel.y = 0f
                    }
                }
            }

            testRect.set(physics.pos.x * AppObj.unitScale, physics.pos.y * AppObj.unitScale, physics.w * AppObj.unitScale, physics.h * AppObj.unitScale)

            myTiles = getHorizNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    physics.direction *= -1
                    physics.rect.x -= physics.vel.x * AppObj.unitScale
                    physics.facesRight = !physics.facesRight
                    return
                }
            }

            with(physics) {
                if (grounded == true) {
                    if (facesRight) testRect.set(pos.x * AppObj.unitScale + 1, pos.y * AppObj.unitScale - 1, w * AppObj.unitScale, h * AppObj.unitScale)
                    else testRect.set(pos.x * AppObj.unitScale - 1, pos.y * AppObj.unitScale - 1, w * AppObj.unitScale, h * AppObj.unitScale)
                    myTiles = getVertNeighbourTiles(vel, testRect, solid)

                    if (myTiles.size == 0) {
                        direction *= -1
                        rect.x -= vel.x * AppObj.unitScale
                        facesRight = !facesRight
                        return
                    }
                }
            }
        }

    }
}
