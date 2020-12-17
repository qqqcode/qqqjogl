package com.qqq.utils;

import com.jogamp.opengl.GL2;

public class BsipcUtils {

    GL2 gl;

    public BsipcUtils(GL2 gl) {
        this.gl = gl;
    }

    public void point() {
        gl.glBegin(GL2.GL_POINTS);// 单个顶点
        gl.glVertex3f(0.0f, 1.0f, -1.0f);// a点
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);// b点
        gl.glVertex3f(1.0f, -1.0f, 0.0f);// c点
        gl.glEnd();
    }

    public void line() {
        gl.glBegin(GL2.GL_LINE_LOOP); // 闭合折线
        gl.glVertex3f(0.0f, 1.0f, -1.0f);// a点
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);// b点
        gl.glVertex3f(1.0f, -1.0f, 0.0f);// c点
        gl.glEnd();
    }

    public void Triangle() {
        gl.glBegin(GL2.GL_POLYGON);// 填充凸多边形
        gl.glVertex3f(0.0f, 1.0f, -1.0f);// a点
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);// b点
        gl.glVertex3f(1.0f, -1.0f, 0.0f);// c点
        gl.glEnd();
    }

    public void square() {
        gl.glBegin(GL2.GL_POLYGON);// 填充凸多边形
        gl.glVertex3f(0.0f, 0.0f, 0.0f);// a点
        gl.glVertex3f(1.0f, 0.0f, 0.0f);// b点
        gl.glVertex3f(1.0f, 0.0f, -1.0f);// c点
        gl.glVertex3f(0.0f, 0.0f, -1.0f);// d点
        gl.glEnd();
    }

    public void esquare() {
        gl.glBegin(GL2.GL_QUAD_STRIP);// 填充凸多边形
        gl.glVertex3f(0.0f, 0.0f, 0.0f);// a0点
        gl.glVertex3f(0.0f, 1.0f, 0.0f);// a1点
        gl.glVertex3f(1.0f, 0.0f, 0.0f);// b0点
        gl.glVertex3f(1.0f, 1.0f, 0.0f);// b1点
        gl.glVertex3f(1.0f, 0.0f, -1.0f);// c0点
        gl.glVertex3f(1.0f, 1.0f, -1.0f);// c1点
        gl.glVertex3f(0.0f, 0.0f, -1.0f);// d0点
        gl.glVertex3f(0.0f, 1.0f, -1.0f);// d1点
        gl.glVertex3f(0.0f, 0.0f, 0.0f);// a0点
        gl.glVertex3f(0.0f, 1.0f, 0.0f);// a1点
        gl.glEnd();
        // 现在这个正方体还缺上下两个面。应该补上。
        gl.glBegin(GL2.GL_POLYGON);// 填充凸多边形
        gl.glVertex3f(0.0f, 0.0f, 0.0f);// a0点
        gl.glVertex3f(1.0f, 0.0f, 0.0f);// b0点
        gl.glVertex3f(1.0f, 0.0f, -1.0f);// c0点
        gl.glVertex3f(0.0f, 0.0f, -1.0f);// d0点
        gl.glVertex3f(0.0f, 1.0f, 0.0f);// a1点
        gl.glVertex3f(1.0f, 1.0f, 0.0f);// b1点
        gl.glVertex3f(1.0f, 1.0f, -1.0f);// c1点
        gl.glVertex3f(0.0f, 1.0f, -1.0f);// d1点
        gl.glEnd();
    }

    public void park() {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);// 扇形连续填充三角形串
        gl.glVertex3f(0, 0, 0.0f);
        for (int i = 0; i <= 390; i += 30) {
            float p = (float) (i * 3.14 / 180);
            gl.glVertex3f((float) Math.sin(p), (float) Math.cos(p), 0.0f);// 园轨迹
        }
        gl.glEnd();
    }

    public void pillar() {
        gl.glBegin(GL2.GL_QUAD_STRIP);// 连续填充四边形串
        for (int i = 0; i <= 390; i += 30) {
            float p = (float) (i * 3.14 / 180);
            gl.glVertex3f((float) Math.sin(p) / 2, (float) Math.cos(p) / 2, 1.0f);// 前园
            gl.glVertex3f((float) Math.sin(p) / 2, (float) Math.cos(p) / 2, 0.0f);// 后园
        }
        gl.glEnd();
    }
}