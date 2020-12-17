package com.qqq.jogltest;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.qqq.texture.Robot;

import java.awt.*;


public class CubeRobot implements GLEventListener {


    private GL2 gl;
    private GLU glu;
    private Robot robot;
    private float rotationAngle;

    float xRot;
    float yRot;
    float zRot;

    float xSpeed;
    float zSpeed;

    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        glu = new GLU();
        robot = new Robot(gl);
        rotationAngle = 0.0f;

        xRot = 0.0f;
        yRot = 0.0f;
        zRot = -30.0f;

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        int w = ((Component) glAutoDrawable).getWidth();
        int h = ((Component) glAutoDrawable).getHeight();

        gl.glViewport(0, 0, w, h);		// 设置视点大小reset the viewport to new dimensions
        gl.glMatrixMode(GL2.GL_PROJECTION);			//设置当前矩阵模式 set projection matrix current matrix
        gl.glLoadIdentity();						//把当前绘画点移动到屏幕中心 reset projection matrix

        //设置摄像机的拍摄性能 calculate aspect ratio of window
        glu.gluPerspective(52.0f, (float) w / (float) h, 1.0f, 1000.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);				//设置当前矩阵模式 set modelview matrix
        gl.glLoadIdentity();                          //把当前绘画点移动到屏幕中心 reset modelview matrix

        //播放音乐,不要就注释掉
        //new AePlayWave("wave/run.wav").start();

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        //刷新背景颜色 clear screen and depth buffer
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        //把当前绘画点移动到屏幕中心 load the identity matrix (clear to default position and orientation)
        gl.glLoadIdentity();

        gl.glPushMatrix();							//保存当前矩阵 put current matrix on stack
        gl.glLoadIdentity();					//把当前绘画点移动到屏幕中心  reset matrix
        gl.glTranslatef(xRot, yRot, zRot);	//把当前绘画点移动到(x,y,z) move to (0, 0, -30)
        //gl.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);	//旋转(角度值,x轴,Y轴,Z轴) rotate the robot on its y-axis
        robot.DrawRobot(0.0f, 0.0f, 0.0f);		//画机器人 draw the robot
        gl.glPopMatrix();

        this.walk(0.2f);
        //this.Prepare(0.2f);          //刷新机器人动作

    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void walk(float speed){
        xRot += 1.0f*xSpeed;
        //yRot += 5.0f*speed;
        zRot += 1.0f*zSpeed;
        if(zRot>=0){
            zRot = -zRot;
        }
        robot.Prepare(speed);
    }

    public void Prepare(float dt) {
        rotationAngle += 45.0f * dt;	// increase our rotation angle counter
        if (rotationAngle >= 360.0f) // if we've gone in a circle, reset counter
        {
            rotationAngle = 0.0f;
        }
        robot.Prepare(dt);
    }

}
