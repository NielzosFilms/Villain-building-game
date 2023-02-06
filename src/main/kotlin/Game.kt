import engine.LevelScene
import engine.Scene
import engine.Window
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*


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
        var lastTime = System.nanoTime()
        val amountOfTicks = 60.0
        val tickNs = 1_000_000_000 / amountOfTicks
        val framesNs = 1_000_000_000 / 144.0
        var tickDelta = 0.0
        var framesDelta = 0.0
        var timer = System.currentTimeMillis()
        var frames = 0

        while (!window.windowShouldClose()) {
            val now = System.nanoTime()
            tickDelta += (now - lastTime) / tickNs
            framesDelta += (now - lastTime) / framesNs
            lastTime = now
            while (tickDelta >= 1) {
                tick()
                tickDelta--
            }

            if (framesDelta >= 1) {
                render()
                frames++
                framesDelta--
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000
                currentFps = frames
                frames = 0
            }
        }
    }

    private fun tick() {
        glfwPollEvents()
    }

    private fun render() {
        window.resizeViewport()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        if (wireFrame) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
        }

        currScene.render()

        window.swapBuffers()
    }

    private fun cleanup() {
        window.destroy()
    }
}