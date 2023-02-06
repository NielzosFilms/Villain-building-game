import engine.LevelScene
import engine.Scene
import engine.Window
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*
import kotlin.math.round

class Game {
    private val logger = KotlinLogging.logger {}
    private lateinit var window: Window

    private var currScene: Scene = LevelScene()

    companion object {
        var currentFps: Int = 0
        var wireFrame = false
    }

    fun run() {
        logger.info { "LWJGL @${Version.getVersion()}" }

        init()
        loop()

        cleanup()
    }

    private fun init() {
        window = Window.getInstance()

        GL.createCapabilities()
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f)

        currScene.init()
    }

    private fun loop() {
        var lastFrameTime = -1f

        while (!window.windowShouldClose()) {
            val time = glfwGetTime().toFloat()
            val dt = if (lastFrameTime == -1f) {
                1f / 60f
            } else {
                time - lastFrameTime
            }
            lastFrameTime = time

            currentFps = round(1f / dt).toInt()

            update(dt)
        }
    }

    private fun update(dt: Float) {
        glfwPollEvents()
        window.resizeViewport()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        if (wireFrame) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
        }

        currScene.update(dt)

        window.swapBuffers()
    }

    private fun cleanup() {
        window.destroy()
    }
}
