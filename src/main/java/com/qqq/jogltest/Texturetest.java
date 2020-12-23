package com.qqq.jogltest;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.qqq.utils.JoglUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * @author Johnson
 * 2020/12/19
 */
public class Texturetest  implements GLEventListener {

    private final GLU glu = new GLU();
    private int program;
    private int shaderColor;
    private int shaderPosition;
    private int shaderTextureCoord;
    private int[] shaderLocation = new int[16];

    private final int[] vao = new int[1];
    private final int[] vbo = new int[1];
    private final int[] ebo = new int[1];
    private final int[] texture = new int[1];
    private final int[] texture1 = new int[1];
    private long createTime;

    float[] vertices = {
            // Positions          // Colors           // Texture Coords
            0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // Top Right
            0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // Bottom Right
            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // Bottom Left
            -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f  // Top Left
    };

    int[] indices = {  // Note that we start from 0!
            0, 1, 3, // First Triangle
            1, 2, 3  // Second Triangle
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
        //gl.glEnable(GL4.GL_TEXTURE_2D);

        createTime = System.currentTimeMillis();

        this.program = JoglUtils.createProgram(gl,"D:\\Document\\texture\\textureShader.vs","D:\\Document\\texture\\textureShader.frag");

        this.shaderPosition = gl.glGetAttribLocation(this.program, "position");
        shaderLocation[0] = this.shaderPosition;
        this.shaderColor = gl.glGetAttribLocation(this.program,"color");
        shaderLocation[1] = this.shaderColor;
        this.shaderTextureCoord = gl.glGetAttribLocation(this.program,"texCoord");
        shaderLocation[2] = this.shaderTextureCoord;
        // Create the mesh
        createBuffer(gl, shaderLocation, vertices,indices);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL4 gl = glAutoDrawable.getGL().getGL4();
        gl.glDeleteVertexArrays(vao.length, vao, 0);
        gl.glDeleteBuffers(vbo.length,vbo,0);
        gl.glDeleteBuffers(ebo.length,ebo,0);
        gl.glDeleteProgram(program);
    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL4 gl =glAutoDrawable.getGL().getGL4();
        //gl.glBindFramebuffer(GL4.GL_DRAW_FRAMEBUFFER, 0);
        gl.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, texture[0]);
        gl.glUniform1i(gl.glGetUniformLocation(this.program, "ourTexture1"), 0);
        gl.glActiveTexture(GL4.GL_TEXTURE1);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, texture1[0]);
        gl.glUniform1i(gl.glGetUniformLocation(this.program, "ourTexture2"), 1);
        // Activate shader
        gl.glUseProgram(this.program);

        Matrix4f transform = new Matrix4f();
        transform.translate(new Vector3f(0.5f, -0.5f, 0.0f));
        transform.rotate(10.0f*(System.currentTimeMillis() - createTime),new Vector3f(0.0f, 0.0f, 1.0f));

        //Mat4 projection = glm.perspective_(45.0f, 4.0f/3.0f, 0.1f, 100.0f);
        int transformLoc = gl.glGetUniformLocation(this.program, "transform");
        FloatBuffer floatBuffer = Buffers.newDirectFloatBuffer(16);
        gl.glUniformMatrix4fv(transformLoc, 1, false,transform.get(floatBuffer));

        // Draw container
        gl.glBindVertexArray(this.vao[0]);
        gl.glDrawElements(GL4.GL_TRIANGLES, 6, GL4.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL4 gl = glAutoDrawable.getGL().getGL4();
        gl.glViewport(x, y, width, height);
    }

    private void createBuffer(final GL4 gl, final int[] shaderLocation, float[] values,int[] indices) {

        gl.glGenVertexArrays(this.vao.length, this.vao, 0);
        gl.glBindVertexArray(this.vao[0]);

        //创建定点缓冲对象
        gl.glGenBuffers(this.vbo.length, this.vbo, 0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo[0]);
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(values);
        int bufferSizeInBytes = values.length * Buffers.SIZEOF_FLOAT;
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, bufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);

        gl.glGenBuffers(this.ebo.length,this.ebo,0);
        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER,this.ebo[0]);
        IntBuffer ibVertices = Buffers.newDirectIntBuffer(indices);
        bufferSizeInBytes = indices.length * Buffers.SIZEOF_INT;
        gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, bufferSizeInBytes, ibVertices, GL4.GL_STATIC_DRAW);

        // Position attribute
        gl.glVertexAttribPointer(shaderLocation[0], 3, GL4.GL_FLOAT,false,8*Buffers.SIZEOF_FLOAT,0);
        gl.glEnableVertexAttribArray(0);
        // Color attribute
        gl.glVertexAttribPointer(shaderLocation[1], 3, GL4.GL_FLOAT,false,8*Buffers.SIZEOF_FLOAT,3*Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(1);
        //texture
        gl.glVertexAttribPointer(shaderLocation[2], 2, GL4.GL_FLOAT,false,8*Buffers.SIZEOF_FLOAT,6*Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(2);

        gl.glBindVertexArray(0);


        //加载和创建纹理
        JoglUtils.createGlTexture(gl,"D:\\Document\\texture\\container.jpg",texture,GL4.GL_REPEAT,GL4.GL_REPEAT,GL4.GL_LINEAR,GL4.GL_LINEAR);

        JoglUtils.createGlTexture(gl,"D:\\Document\\texture\\awesomeface.png",texture1,GL4.GL_REPEAT,GL4.GL_REPEAT,GL4.GL_LINEAR,GL4.GL_LINEAR);

    }
}
