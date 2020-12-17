package com.qqq.jogl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Johnson
 * 2020/12/15
 */
public class BaseFrame implements GLEventListener {


    public void init(GLAutoDrawable drawable) {

    }

    public void dispose(GLAutoDrawable drawable) {

    }

    public void display(GLAutoDrawable drawable) {
        // TODO Auto-generated method stu
        final GL2 gl = drawable.getGL().getGL2();
        //gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glColor3f( 1.0f,0.0f,0.0f);   // Red
        gl.glVertex3f(-0.5f,-0.5f,0.0f);
        gl.glColor3f( 0.0f,1.0f,0.0f);     // green
        gl.glVertex3f(0.5f,-0.5f,0.0f);
        gl.glColor3f( 0.0f,0.0f,1.0f);     // blue
        gl.glVertex3f(0.0f,0.5f,0.0f);
        gl.glEnd();
    }

    public void reshape(GLAutoDrawable drawable, int x,  int y, int width, int height) {
    }
    public static void main(String[] args) {
        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        BaseFrame l = new BaseFrame();
        glcanvas.addGLEventListener(l);
        glcanvas.setSize(400, 400);
        //creating frame
        final JFrame frame = new JFrame ("straight Line");
        //adding canvas to frame
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        final FPSAnimator animator = new FPSAnimator(glcanvas, 300,true );
        animator.start();

        System.out.println("qqqtest");
    }
}
