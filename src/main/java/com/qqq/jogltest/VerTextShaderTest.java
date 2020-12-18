package com.qqq.jogltest;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

import java.nio.FloatBuffer;


/**
 * @author Johnson
 * 2020/12/18
 */
public class VerTextShaderTest implements GLEventListener {

    private String vertexShaderSource =
            "#version 330 core \n"+
                    "layout (location = 0) in vec3 position;\n"+
                    "layout (location = 1) in vec3 color; \n"+
                    "out vec3 ourColor; \n"+
                    "void main() \n"+
                    "{ \n"+
                        "gl_Position = vec4(position, 1.0); \n"+
                        "ourColor = color;  \n"+
                    "}                                              \n";
    private String fragmentShaderSource =
            "#version 410 core \n"+
            "in vec3 ourColor;\n"+
            "out vec4 color;\n"+
            "void main()\n"+
            "{\n"+
                "color = vec4(ourColor, 1.0f);\n"+
            "}       \n";

    private int program;
    private int shaderPosition;

    private final int[] vao = new int[1];
    private final int[] vbo = new int[1];

    private float[] two_triangles = new float[]{
            0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,  // Bottom Right
            -0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  // Bottom Left
            0.0f,  0.5f, 0.0f,  0.0f, 0.0f, 1.0f
    };

    public void init(GLAutoDrawable glAutoDrawable) {
        GL4 gl = glAutoDrawable.getGL().getGL4();

        gl.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL4.GL_CULL_FACE);
        gl.glCullFace(GL4.GL_BACK);
        gl.glFrontFace(GL4.GL_CCW);
        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthFunc(GL4.GL_LEQUAL);

        this.program = createProgram(gl);
        this.shaderPosition = gl.glGetAttribLocation(this.program, "position");
        // Create the mesh
        createBuffer(gl, shaderPosition, two_triangles, 3);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL4 gl = glAutoDrawable.getGL().getGL4();
        gl.glDeleteVertexArrays(vao.length, vao, 0);
        gl.glDeleteBuffers(vbo.length,vbo,0);
        gl.glDeleteProgram(program);
    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL4 gl =glAutoDrawable.getGL().getGL4();
        gl.glBindFramebuffer(GL4.GL_DRAW_FRAMEBUFFER, 0);
        gl.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(this.program);
        gl.glBindVertexArray(this.vao[0]);
        gl.glEnableVertexAttribArray(this.shaderPosition);
        //gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 6);
        gl.glDrawArrays(GL4.GL_TRIANGLES,0,3);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL4 gl = glAutoDrawable.getGL().getGL4();
        gl.glViewport(x, y, width, height);
    }

    private int createProgram(GL4 gl) {
        ShaderCode vertexShaderCode = compileShader(gl,vertexShaderSource,GL4.GL_VERTEX_SHADER);
        ShaderCode fragmentShaderCode = compileShader(gl,fragmentShaderSource,GL4.GL_FRAGMENT_SHADER);
        ShaderProgram program = linkShader(gl,vertexShaderCode,fragmentShaderCode);
        return program.program();
    }

    private ShaderCode compileShader(final GL4 gl,final String source,final int shaderType){
        String[][] sources = new String[1][1];
        sources[0] = new String[]{source};
        ShaderCode shaderCode = new ShaderCode(shaderType, sources.length, sources);
        boolean compiled = shaderCode.compile(gl, System.err);
        if (!compiled){
            System.err.println("Unable to compile " + source);
            System.exit(1);
        }
        return shaderCode;
    }

    private ShaderProgram linkShader(final GL4 gl, final ShaderCode vertexShader, final ShaderCode fragmentShader) throws GLException {
        ShaderProgram program = new ShaderProgram();
        program.init(gl);
        program.add(vertexShader);
        program.add(fragmentShader);
        program.link(gl, System.out);
        final boolean validateProgram = program.validateProgram(gl, System.out);
        if (!validateProgram) {
            System.err.println("Unable to link shader");
            System.exit(1);
        }
        return program;
    }

    private void createBuffer(final GL4 gl, final int shaderAttribute, float[] values, final int valuesPerVertex) {

        gl.glGenVertexArrays(this.vao.length, this.vao, 0);
        gl.glBindVertexArray(this.vao[0]);

        gl.glGenBuffers(this.vbo.length, this.vbo, 0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo[0]);

        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(values);
        final int bufferSizeInBytes = values.length * Buffers.SIZEOF_FLOAT;
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, bufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);


        gl.glVertexAttribPointer(shaderAttribute, valuesPerVertex, GL4.GL_FLOAT, false, 0, 0);
    }
}
