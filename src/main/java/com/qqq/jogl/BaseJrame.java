package com.qqq.jogl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

public class BaseJrame implements GLEventListener {
    private float rtri;  //for angle of rotation
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear (GL2.GL_COLOR_BUFFER_BIT |  GL2.GL_DEPTH_BUFFER_BIT );
        // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();  // Reset The View
        //triangle rotation
        gl.glRotatef( rtri, 0.0f, 1.0f, 0.0f );
//        float sin = (float) (Math.sin(rtri*0.1));
//        gl.glScalef(sin,sin,sin);

        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glColor3f( 1.0f,0.0f,0.0f);   // Red
        gl.glVertex3f(-0.5f,-0.5f,0.0f);
        gl.glColor3f( 0.0f,1.0f,0.0f);     // green
        gl.glVertex3f(0.5f,-0.5f,0.0f);
        gl.glColor3f( 0.0f,0.0f,1.0f);     // blue
        gl.glVertex3f(0.0f,0.5f,0.0f);
        gl.glEnd();

        gl.glFlush();
        rtri += 0.2f;  //assigning the angle

        gl.glEnable( GL2.GL_LIGHTING );
        gl.glEnable( GL2.GL_LIGHT0 );
        gl.glEnable( GL2.GL_NORMALIZE );
        // weak RED ambient
        float[] ambientLight = { 0.1f, 0.f, 0.f,0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        // multicolor diffuse
        float[] diffuseLight = { 1f,2f,1f,0f };
        gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0 );
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    public static void main(String[] args){
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(profile);
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);

        BaseJrame jrame = new BaseJrame();

        glCanvas.addGLEventListener(jrame);
        glCanvas.setSize(600,600);

        final JFrame jFrame  = new JFrame("JFrame");
        jFrame.getContentPane().add(glCanvas);
        jFrame.setSize(jFrame.getContentPane().getPreferredSize());
        jFrame.setVisible(true);

        final FPSAnimator animator = new FPSAnimator(glCanvas, 300,true);
        animator.start();
    }
}
