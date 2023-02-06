package engine

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

class Window private constructor() {
    private var window: Long = 0L

    private val title = "Villain building game"
    private val width = 800
    private val height = 600

    companion object {
        private var INSTANCE: Window? = null

        fun getInstance(): Window {
            if (INSTANCE == null) {
                INSTANCE = Window()
            }
            return INSTANCE!!
        }
    }

    init {
        GLFWErrorCallback.createPrint(System.err).set()

        require(glfwInit()) { "Unable to initialize GLFW" }

        // Configure GLFW
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = glfwCreateWindow(width, height, title, NULL, NULL)
        if (window == 0L) {
            throw RuntimeException("Failed to create window")
        }

        glfwSetKeyCallback(window, KeyListener::keyCallback)
        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback)
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback)
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback)

        // Get the thread stack and push a new frame
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                window,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        glfwMakeContextCurrent(window)
        // Enable v-sync
        glfwSwapInterval(1)

        glfwShowWindow(window)
    }

    fun resizeViewport() =
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            glfwGetWindowSize(window, pWidth, pHeight)
            glViewport(0, 0, pWidth.get(), pHeight.get())
        }

    fun swapBuffers() = glfwSwapBuffers(window)

    fun destroy() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)

        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    fun windowShouldClose(): Boolean = glfwWindowShouldClose(window)

}