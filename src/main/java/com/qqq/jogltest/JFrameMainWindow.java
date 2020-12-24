package com.qqq.jogltest;


import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JFrameMainWindow extends JFrame {
    CameraSystem listener=new CameraSystem();
    private static FPSAnimator animator = null;
    public JFrameMainWindow() throws HeadlessException {
        super("qqq");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GLCapabilities glcaps = new GLCapabilities(GLProfile.getGL2GL3());
        GLCanvas canvas = new GLCanvas(glcaps);
        canvas.addGLEventListener(listener);
        //this.addMouseListener();
        this.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                System.out.println("keyTyped"+e.getKeyCode());
            }

            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed"+e.getKeyCode());
                if(e.getKeyCode()==87){
                    listener.xkey+=5.0f;
                }
            }

            public void keyReleased(KeyEvent e) {
                System.out.println("keyReleased"+e.getKeyCode());
                if(e.getKeyCode()==87){
                    listener.xkey=0.0f;
                }
            }
        });
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouseClicked");
            }

            public void mousePressed(MouseEvent e) {
                System.out.println("mousePressed");
            }

            public void mouseReleased(MouseEvent e) {
                System.out.println("mouseReleased");
            }

            public void mouseEntered(MouseEvent e) {
                System.out.println("mouseEntered");
            }

            public void mouseExited(MouseEvent e) {
                System.out.println("mouseExited");
            }
        });
        getContentPane().add(canvas, BorderLayout.CENTER);
        animator=new FPSAnimator(canvas,60,true);

        centerWindow(this);
    }
    private void centerWindow(Component frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        frame.setLocation((screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1);

    }

    public static void main(String[] args) {
        final JFrameMainWindow app = new JFrameMainWindow();
        // 显示窗体
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.setVisible(true);
            }
        });
        // 动画线程开始
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                animator.start();
            }
        });
    }

}
