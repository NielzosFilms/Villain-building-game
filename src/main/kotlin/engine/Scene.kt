package engine

abstract class Scene {
    abstract fun init()

    abstract fun tick()
    abstract fun render()
}