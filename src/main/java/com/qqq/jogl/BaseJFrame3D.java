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

public class BaseJFrame3D implements GLEventListener {

    public static DisplayMode dm, dm_old;
    private GLU glu = new GLU();
    private float xrot,yrot,zrot;
    private int texture;

    public void init(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f,0f,0f,0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT,GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        try{
            File im = new File("D:\\Document\\texture\\mjr01.jpg");
            Texture t = TextureIO.newTexture(im, true);
            texture = t.getTextureObject(gl);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); // Reset The View
        gl.glTranslatef(0f, 0f, -5.0f);
        gl.glRotatef(xrot, 1.0f, 1.0f, 1.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        gl.glBegin(GL2.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        // Back Face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        // Top Face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
        // Bottom Face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
        // Left Face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glFlush();
        //change the speeds here
        xrot += .1f;
        yrot += .1f;
        zrot += .1f;
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        if( height <= 0 )
            height = 1;
        final float h = ( float ) width/( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    public static void main(String[] args){
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(profile);
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);

        BaseJFrame3D jrame = new BaseJFrame3D();

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
