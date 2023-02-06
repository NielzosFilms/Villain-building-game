package engine

class LevelScene : Scene() {
    private lateinit var shader: Shader
    private lateinit var model: ModelLoader

    override fun init() {
        shader = Shader("vertexShader.glsl", "fragmentShader.glsl")
        shader.compile()

        model = ModelLoader()
        model.init()
    }

    override fun update(dt: Float) {
        shader.use()
        model.render()
        shader.detach()
    }
}
