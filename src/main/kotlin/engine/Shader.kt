package engine

import org.lwjgl.opengl.GL20.*

class Shader(vertexShaderPath: String, fragmentShaderPath: String) {
    private val vertexShaderSrc =
        Shader::class.java.classLoader.getResource("shaders/$vertexShaderPath")!!.readText()
    private val fragmentShaderSrc =
        Shader::class.java.classLoader.getResource("shaders/$fragmentShaderPath")!!.readText()

    private var vertexShader: Int = -1
    private var fragmentShader: Int = -1
    private var shaderProgram: Int = -1

    fun compile() {
        compileVertexShader()
        compileFragmentShader()
        linkToShaderProgram()
    }

    private fun compileVertexShader() {
        vertexShader = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vertexShader, vertexShaderSrc)
        glCompileShader(vertexShader)
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw RuntimeException(
                "Failed to compile vertex shader \n${
                    glGetShaderInfoLog(
                        vertexShader,
                        glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH)
                    )
                }"
            )
        }
    }

    private fun compileFragmentShader() {
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fragmentShader, fragmentShaderSrc)
        glCompileShader(fragmentShader)
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw RuntimeException(
                "Failed to compile fragment shader \n${
                    glGetShaderInfoLog(
                        fragmentShader,
                        glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH)
                    )
                }"
            )
        }
    }

    private fun linkToShaderProgram() {
        shaderProgram = glCreateProgram()
        glAttachShader(shaderProgram, vertexShader)
        glAttachShader(shaderProgram, fragmentShader)
        glLinkProgram(shaderProgram)

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw RuntimeException(
                "Failed to link shaders \n${
                    glGetProgramInfoLog(
                        shaderProgram,
                        glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)
                    )
                }"
            )
        }
    }

    fun use() = glUseProgram(shaderProgram)
    fun detach() = glUseProgram(0)
}