package com.qqq.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Scanner;
import java.util.Vector;

import static com.jogamp.opengl.GL.GL_NO_ERROR;
import static com.jogamp.opengl.GL2ES2.*;

/**
 * @author Johnson
 * 2020/12/25
 */
public class Code extends JFrame implements GLEventListener {

    private GLCanvas myCanvas;
    private int rendering_program;
    private int vao[ ] = new int[1];
    private int vbo[ ] = new int[2];
    private float cameraX, cameraY, cameraZ;
    private float cubeLocX, cubeLocY, cubeLocZ;
    //private GLSLUtils util = new GLSLUtils();
    private Matrix4f pMat;

    public static void main(String[ ] args) {
        new Code();
    }

    public Code(){
        setTitle("Chapter2 - program1");
        setSize(600, 400);
        setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        this.add(myCanvas);
        setVisible(true);
        FPSAnimator animtr = new FPSAnimator(myCanvas, 50);
        animtr.start();
    }


    public void init(GLAutoDrawable glAutoDrawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        rendering_program = createShaderProgram();
        setupVertices();
        cameraX=0.0f; cameraY=0.0f; cameraZ=6.0f;
        cubeLocX=0.0f; cubeLocY=4.0f; cubeLocZ=0.0f; // shifted down along the Y-axis to reveal perspective

        float aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat=new Matrix4f().perspective(60.0f, aspect, 0.1f, 1000.0f);

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        float bkg[] = {0.0f,0.0f,0.0f,1.0f};
        FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
        gl.glClearBufferfv(GL4.GL_COLOR,0,bkgBuffer);
        gl.glUseProgram(rendering_program);

        double t = System.currentTimeMillis() / 10000.0;

        System.out.println(t);

        Matrix4f mvMat = new Matrix4f().translate(-cameraX,-cameraY,-cameraZ).translate((float)(Math.sin(2*t)*2.0), (float)(Math.sin(3*t)*2.0), (float)(Math.sin(4*t)*2.0));

        int mv_loc = gl.glGetUniformLocation(rendering_program, "mv_matrix");
        int proj_loc = gl.glGetUniformLocation(rendering_program, "proj_matrix");
        gl.glUniformMatrix4fv(proj_loc, 1, false, pMat.get(Buffers.newDirectFloatBuffer(16)));
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvMat.get(Buffers.newDirectFloatBuffer(16)));
        // associate VBO with the corresponding vertex attribute in the vertex shader
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        // adjust OpenGL settings and draw model
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glDrawArrays(GL_TRIANGLES, 0, 36);

    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    private int createShaderProgram() {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        int[ ] vertCompiled = new int[1];
        int[ ] fragCompiled = new int[1];
        int[ ] linked = new int[1];

        String vshaderSource[] = readShaderSource(this.getClass().getResource("/baseShader.vs").getPath());
        String fshaderSource[] = readShaderSource(this.getClass().getResource("/baseShader.frag").getPath());

        int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0); // note: 3 lines of
        //code
        gl.glCompileShader(vShader);
        checkOpenGLError(); // can use returned boolean
        gl.glGetShaderiv(vShader, GL_COMPILE_STATUS, vertCompiled, 0);
        if (vertCompiled[0] == 1) {
            System.out.println(". . . vertex compilation success.");
        } else {
            System.out.println(". . . vertex compilation failed.");
            printShaderLog(vShader);
        }


