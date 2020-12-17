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
public class LeptospiraGLEventListener extends JFrame implements GLEventListener,Runnable {

    GL2 gl;
    GLCanvas glCanvas;
    GLCapabilities glCapabilities;
    GLU glu;

    FPSAnimator animator;

    double linelong = 0;
    double width = 2;
    double height = 2;

    Thread myThread = new Thread(this);

    public LeptospiraGLEventListener() throws HeadlessException {
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
        this.gl = glAutoDrawable.getGL().getGL2();
        this.glu = new GLU();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glViewport(-250, -150, 250, 150);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-250.0, 250.0, -150.0, 150.0);
    }

    public void dispose(GLAutoDrawable glAutoDrawable){

    }

    public void display(GLAutoDrawable glAutoDrawable){
        double x, y;
        float red = 1.0f;
        float green = 0.0f;
        float blue = 1.0f;
        gl = glAutoDrawable.getGL().getGL2();
        if (linelong == 59 || linelong == 58) {
            gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        } else if (linelong == 57 || linelong == 56) {
            gl.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
        } else if (linelong == 55 || linelong == 54) {
            gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        } else if (linelong == 60) {
            gl.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
        } else if (linelong == 00) {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(red, green, blue);
        if (linelong < 60) {
            linelong += 1;
        }
        gl.glBegin(GL.GL_LINE_STRIP);

        if (linelong < 60) {
            for (double i = 0; i < linelong; i += 0.1) {
                x = Math.sin(i) * i * width;
                y = Math.cos(i) * i * height;
                gl.glVertex2d(x, y);
            }
        } else if (linelong == 60 && width < 15 && height < 15) {
            width += 0.1;
            height += 0.1;
            for (double i = 0; i < linelong; i += 0.1) {
                x = Math.sin(i) * i * width;
                y = Math.cos(i) * i * height;
                gl.glVertex2d(x, y);
            }
        } else if (width >= 15 && height >= 15) {
            linelong = 0;
            width = 2;
            height = 2;
            for (double i = 0; i < linelong; i += 0.1) {
                x = Math.sin(i) * i * width;
                y = Math.cos(i) * i * height;
                gl.glVertex2d(x, y);
            }
        }
        gl.glEnd();
        gl.glFlush();
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void run() {

    }
}
