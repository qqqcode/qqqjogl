package com.qqq.utils;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JoglUtils {

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
}