        int fShader=gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0); // note: 4 lines of
        gl.glCompileShader(fShader);
        checkOpenGLError(); // can use returned boolean
        gl.glGetShaderiv(fShader, GL_COMPILE_STATUS, fragCompiled, 0);
        if (fragCompiled[0] == 1) {
            System.out.println(". . . fragment compilation success.");
        } else {
            System.out.println(". . . fragment compilation failed.");
            printShaderLog(fShader);
        }

        if ((vertCompiled[0] != 1) || (fragCompiled[0] != 1)) {
            System.out.println("\nCompilation error; return-flags:");
            System.out.println(" vertCompiled = " + vertCompiled[0] + "fragCompiled = " + fragCompiled[0]);
        } else {
            System.out.println("Successful compilation");
        }
        // catch errors while linking shaders
        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
        gl.glLinkProgram(vfprogram);
        checkOpenGLError();
        gl.glGetProgramiv(vfprogram, GL_LINK_STATUS, linked,0);
        if (linked[0] == 1) {
            System.out.println(". . . linking succeeded.");
        } else {
            System.out.println(". . . linking failed.");
            printProgramLog(vfprogram);
        }

        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);
        return vfprogram;
    }

    private String[] readShaderSource(String filename) {
        Vector<String> lines = new Vector<String>();
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (IOException e) {
            System.err.println("IOException reading file: " + e);
            return null;
        }
        while (sc.hasNext()) {
            lines.addElement(sc.nextLine());
        }
        String[ ] program = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            program[i] = (String) lines.elementAt(i) + "\n";
        }
        return program;
    }

    private void printShaderLog(int shader) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[ ] len = new int[1];
        int[ ] chWrittn = new int[1];
        byte[ ] log = null;
        // determine the length of the shader compilation log
        gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        { log = new byte[len[0]];
            gl.glGetShaderInfoLog(shader, len[0], chWrittn, 0, log, 0);
            System.out.println("Shader Info Log: ");
            for (int i = 0; i < log.length; i++)
            { System.out.print((char) log[i]);
            }
        }
    }

    void printProgramLog(int prog) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[ ] len = new int[1];
        int[ ] chWrittn = new int[1];
        byte[ ] log = null;
        // determine the length of the program linking log
        gl.glGetProgramiv(prog,GL_INFO_LOG_LENGTH,len, 0);
        if (len[0] > 0) {
            log = new byte[len[0]];
            gl.glGetProgramInfoLog(prog, len[0], chWrittn, 0,log, 0);
            System.out.println("Program Info Log: ");
            for (int i = 0; i < log.length; i++) {
                System.out.print((char) log[i]);
            }
        }
    }

    boolean checkOpenGLError() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        boolean foundError = false;
        GLU glu = new GLU();
        int glErr = gl.glGetError();
        while (glErr != GL_NO_ERROR) {
            System.err.println("glError: " + glu.gluErrorString(glErr));
            foundError = true;
            glErr = gl.glGetError();
        }
        return foundError;
    }

    private void setupVertices() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        // 36 vertices of the 12 triangles making up a 2 x 2 x 2 cube centeredat the origin
        float[ ] vertex_positions =
                { -1.0f, 1.0f, -1.0f,   -1.0f, -1.0f, -1.0f,  1.0f, -1.0f, -1.0f,
                   1.0f,-1.0f, -1.0f,   1.0f, 1.0f, -1.0f,    -1.0f, 1.0f, -1.0f,
                   1.0f, -1.0f, -1.0f,  1.0f, -1.0f, 1.0f,    1.0f, 1.0f, -1.0f,
                   1.0f, -1.0f, 1.0f,   1.0f, 1.0f, 1.0f,     1.0f, 1.0f, -1.0f,
                   1.0f, -1.0f, 1.0f,   -1.0f, -1.0f, 1.0f,   1.0f, 1.0f, 1.0f,
                   -1.0f,-1.0f, 1.0f,   -1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 1.0f,
                   -1.0f, -1.0f, 1.0f,  -1.0f, -1.0f, -1.0f,  -1.0f, 1.0f, 1.0f,
                   -1.0f,-1.0f, -1.0f,  -1.0f, 1.0f, -1.0f,   -1.0f, 1.0f, 1.0f,
                   -1.0f, -1.0f, 1.0f,  1.0f, -1.0f, 1.0f,    1.0f, -1.0f, -1.0f,
                   1.0f,-1.0f, -1.0f,   -1.0f, -1.0f, -1.0f,  -1.0f, -1.0f, 1.0f,
                   -1.0f, 1.0f, -1.0f,  1.0f, 1.0f, -1.0f,    1.0f, 1.0f, 1.0f,
                   1.0f, 1.0f,1.0f,     -1.0f, 1.0f, 1.0f,    -1.0f, 1.0f, -1.0f
                };
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(vbo.length, vbo, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer vertBuf = Buffers.newDirectFloatBuffer(vertex_positions);
        gl.glBufferData(GL_ARRAY_BUFFER, vertBuf.limit()*4, vertBuf, GL_STATIC_DRAW);
    }
}
