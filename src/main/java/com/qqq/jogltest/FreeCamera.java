package com.qqq.jogltest;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FreeCamera {
    public Vector3f linearAcc = new Vector3f();
    public Vector3f linearVel = new Vector3f();

    /** Always rotation about the local XYZ axes of the camera! */
    public Vector3f angularAcc = new Vector3f();
    public Vector3f angularVel = new Vector3f();

    public Vector3f position = new Vector3f(0, 0, 10);
    public Quaternionf rotation = new Quaternionf();

    /**
     * Update this {@link FreeCamera} based on the given elapsed time.
     *
     * @param dt
     *            the elapsed time
     * @return this
     */
    public FreeCamera update(float dt) {
        // update linear velocity based on linear acceleration
        linearVel.fma(dt, linearAcc);
        // update angular velocity based on angular acceleration
        angularVel.fma(dt, angularAcc);
        // update the rotation based on the angular velocity
        rotation.integrate(dt, angularVel.x, angularVel.y, angularVel.z);
        // update position based on linear velocity
        position.fma(dt, linearVel);
        return this;
    }

    /**
     * Compute the world-space 'right' vector and store it into <code>dest</code>.
     *
     * @param dest
     *          will hold the result
     * @return dest
     */
    public Vector3f right(Vector3f dest) {
        return rotation.positiveX(dest);
    }

    /**
     * Compute the world-space 'up' vector and store it into <code>dest</code>.
     *
     * @param dest
     *          will hold the result
     * @return dest
     */
    public Vector3f up(Vector3f dest) {
        return rotation.positiveY(dest);
    }

    /**
     * Compute the world-space 'forward' vector and store it into <code>dest</code>.
     *
     * @param dest
     *          will hold the result
     * @return dest
     */
    public Vector3f forward(Vector3f dest) {
        return rotation.positiveZ(dest).negate();
    }

    /**
     * Apply the camera/view transformation of this {@link FreeCamera} to the given matrix.
     *
     * @param m
     *            the matrix to apply the view transformation to
     * @return m
     */
    public Matrix4f apply(Matrix4f m) {
        return m.rotate(rotation).translate(-position.x, -position.y, -position.z);
    }
}
