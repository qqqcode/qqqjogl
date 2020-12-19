package com.qqq.jogltest;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.qqq.utils.JoglUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;


/**
 * @author Johnson
 * 2020/12/18
 */
public class VerTextShaderTest implements GLEventListener {

    private String vertexShaderSource =
            "#version 410 core \n"+
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
    private int shaderColor;
    private int shaderPosition;
    private int[] shaderLocation = new int[16];

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
        gl.glEnable(GL4.GL_CULL_FACE);//清除背面绘制
        gl.glCullFace(GL4.GL_BACK);//清除背面
        gl.glFrontFace(GL4.GL_CW);//顺时针绘制为正
        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthFunc(GL4.GL_LEQUAL);

        this.program = JoglUtils.createProgram(gl,"D:\\qqqworkspaces\\qqqjogl\\src\\main\\resources\\shader.vs","D:\\qqqworkspaces\\qqqjogl\\src\\main\\resources\\shader.frag");
        this.shaderPosition = gl.glGetAttribLocation(this.program, "position");
        shaderLocation[0] = this.shaderPosition;
        this.shaderColor = gl.glGetAttribLocation(this.program,"color");
        shaderLocation[1] = this.shaderColor;
        // Create the mesh
        createBuffer(gl, shaderLocation, two_triangles, 3);
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

    private void createBuffer(final GL4 gl, final int[] shaderLocation, float[] values, final int valuesPerVertex) {

        gl.glGenVertexArrays(this.vao.length, this.vao, 0);
        gl.glBindVertexArray(this.vao[0]);

        //创建定点缓冲对象
        gl.glGenBuffers(this.vbo.length, this.vbo, 0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo[0]);
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(values);
        final int bufferSizeInBytes = values.length * Buffers.SIZEOF_FLOAT;
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, bufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);


        //gl.glVertexAttribPointer(shaderAttribute, valuesPerVertex, GL4.GL_FLOAT, false, 0, 0);
        // Position attribute
        gl.glVertexAttribPointer(shaderLocation[0], valuesPerVertex, GL4.GL_FLOAT,false,6*Buffers.SIZEOF_FLOAT,0*Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(0);
        // Color attribute
        gl.glVertexAttribPointer(shaderLocation[1], valuesPerVertex, GL4.GL_FLOAT,false,6*Buffers.SIZEOF_FLOAT,3*Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(1);
    }
}
