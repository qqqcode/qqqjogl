package com.qqq.utils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import org.joml.Matrix4f;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class JoglUtils {

    public static void clearColorAndDepth(final GL4 gl,float r, float g, float b, float a,float depth){
        gl.glClearColor(r, g, b, a);
        gl.glClearDepthf(depth);
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
    }

    /**
     *
     * @param gl
     * @param i1 清除面（背面或正面）：GL_BACK or GL_FRONT
     * @param i2 指定面 GL_CCW 逆时针绘制为正面， GL_CW顺时针绘制为正面
     */
    public static void enableCullFace(final GL4 gl,int i1,int i2){
        gl.glEnable(GL4.GL_CULL_FACE);//清除背面绘制
        gl.glCullFace(i1);//清除背面
        gl.glFrontFace(i2);//顺时针绘制为正
    }

    /**
     *
     * @param gl
     * @param i1 深度测试函数
     */
    public static void enableDepthtest(final GL4 gl,int i1){
        gl.glEnable(GL4.GL_DEPTH_TEST);//开启深度测试
        gl.glDepthFunc(i1);
    }

    public static int createProgram(GL4 gl,String vsPath,String fragPath) {
        ShaderCode vertexShaderCode = compileShader(gl,vsPath,GL4.GL_VERTEX_SHADER);
        ShaderCode fragmentShaderCode = compileShader(gl,fragPath,GL4.GL_FRAGMENT_SHADER);
        ShaderProgram program = linkShader(gl,vertexShaderCode,fragmentShaderCode);
        return program.program();
    }

    public static ShaderCode compileShader(final GL4 gl,final String source,final int shaderType){
        String vsrc = "";
        try {
            BufferedReader brv = new BufferedReader(new FileReader(source));
            String line;
            while ((line=brv.readLine()) != null) {
                vsrc += line + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] sources = new String[1][1];
        sources[0] = new String[]{vsrc};
        ShaderCode shaderCode = new ShaderCode(shaderType, sources.length, sources);
        boolean compiled = shaderCode.compile(gl, System.err);
        if (!compiled){
            System.err.println("Unable to compile " + vsrc);
            System.exit(1);
        }
        return shaderCode;
    }

    public static ShaderProgram linkShader(final GL4 gl, final ShaderCode vertexShader, final ShaderCode fragmentShader) throws GLException {
        ShaderProgram program = new ShaderProgram();
        program.init(gl);
        program.add(vertexShader);
        program.add(fragmentShader);
        program.link(gl, System.out);
        final boolean validateProgram = program.validateProgram(gl, System.out);
        if (!validateProgram) {
            System.err.println("Unable to link shader");
            System.exit(1);
        }
        return program;
    }

    public static int createVertexArrays(final int[] vao,final GL4 gl,final int[] shaderLocation, float[] values,int[] indices){
        return 1;
    }

    public static int[] createGlTexture(final GL4 gl,String texturePath,int[] texture,int i1,int i2,int i3,int i4){
        gl.glGenTextures(texture.length,texture,0);
        //绑定
        gl.glBindTexture(GL4.GL_TEXTURE_2D,texture[0]);
        //纹理环绕方式
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, i1);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, i2);
        //纹理过滤
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, i3);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, i4);
        //根据路径导出一个纹理
        Texture textureIO = createTexture(texturePath);
        texture[0] = textureIO.getTextureObject(gl);
        //
        gl.glGenerateTextureMipmap(GL4.GL_TEXTURE_2D);
        //解绑
        gl.glBindTexture(GL4.GL_TEXTURE_2D, 0);
        return texture;
    }

    public static Texture createTexture(String texturePath){
        File im = new File(texturePath);
        Texture t = null;
        try {
            t = TextureIO.newTexture(im, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void uniformMatrix4fv(final GL4 gl,final int program,final String name,final Matrix4f matrix4f){
        int location = gl.glGetUniformLocation(program, name);
        gl.glUniformMatrix4fv(location, 1, false,matrix4f.get(Buffers.newDirectFloatBuffer(16)));

    }

    public static void initVBO(GL4 gl,float[] values,final int[] vbo){
        gl.glGenBuffers(vbo.length, vbo, 0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vbo[0]);
        int bufferSizeInBytes = values.length * Buffers.SIZEOF_FLOAT;
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(values);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, bufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);
    }

    public static void initEBO(GL4 gl,int[] indices,int[] ebo){
        gl.glGenBuffers(ebo.length,ebo,0);
        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER,ebo[0]);
        IntBuffer ibVertices = Buffers.newDirectIntBuffer(indices);
        int bufferSizeInBytes = indices.length * Buffers.SIZEOF_INT;
        gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, bufferSizeInBytes, ibVertices, GL4.GL_STATIC_DRAW);
    }

    public static int[] initGlTexture(final GL4 gl,String texturePath,int[] texture,int i1,int i2,int i3,int i4,String type){
        try {
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), new File(texturePath), false, type);
            gl.glGenTextures(texture.length,texture,0);
            gl.glBindTexture(GL4.GL_TEXTURE_2D,texture[0]);
            gl.glTexImage2D(GL4.GL_TEXTURE_2D,
                    0,
                    data.getInternalFormat(),
                    data.getWidth(),
                    data.getHeight(),
                    data.getBorder(),
                    data.getPixelFormat(),
                    data.getPixelType(),
                    data.getBuffer()
                    );
            //纹理环绕方式
            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, i1);
            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, i2);
            //纹理过滤
            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, i3);
            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, i4);
            gl.glGenerateTextureMipmap(GL4.GL_TEXTURE_2D);
            gl.glBindTexture(GL4.GL_TEXTURE_2D, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[2];
    }
}
