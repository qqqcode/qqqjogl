package com.qqq.jogltest;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Johnson
 * 2020/12/17
 */
public class PointsLight  extends JFrame implements GLEventListener,Runnable {
    GL2 gl;
    GLCanvas glCanvas;
    GLCapabilities glCapabilities;
    GLU glu;

    FPSAnimator animator;

    Thread myThread = new Thread(this);

    public PointsLight() throws HeadlessException {
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
        myThread.start();
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glViewport(0, 0, 500, 300);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0, 500.0, 0.0, 300.0);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        float red = (float) ((Math.random())*(1.0f));            //随机红
        float green = (float) ((Math.random())*(1.0f));          //随机绿
        float blue = (float) ((Math.random())*(1.0f));           //随机蓝

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glPointSize(5.0f);
        for (int i=0; i < 50; i++) {
            red -= .09f;
            green -= .12f;
            blue -= .15f;
            if (red < 0.15) red = 1.0f;
            if (green < 0.15) green = 1.0f;
            if (blue < 0.15) blue = 1.0f;
            gl.glColor3f(red, green, blue);
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex2i((i*10),150);
            gl.glEnd();
        }
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {

    }

    public void run() {
        this.animator.start();
    }
}
