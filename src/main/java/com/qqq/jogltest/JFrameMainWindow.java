package com.qqq.jogltest;


import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JFrameMainWindow extends JFrame {
    VerTextShaderTest listener=new VerTextShaderTest();
    private static FPSAnimator animator=null;
    public JFrameMainWindow() throws HeadlessException {
        super("qqq");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GLCapabilities glcaps=new GLCapabilities(GLProfile.get(GLProfile.GL2));
        GLCanvas canvas=new GLCanvas(glcaps);
        canvas.addGLEventListener(listener);
        //canvas.addMouseListener(listener);
        this.addKeyListener(new keyEventListener());
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

    class keyEventListener implements KeyListener {

        public void keyTyped(KeyEvent e) {

        }

        public void keyPressed(KeyEvent e) {
            //处理键盘事件
            System.out.println(e.getKeyCode());
//            if (e.getKeyCode() == 37) {
//                listener.xSpeed = -1.0f;
//            }
//
//            if (e.getKeyCode() == 38) {
//                listener.zSpeed = -1.0f;
//            }
//
//            if (e.getKeyCode() == 39) {
//                listener.xSpeed = 1.0f;
//            }
//
//            if (e.getKeyCode() == 40) {
//                listener.zSpeed = 1.0f;
//            }
        }

        public void keyReleased(KeyEvent e) {
//            listener.zSpeed = 0.0f;
//            listener.xSpeed = 0.0f;
        }
    }

}
