package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private ArrayList<Triangle> trigs = new ArrayList<Triangle>();

    private static final String TAG = "MyGLRenderer";
    private Triangle mTriangle;
    private Square   mSquare;

    private final float[] mMVPMatrix        = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix       = new float[16];
    private final float[] mRotationMatrix   = new float[16];
    private final float[] mModelMatrix      = new float[16];
    private       float[] mTempMatrix       = new float[16];

    private float mAngle;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle = new Triangle();
        mSquare = new Square();

        for (int i = 0; i < 20; i++) {
            trigs.add(new Triangle());
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

//        mSquare.draw(mMVPMatrix);
//
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, -0.5f, 0, 0);
//
//        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);
//
//        mTempMatrix = mModelMatrix.clone();
//        Matrix.multiplyMM(mModelMatrix, 0, mTempMatrix, 0, mRotationMatrix, 0);
//        mTempMatrix = mMVPMatrix.clone();
//        Matrix.multiplyMM(mMVPMatrix,   0, mTempMatrix, 0, mModelMatrix,    0);
//
//        mTriangle.draw(mMVPMatrix);

        for (Triangle t : trigs) {
            t.tick();

            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, t.getX(), t.getY(), 0);

            Matrix.setRotateM(mRotationMatrix, 0, 0, 0, 0, 1.0f);

            mTempMatrix = mModelMatrix.clone();
            Matrix.multiplyMM(mModelMatrix, 0, mTempMatrix, 0, mRotationMatrix, 0);

            mTempMatrix = mMVPMatrix.clone();
            Matrix.multiplyMM(mMVPMatrix, 0, mTempMatrix, 0, mModelMatrix, 0);

            t.draw(mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }
}