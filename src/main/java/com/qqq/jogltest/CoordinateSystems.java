package com.qqq.jogltest;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.qqq.utils.JoglUtils;
import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Johnson
 * 2020/12/21
 */
public class CoordinateSystems implements GLEventListener {

    GL4 gl;
    private int program;

    private int[] shaderLocation = new int[16];
    private final int[] vao = new int[1];
    private final int[] vbo = new int[1];
    private final int[] ebo = new int[1];
    private final int[] texture = new int[1];
    private final int[] texture1 = new int[1];

    float[] vertices = {
            // Positions          // Texture Coords
            0.5f,  0.5f, 0.0f,   1.0f, 1.0f, // Top Right
            0.5f, -0.5f, 0.0f,   1.0f, 0.0f, // Bottom Right
            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // Bottom Left
            -0.5f,  0.5f, 0.0f,   0.0f, 1.0f  // Top Left
    };
    int[] indices = {  // Note that we start from 0!
            0, 1, 3, // First Triangle
            1, 2, 3  // Second Triangle
    };


    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL4();
        JoglUtils.clearColorAndDepth(gl,1.0f, 0.0f, 1.0f, 1.0f,1.0f);
        JoglUtils.enableCullFace(gl,GL4.GL_BACK,GL4.GL_CW);//清除背面绘制 清除背面顺时针绘制为正
        JoglUtils.enableDepthtest(gl,GL4.GL_LEQUAL);//深度测试函数为GL_LEQUAL
        //创建着色器程序
        this.program = JoglUtils.createProgram(gl,"D:\\qqqworkspaces\\qqqjogl\\src\\main\\resources\\coordinateShader.vs","D:\\qqqworkspaces\\qqqjogl\\src\\main\\resources\\coordinateShader.frag");

        int position = gl.glGetAttribLocation(this.program, "position");
        shaderLocation[0] = position;
        int texCoord = gl.glGetAttribLocation(this.program,"texCoord");
        shaderLocation[2] = texCoord;
        createBuffer(gl, shaderLocation, vertices,indices);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL4 gl = glAutoDrawable.getGL().getGL4();
        gl.glDeleteBuffers(ebo.length,ebo,0);
        gl.glDeleteVertexArrays(vao.length, vao, 0);
        gl.glDeleteBuffers(vbo.length,vbo,0);
        gl.glDeleteProgram(program);
    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL4 gl =glAutoDrawable.getGL().getGL4();
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

        Mat4 model = new Mat4();
        Mat4 view = new Mat4();
        Mat4 projection = new Mat4();
        model.rotate(-55.0f, new Vec3(1.0f, 0.0f, 0.0f));
        view.translate(new Vec3(0.0f, 0.0f, -3.0f));
        Glm.perspective(45.0f, (float) 800/600, 0.1f, 100.0f,projection);

        int modelLoc = gl.glGetUniformLocation(this.program, "model");
        int viewLoc = gl.glGetUniformLocation(this.program, "view");
        int projLoc = gl.glGetUniformLocation(this.program, "projection");

        gl.glUniformMatrix4fv(modelLoc, 1, false,Buffers.newDirectFloatBuffer(model.toFa_()));
        gl.glUniformMatrix4fv(viewLoc, 1, false,Buffers.newDirectFloatBuffer(view.toFa_()));
        gl.glUniformMatrix4fv(projLoc, 1, false,Buffers.newDirectFloatBuffer(projection.toFa_()));

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
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(values);
        createGenBuffers(gl,this.vbo,GL4.GL_ARRAY_BUFFER,fbVertices);

        IntBuffer ibVertices = Buffers.newDirectIntBuffer(indices);
        createGenBuffers(gl,this.ebo,GL4.GL_ELEMENT_ARRAY_BUFFER,ibVertices);

        // Position attribute
        gl.glVertexAttribPointer(shaderLocation[0], 3, GL4.GL_FLOAT,false,5*Buffers.SIZEOF_FLOAT,0);
        gl.glEnableVertexAttribArray(0);
        // Color attribute
        //texture
        gl.glVertexAttribPointer(shaderLocation[2], 2, GL4.GL_FLOAT,false,5*Buffers.SIZEOF_FLOAT,3*Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(2);

        gl.glBindVertexArray(0);


        //加载和创建纹理
        JoglUtils.createGlTexture(gl,"D:\\qqqworkspaces\\qqqjogl\\src\\main\\resources\\container.jpg",texture,GL4.GL_REPEAT,GL4.GL_REPEAT,GL4.GL_LINEAR,GL4.GL_LINEAR);

        JoglUtils.createGlTexture(gl,"D:\\qqqworkspaces\\qqqjogl\\src\\main\\resources\\awesomeface.png",texture1,GL4.GL_REPEAT,GL4.GL_REPEAT,GL4.GL_LINEAR,GL4.GL_LINEAR);

    }

    private void createGenBuffers(final GL4 gl, int[] values,int bufferType, Buffer buffer){
        gl.glGenBuffers(values.length, values, 0);
        gl.glBindBuffer(bufferType, values[0]);
        if(bufferType==GL4.GL_ARRAY_BUFFER){
            int bufferSizeInBytes = values.length * Buffers.SIZEOF_FLOAT;
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, bufferSizeInBytes, buffer, GL4.GL_STATIC_DRAW);
        }else if(bufferType == GL4.GL_ELEMENT_ARRAY_BUFFER){
            int bufferSizeInBytes = indices.length * Buffers.SIZEOF_INT;
            gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, bufferSizeInBytes, buffer, GL4.GL_STATIC_DRAW);
        }
    }
}
