package engine

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

class MouseListener {
    private var scrollX: Double = 0.0
    private var scrollY: Double = 0.0
    private var xPos: Double = 0.0
    private var yPos: Double = 0.0
    private var lastX: Double = 0.0
    private var lastY: Double = 0.0
    private val mouseButtonPressed: MutableMap<Int, Boolean> = mutableMapOf()
    private var isDragging: Boolean = false

    companion object {
        private var INSTANCE: MouseListener? = null

        fun getInstance(): MouseListener {
            if (INSTANCE == null) {
                INSTANCE = MouseListener()
            }
            return INSTANCE!!
        }

        fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
            val instance = getInstance()
            instance.lastX = instance.xPos
            instance.lastY = instance.yPos
            instance.xPos = xPos
            instance.yPos = yPos
            instance.isDragging =
                instance.mouseButtonPressed[0] ?: false || instance.mouseButtonPressed[1] ?: false || instance.mouseButtonPressed[2] ?: false
        }

        fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
            val instance = getInstance()
            if (action == GLFW_PRESS) {
                instance.mouseButtonPressed[button] = true
            } else if (action == GLFW_RELEASE) {
                instance.mouseButtonPressed[button] = false
                instance.isDragging = false
            }
        }

        fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
            getInstance().scrollX = xOffset
            getInstance().scrollY = yOffset
        }

        fun endFrame() {
            val instance = getInstance()
            instance.scrollX = 0.0
            instance.scrollY = 0.0
            instance.lastX = instance.xPos
            instance.lastY = instance.yPos
        }

        fun getX(): Float = getInstance().xPos.toFloat()
        fun getY(): Float = getInstance().yPos.toFloat()
        fun getDX(): Float = getInstance().lastX.toFloat()
        fun getDY(): Float = getInstance().lastY.toFloat()
        fun getScrollX(): Float = getInstance().scrollX.toFloat()
        fun getScrollY(): Float = getInstance().scrollY.toFloat()
        fun isDragging(): Boolean = getInstance().isDragging
        fun mouseButtonDown(button: Int): Boolean = getInstance().mouseButtonPressed[button] ?: false
    }
}
