package engine

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ModelLoader {
    private val vertices: FloatArray = floatArrayOf(
        -0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.5f, -0.5f, 0.0f, 0.0f, 0.9f, 0.9f, 1.0f,
        -0.5f, -0.5f, 0.0f, 0.0f, 0.9f, 0.9f, 1.0f,
    )

    private val elements: IntArray = intArrayOf(
        0, 1, 2,
        0, 2, 3
    )

    private var vbo: Int = -1
    private var vao: Int = -1
    private var ebo: Int = -1

    fun init() {
        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val vertexBuffer: FloatBuffer = BufferUtils.createFloatBuffer(vertices.size)
        vertexBuffer.put(vertices).flip()

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

        val elementBuffer: IntBuffer = BufferUtils.createIntBuffer(elements.size)
        elementBuffer.put(elements).flip()

        ebo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW)

        val positionSize = 3
        val colorSize = 4
        val floatSizeBytes = 4
        val vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, (positionSize * floatSizeBytes).toLong())
        glEnableVertexAttribArray(1)
    }

    fun render() {
        glBindVertexArray(vao)

        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, elements.size, GL_UNSIGNED_INT, 0)
    }
}
