package com.qqq.jogltest;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Renderer extends JFrame implements GLEventListener,Runnable {
//    GL2 gl;
    GLCanvas glCanvas;
    GLCapabilities glCapabilities;
    GLU glu;

    private float rquad = 0.0f;
    private float rtri = 0.0f;

    FPSAnimator animator;
    Thread myThread = new Thread(this);

    public Renderer() throws HeadlessException {
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
        this.setSize(500,400);
        myThread.start();
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);              // 开启平滑画图
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // 背景颜色
        gl.glClearDepth(1.0f);                      // 深度缓冲
        gl.glEnable(GL2.GL_DEPTH_TEST);       //开启深度测试
        gl.glDepthFunc(GL2.GL_LEQUAL);        // 深度测试的类型
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        // GL_PERSPECTIVE_CORRECTION_HINT 指定颜色和纹理坐标的插值质量.
        // GL_NICEST为使用质量最好的模式.
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        double x,y;
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(-1.5f, 0.0f, -6.0f);    //移动画图点到指定坐标X，Y，Z
        gl.glRotatef(rtri, 0.0f, 1.0f, 0.0f);
        gl.glBegin(GL.GL_TRIANGLES);      //画三角形
        gl.glColor3f(1.0f, 0.0f, 0.0f);   //设置当前画刷颜色红
        gl.glVertex3f(0.0f, 1.0f, 0.0f); // 上
        gl.glColor3f(0.0f, 1.0f, 0.0f);   //设置当前画刷颜色绿
        gl.glVertex3f(-1.0f, -1.0f, 0.0f); // 下左
        gl.glColor3f(0.0f, 0.0f, 1.0f);   //设置当前画刷颜色蓝
        gl.glVertex3f(1.0f, -1.0f, 0.0f); // 下右
        gl.glEnd();
//        gl.glLoadIdentity();
//        gl.glTranslatef(1.5f, 0.0f, -6.0f);   //移动画图点到指定坐标X，Y，Z
//        gl.glRotatef(rquad, 1.0f, 0.0f, 0.0f);
//        gl.glBegin(GL2.GL_QUADS);            // Draw A Quad 画正方形  原NEHE JOGL的内容，修改了。
//        gl.glColor3f(0.5f, 0.5f, 1.0f);   // 设置为光绿色
//        gl.glVertex3f(-1.0f, 1.0f, 0.0f); // 左上顶点
//        gl.glVertex3f(1.0f, 1.0f, 0.0f); // 右上顶点
//        gl.glVertex3f(1.0f, -1.0f, 0.0f); // 右下顶点
//        gl.glVertex3f(-1.0f, -1.0f, 0.0f); //左下顶点
//        gl.glEnd();    // glVertex3f必须被包含在glBegin（）glEnd()里

        gl.glLineWidth(5.0f);       //设置线宽为5
        gl.glBegin(GL.GL_LINE_STRIP);
        for (double i = 0; i < 60; i += 0.1) {                    //这个for语句里画螺旋，由glBegin开始，glEnd
            float red = (float) ((Math.random())*(1.0f));            //随机红
            float green = (float) ((Math.random())*(1.0f));          //随机绿
            float blue = (float) ((Math.random())*(1.0f));          //随机蓝
            gl.glColor3f(red, green, blue);                                   //设置画图颜色，第二章的内容
            x = Math.sin(i) * i /10 ;
            y = Math.cos(i) * i/10 ;
            gl.glVertex2d(x, y);

        }
        gl.glEnd();
        gl.glFlush();
        rtri += 0.2f;
        rquad += 0.15f;
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        final GLU glu = new GLU();

        if (height <= 0)
            height = 1;
        final float h = (float) width / (float) height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 30.0); //设置摄像机可视范围的 近上下宽度，远上下宽度，近可视点，远可视点
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void run() {
        this.animator.start();
    }

    public static void main(String[] args){
        Renderer renderer = new Renderer();
    }
}
