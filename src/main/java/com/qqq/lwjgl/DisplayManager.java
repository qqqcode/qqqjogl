package com.qqq.lwjgl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author Johnson
 * 2020/12/17
 */
public class DisplayManager {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS_CAP = 120;

    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3, 2);
        attribs.withProfileCompatibility(true);
        attribs.withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("MyGameEngine");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }


    public static void updateDisplay() {
        Display.sync(FPS_CAP);  // 帧率同步最大值：120
        Display.update();
    }


    public static void closeDisplay() {
        Display.destroy();
    }
}
