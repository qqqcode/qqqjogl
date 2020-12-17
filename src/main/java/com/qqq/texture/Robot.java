package com.qqq.texture;

import com.jogamp.opengl.GL2;

public class Robot {
    private GL2 gl;
    private String[] legStates = new String[2];
    private String[] armStates = new String[2];
    private float[] legAngles = new float[2];
    private float[] armAngles = new float[2];
    final int LEFT = 0;
    final int RIGHT = 1;

    public Robot(GL2 gl) {
        this.gl = gl;
        armAngles[LEFT] = 0.0f;
        armAngles[RIGHT] = 0.0f;
        legAngles[LEFT] = 0.0f;
        legAngles[RIGHT] = 0.0f;

        armStates[LEFT] = "FORWARD_STATE";
        armStates[RIGHT] = "BACKWARD_STATE";

        legStates[LEFT] = "FORWARD_STATE";
        legStates[RIGHT] = "BACKWARD_STATE";
    }

    private void DrawCube(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        gl.glTranslatef(xPos, yPos, zPos);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);	// top face
        gl.glVertex3f(0.0f, 0.0f, -1.0f);
        gl.glVertex3f(-1.0f, 0.0f, -1.0f);
        gl.glVertex3f(-1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);	// front face
        gl.glVertex3f(-1.0f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glVertex3f(0.0f, -1.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);	// right face
        gl.glVertex3f(0.0f, -1.0f, 0.0f);
        gl.glVertex3f(0.0f, -1.0f, -1.0f);
        gl.glVertex3f(0.0f, 0.0f, -1.0f);
        gl.glVertex3f(-1.0f, 0.0f, 0.0f);	// left face
        gl.glVertex3f(-1.0f, 0.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);	// bottom face
        gl.glVertex3f(0.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);	// back face
        gl.glVertex3f(-1.0f, 0.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(0.0f, -1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void DrawArm(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.0f, 0.0f);	// red
        gl.glTranslatef(xPos, yPos, zPos);
        gl.glScalef(1.0f, 4.0f, 1.0f);		// arm is a 1x4x1 cube
        DrawCube(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();
    }

    private void DrawHead(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        // white
        gl.glTranslatef(xPos, yPos, zPos);
        gl.glScalef(2.0f, 2.0f, 2.0f);		// head is a 2x2x2 cube
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        DrawCube(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();
    }

    private void DrawTorso(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 1.0f);	// blue
        gl.glTranslatef(xPos, yPos, zPos);
        gl.glScalef(3.0f, 5.0f, 2.0f);		// torso is a 3x5x2 cube
        DrawCube(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();
    }

    private void DrawLeg(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        gl.glTranslatef(xPos, yPos, zPos);

        // draw the foot
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, 0.0f);
        DrawFoot(0.0f, -5.0f, 0.0f);
        gl.glPopMatrix();

        gl.glScalef(1.0f, 5.0f, 1.0f);		// leg is a 1x5x1 cube
        gl.glColor3f(1.0f, 1.0f, 0.0f);	// yellow
        DrawCube(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();
    }

    private void DrawFoot(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glTranslatef(xPos, yPos, zPos);
        gl.glScalef(1.0f, 0.5f, 3.0f);
        DrawCube(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();
    }

    public void DrawRobot(float xPos, float yPos, float zPos) {
        gl.glPushMatrix();
        gl.glTranslatef(xPos, yPos, zPos);	// draw robot at desired coordinates

        //画头 draw head and torso parts
        DrawHead(1.0f, 2.0f, 0.0f);
        DrawTorso(1.5f, 0.0f, 0.0f);

        //画左手 move the left arm away from the torso and rotate it to give "walking" effect
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, 0.0f);
        gl.glRotatef(armAngles[LEFT], 1.0f, 0.0f, 0.0f);
        DrawArm(2.5f, 0.0f, -0.5f);
        gl.glPopMatrix();

        //画右手 move the right arm away from the torso and rotate it to give "walking" effect
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, 0.0f);
        gl.glRotatef(armAngles[RIGHT], 1.0f, 0.0f, 0.0f);
        DrawArm(-1.5f, 0.0f, -0.5f);
        gl.glPopMatrix();

        //画左脚 move the left leg away from the torso and rotate it to give "walking" effect
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, 0.0f);
        gl.glRotatef(legAngles[LEFT], 1.0f, 0.0f, 0.0f);
        DrawLeg(-0.5f, -5.0f, -0.5f);
        gl.glPopMatrix();

        //画右脚 move the right leg away from the torso and rotate it to give "walking" effect
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, 0.0f);
        gl.glRotatef(legAngles[RIGHT], 1.0f, 0.0f, 0.0f);
        DrawLeg(1.5f, -5.0f, -0.5f);
        gl.glPopMatrix();

        gl.glPopMatrix();	// pop back to original coordinate system
    }

    public void walk(float speed){

    }

    public void Prepare(float dt) {
        //机器人动作,实际就是按规则旋转手脚
        // if leg is moving forward, increase angle, else decrease angle
        for (char side = 0; side < 2; side++) {
            // arms
            if (armStates[side] == "FORWARD_STATE") {
                armAngles[side] += 20.0f * dt;
            } else {
                armAngles[side] -= 20.0f * dt;
            }

            // change state if exceeding angles
            if (armAngles[side] >= 15.0f) {
                armStates[side] = "BACKWARD_STATE";
            } else if (armAngles[side] <= -15.0f) {
                armStates[side] = "FORWARD_STATE";
            }

            // legs
            if (legStates[side] == "FORWARD_STATE") {
                legAngles[side] += 20.0f * dt;
            } else {
                legAngles[side] -= 20.0f * dt;
            }

            // change state if exceeding angles
            if (legAngles[side] >= 15.0f) {
                legStates[side] = "BACKWARD_STATE";
            } else if (legAngles[side] <= -15.0f) {
                legStates[side] = "FORWARD_STATE";
            }
        }
    }

}
