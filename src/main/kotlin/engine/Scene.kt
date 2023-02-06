package engine

abstract class Scene {
    abstract fun init()

    abstract fun update(dt: Float)
}
