package com.qqq.jogltest;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.qqq.utils.BsipcUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GLRender extends JFrame implements GLEventListener, KeyListener ,Runnable{
    GLCanvas glCanvas;
    GLCapabilities glCapabilities;
    GLU glu;

    private float r;
    BsipcUtils m_bsipic;

    FPSAnimator animator;
    Thread myThread = new Thread(this);

    public GLRender() throws HeadlessException {
        this.glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        this.glCanvas = new GLCanvas(glCapabilities);
        this.glCanvas.addGLEventListener(this);
        this.getContentPane().add(this.glCanvas,BorderLayout.CENTER);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.animator = new FPSAnimator(glCanvas,300,true);
        this.setTitle("qqq");
        this.setVisible(true);
        this.setSize(1000,1000);
        centerWindow(this);
        myThread.start();
    }

    private void centerWindow(Component frame) { // 居中窗体
        Dimension frameSize = frame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        frame.setLocation((screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1);

    }


    public void keyPressed(KeyEvent keyEvent) {

    }

    public void keyReleased(KeyEvent keyEvent) {

    }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl=glAutoDrawable.getGL().getGL2();
        m_bsipic=new BsipcUtils(gl);
        GLU glu=new GLU();

        gl.glViewport(0,0,1000,1000);			// 设置OpenGL视口大小。
        gl.glMatrixMode(GL2.GL_PROJECTION);			// 设置当前矩阵为投影矩阵。
        gl.glLoadIdentity();						// 重置当前指定的矩阵为单位矩阵
        glu.gluPerspective							// 设置透视图
                ( 54.0f,							// 透视角设置为 45 度
                        (float)600/(float)480,	// 窗口的宽与高比
                        0.1f,    				// 视野透视深度:近点1.0f
                        3000.0f					// 视野透视深度:始点0.1f远点1000.0f
                );
        // 这和照象机很类似，第一个参数设置镜头广角度，第二个参数是长宽比，后面是远近剪切。
        gl.glMatrixMode(GL2.GL_MODELVIEW);				// 设置当前矩阵为模型视图矩阵
        gl.glLoadIdentity();

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl=glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.6f, 1.0f);			 // 设置刷新背景色
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);// 刷新背景
        gl.glLoadIdentity();

        GLUT glut=new GLUT();
        gl.glPushMatrix();
        gl.glPointSize(4);										//点的大小
        gl.glTranslatef (-5.0f, 4.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f);	//整体旋转
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        m_bsipic.point();		//画点
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef ( 0.0f, 4.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        m_bsipic.line();		//画线
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef ( 5.0f, 4.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        m_bsipic.Triangle();	//画面
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef (-5.0f, 0.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        m_bsipic.square();		//画正方面
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef ( 0.0f, 0.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        m_bsipic.esquare();		//画正方体
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef ( 5.0f, 0.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(1.0f, 0.0f, 1.0f);
        m_bsipic.park();		//画园
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef (-5.0f,-4.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        m_bsipic.pillar();		//园柱
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef ( 0.0f, -4.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(0.7f, 0.7f, 0.7f);
        glut.glutSolidCone(1.5, 3, 3, 20);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef ( 5.0f,-4.0f,-13.0f);
        gl.glRotatef(r,1.0f,1.0f,1.0f); //整体旋转
        gl.glColor3f(0.4f, 0.4f, 0.4f);
        glut.glutWireTeapot(1);
        gl.glPopMatrix();

        gl.glFlush();										 // 更新窗口
        // 切换缓冲区
        r+=1;if(r>360) r=0;// 重置当前的模型观察矩阵

    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void run() {
        this.animator.start();
    }

    public static void main(String[] args){
        GLRender glRender = new GLRender();
    }

}
