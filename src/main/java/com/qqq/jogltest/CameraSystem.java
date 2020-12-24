package com.qqq.jogltest;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.qqq.utils.JoglUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Johnson
 * 2020/12/24
 */
public class CameraSystem implements GLEventListener{
    GL4 gl;
    private int program;

    float xkey;
    float ykey;

    private int[] shaderLocation = new int[16];
    private final int[] vao = new int[1];
    private final int[] vbo = new int[1];
    private final int[] ebo = new int[1];
    private final int[] texture = new int[1];
    private final int[] texture1 = new int[1];
    private String vsPath;
    private String fragPath;
    private String texturePath1 = this.getClass().getResource("/container.jpg").getPath();
    private String texturePath2 = this.getClass().getResource("/awesomeface.png").getPath();

    FreeCamera freeCamera = new FreeCamera();
    Camera camera = new Camera(0.0f, 0.0f, 3.0f);

    long initTime;

    float[] vertices = {
            // Positions          // Texture Coords
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,//b
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,//a
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,//d
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,//d
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//c
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,//b

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//a
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,//a1
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//d1
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//d1
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//d
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//a

            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,//b1
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//b
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//c
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//c
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//c1
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,//b1

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//a
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,//b
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,//b1
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,//b1
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,//a1
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//a

            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,//d1
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//c1
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//c
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//c
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,//d
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,//d1
    };
    int[] indices = {  // Note that we start from 0!
            0, 1, 3, // First Triangle
            1, 2, 3  // Second Triangle
    };


    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL4();
        JoglUtils.clearColorAndDepth(gl,1.0f, 0.0f, 1.0f, 1.0f,1.0f);
        JoglUtils.enableCullFace(gl,GL4.GL_BACK,GL4.GL_CCW);//清除背面绘制 清除背面逆时针绘制为正面，此时判断所有三角形绘制是否都是逆时针
        JoglUtils.enableDepthtest(gl,GL4.GL_LEQUAL);//深度测试函数为GL_LEQUAL
        //创建着色器程序
        vsPath = this.getClass().getResource("/cameraShader.vs").getPath();
        fragPath  = this.getClass().getResource("/cameraShader.frag").getPath();
        this.program = JoglUtils.createProgram(gl,vsPath,fragPath);

        initTime = System.currentTimeMillis();

        int position = gl.glGetAttribLocation(this.program, "position");
        shaderLocation[0] = position;
        int texCoord = gl.glGetAttribLocation(this.program,"texCoord");
        shaderLocation[2] = texCoord;

        createBuffer(gl, shaderLocation, vertices,indices);

//        camera.apply()
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL4 gl = glAutoDrawable.getGL().getGL4();
        gl.glDeleteBuffers(ebo.length,ebo,0);
        gl.glDeleteVertexArrays(vao.length, vao, 0);
        gl.glDeleteProgram(program);
        gl.glDeleteBuffers(vbo.length,vbo,0);
    }

    Vector3f[] cubePositions = {
            new Vector3f( 0.0f,  0.0f,  0.0f),
            new Vector3f( 2.0f,  5.0f, -15.0f),
            new Vector3f(-1.5f, -2.2f, -2.5f),
            new Vector3f(-3.8f, -2.0f, -12.3f),
            new Vector3f( 2.4f, -0.4f, -3.5f),
            new Vector3f(-1.7f,  3.0f, -7.5f),
            new Vector3f( 1.3f, -2.0f, -2.5f),
            new Vector3f( 1.5f,  2.0f, -2.5f),
            new Vector3f( 1.5f,  0.2f, -1.5f),
            new Vector3f(-1.3f,  1.0f, -1.5f)
    };

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


        Matrix4f view = new Matrix4f();
        Matrix4f projection = new Matrix4f();

        //System.out.println(System.currentTimeMillis());
//        float camX = (float) Math.sin((System.currentTimeMillis() - initTime)/1000)*10.0f;
//        float camZ = (float) Math.cos((System.currentTimeMillis() - initTime)/1000)*10.0f;
//        view.translate(new Vector3f(0.0f, 0.0f, -5.0f));
//        view.lookAt(new Vector3f(10.0f,0.0f,10.0f),new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.0f,1.0f,0.0f));
        view = freeCamera.apply(view);
        freeCamera.forward(new Vector3f(0.0f,0.0f,10.0f*xkey));
        System.out.println(xkey);
        projection.perspective(camera.zoom, (float)(4/3), 0.01f, 1000.0f);
        int modelLoc = gl.glGetUniformLocation(this.program,"model");
        JoglUtils.uniformMatrix4fv(gl,this.program,"view",view);
        JoglUtils.uniformMatrix4fv(gl,this.program,"projection",projection);

        gl.glBindVertexArray(this.vao[0]);
        //gl.glDrawElements(GL4.GL_TRIANGLES, 6, GL4.GL_UNSIGNED_INT, 0);
        //gl.glDrawArrays(GL4.GL_TRIANGLES,0,36);
        for (int i = 0; i < cubePositions.length; i++) {
            Matrix4f model = new Matrix4f();
            model.translate(cubePositions[i]).rotate(20.0f*i,new Vector3f(1.0f, 0.3f, 0.5f));
            gl.glUniformMatrix4fv(modelLoc, 1, false,model.get(Buffers.newDirectFloatBuffer(16)));
            gl.glDrawArrays(GL4.GL_TRIANGLES,0,36);
        }
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
        JoglUtils.initVBO(gl,values,this.vbo);

        JoglUtils.initEBO(gl,indices,this.ebo);

        // Position attribute
        gl.glVertexAttribPointer(shaderLocation[0], 3, GL4.GL_FLOAT,false,5*Buffers.SIZEOF_FLOAT,0);
        gl.glEnableVertexAttribArray(0);

        //texture
        gl.glVertexAttribPointer(shaderLocation[2], 2, GL4.GL_FLOAT,false,5*Buffers.SIZEOF_FLOAT,3*Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(2);

        gl.glBindVertexArray(0);

        //加载和创建纹理
        JoglUtils.createGlTexture(gl,texturePath1,texture,GL4.GL_REPEAT,GL4.GL_REPEAT,GL4.GL_LINEAR,GL4.GL_LINEAR);
        JoglUtils.createGlTexture(gl,texturePath2,texture1,GL4.GL_REPEAT,GL4.GL_REPEAT,GL4.GL_LINEAR,GL4.GL_LINEAR);

    }
}
