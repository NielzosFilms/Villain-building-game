package engine

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

class KeyListener {
    private val keysPressed: MutableMap<Int, Boolean> = mutableMapOf()

    companion object {
        private var INSTANCE: KeyListener? = null

        fun getInstance(): KeyListener {
            if (INSTANCE == null) {
                INSTANCE = KeyListener()
            }
            return INSTANCE!!
        }

        fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (action == GLFW_PRESS) {
                getInstance().keysPressed[key] = true
            } else if (action == GLFW_RELEASE) {
                getInstance().keysPressed[key] = false
            }
        }

        fun isKeyPressed(keyCode: Int): Boolean = getInstance().keysPressed[keyCode] ?: false
    }
}
