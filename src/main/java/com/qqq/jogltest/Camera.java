package com.qqq.jogltest;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Johnson
 * 2020/12/24
 */
public class Camera {

    enum Camera_Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    };

    public Vector3f position;
    public Vector3f front;
    public Vector3f up;
    public Vector3f right;
    public Vector3f worldUp;

    public float yaw = -90.0f;
    public float pitch = 0.0f;
    public float movementSpeed = 3.0f;
    public float mouseSensitivity = 0.25f;
    public float zoom = 45.0f;


    public Camera(float posX,float posY,float posZ){
        this.position = new Vector3f(posX,posY,posZ);
        this.front = new Vector3f(0.0f,0.0f,-1.0f);
        this.worldUp = new Vector3f(0.0f,1.0f,0.0f);
        this.updateCameraVectors();
    }

    public Camera(float posX,float posY,float posZ,float upX,float upY,float upZ,float yaw,float pitch) {
        this.position = new Vector3f(posX,posY,posZ);
        this.worldUp = new Vector3f(upX,upY,upZ);
        this.front = new Vector3f(0.0f,0.0f,-1.0f);
        this.updateCameraVectors();
    }

    public Camera(Vector3f position, Vector3f front, Vector3f up, Vector3f right, Vector3f worldUp, float yaw, float pitch, float movementSpeed, float mouseSensitivity, float zoom) {
        this.position = position;
        this.front = front;
        this.up = up;
        this.right = right;
        this.worldUp = worldUp;
        this.yaw = yaw;
        this.pitch = pitch;
        this.movementSpeed = movementSpeed;
        this.mouseSensitivity = mouseSensitivity;
        this.zoom = zoom;
        this.updateCameraVectors();
    }

    Matrix4f getViewMatrix(Matrix4f matrix4f){
        Vector3f center = new Vector3f(this.position.x + this.front.x, this.position.y + this.front.y, this.position.z + this.front.z);
        return matrix4f.lookAt(this.position,center,this.up);
    }

    void ProcessKeyboard(Camera_Movement direction, float deltaTime)
    {
        float velocity = this.movementSpeed * deltaTime;
        if (direction == Camera_Movement.FORWARD){
            this.position =new Vector3f(this.position.x+this.front.x*velocity,this.position.y+this.front.y*velocity,this.position.z+this.front.z*velocity);
        }
        if (direction == Camera_Movement.BACKWARD){
            this.position =new Vector3f(this.position.x-this.front.x*velocity,this.position.y-this.front.y*velocity,this.position.z-this.front.z*velocity);
        }
        if (direction == Camera_Movement.LEFT){
            this.position = new Vector3f(this.position.x-this.right.x*velocity,this.position.y-this.right.y*velocity,this.position.z-this.right.z*velocity);
        }
        if (direction == Camera_Movement.RIGHT){
            this.position = new Vector3f(this.position.x+this.right.x*velocity,this.position.y+this.right.y*velocity,this.position.z+this.right.z*velocity);
        }
    }

    void processMouseMovement(float xoffset, float yoffset){
        this.processMouseMovement(xoffset,yoffset,true);
    }

    void processMouseMovement(float xoffset, float yoffset, boolean constrainPitch) {
        xoffset *= this.mouseSensitivity;
        yoffset *= this.mouseSensitivity;

        this.yaw += xoffset;
        this.pitch += yoffset;

        // Make sure that when pitch is out of bounds, screen doesn't get flipped
        if (constrainPitch)
        {
            if (this.pitch > 89.0f){
                this.pitch = 89.0f;
            }
            if (this.pitch < -89.0f){
                this.pitch = -89.0f;
            }
        }

        // Update Front, Right and Up Vectors using the updated Eular angles
        //this.updateCameraVectors();
    }

    // Processes input received from a mouse scroll-wheel event. Only requires input on the vertical wheel-axis
    void ProcessMouseScroll(float yoffset)
    {
        if (this.zoom >= 1.0f && this.zoom <= 45.0f){
            this.zoom -= yoffset;
        }
        if (this.zoom <= 1.0f){
            this.zoom = 1.0f;
        }
        if (this.zoom >= 45.0f){
            this.zoom = 45.0f;
        }
    }

    private void updateCameraVectors()
    {
        // Calculate the new Front vector
        Vector3f front = new Vector3f((float)Math.cos(Math.toRadians(this.yaw) * Math.cos(Math.toRadians(this.pitch))),
                (float)Math.sin(Math.toRadians(this.pitch)),
                (float)Math.sin(Math.toRadians(this.yaw) * Math.cos(Math.toRadians(this.pitch))));
        this.front.normalize(front);
        // Also re-calculate the Right and Up vector
        this.right = new Vector3f().cross(this.front, this.worldUp).normalize();  // Normalize the vectors, because their length gets closer to 0 the more you look up or down which results in slower movement.
        this.up = new Vector3f().cross(this.right, this.front).normalize();
    }
}
