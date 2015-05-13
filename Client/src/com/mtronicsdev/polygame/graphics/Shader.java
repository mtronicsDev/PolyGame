package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

/**
 * Represents a GLSL shader and manages it.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Shader implements GLObject {

    static {
        Resources.registerResourceHandler(f -> {
            String sub = f.getName().split("_")[1].replace(".glsl", "");

            ShaderType type = sub.equals("vert") ? ShaderType.VERTEX_SHADER :
                    sub.equals("frag") ? ShaderType.FRAGMENT_SHADER :
                            sub.equals("geom") ? ShaderType.GEOMETRY_SHADER :
                                    sub.equals("cont") ? ShaderType.TESSELLATION_CONTROL_SHADER :
                                            sub.equals("eval") ? ShaderType.TESSELLATION_EVALUATION_SHADER :
                                                    sub.equals("comp") ? ShaderType.COMPUTE_SHADER : null;

            if (type == null) throw new IllegalArgumentException("The shader file name has to end with " +
                    "\"_[ShaderType].glsl\".\n" +
                    "Shader types are:\n " +
                    "* vert - Vertex Shader\n" +
                    "* frag - Fragment Shader\n" +
                    "* geom - Geometry Shader\n" +
                    "* cont - Tessellation Control Shader\n" +
                    "* eval - Tessellation Evaluation Shader\n" +
                    "* comp - Compute Shader\n");

            final String[] shaderSourceCode = {""};

            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));

                reader.lines().forEachOrdered(line -> shaderSourceCode[0] += line);
                reader.close();
            } catch (FileNotFoundException e) {
                System.err.println("The file " + f.getPath() + " was not found or is a directory.");
                System.err.println("Could not read shader source code.");
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return new Shader(type, shaderSourceCode[0]);
        }, Shader.class);
    }

    private int id;

    private Shader(ShaderType type, String source) {
        id = glCreateShader(type.type);

        glShaderSource(id, source);
        glCompileShader(id);

        IntBuffer compileStatus = BufferUtils.createIntBuffer(1);
        glGetShader(id, GL_COMPILE_STATUS, compileStatus);

        if (compileStatus.get() == GL11.GL_FALSE)
            throw new RuntimeException("The " + type.name + " \"" + String.valueOf(id) + "\" could not be compiled.");
    }

    public static void init() {
    } //Used to trigger static initializer

    public int getId() {
        return id;
    }

    public ShaderType getType() {
        int type = glGetShaderi(id, GL_SHADER_TYPE);

        if (type == ShaderType.VERTEX_SHADER.type) return ShaderType.VERTEX_SHADER;
        if (type == ShaderType.FRAGMENT_SHADER.type) return ShaderType.FRAGMENT_SHADER;
        if (type == ShaderType.GEOMETRY_SHADER.type) return ShaderType.GEOMETRY_SHADER;
        if (type == ShaderType.TESSELLATION_CONTROL_SHADER.type) return ShaderType.TESSELLATION_CONTROL_SHADER;
        if (type == ShaderType.TESSELLATION_EVALUATION_SHADER.type) return ShaderType.TESSELLATION_EVALUATION_SHADER;
        if (type == ShaderType.COMPUTE_SHADER.type) return ShaderType.COMPUTE_SHADER;
        else return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        unbind();
    }

    @Override
    public void unbind() {
        glDeleteShader(id);
    }

    private enum ShaderType {
        VERTEX_SHADER(GL_VERTEX_SHADER, "Vertex Shader"),
        FRAGMENT_SHADER(GL_FRAGMENT_SHADER, "Fragment Shader"),
        GEOMETRY_SHADER(GL_GEOMETRY_SHADER, "Geometry Shader"),
        TESSELLATION_CONTROL_SHADER(GL_TESS_CONTROL_SHADER, "Tessellation Control Shader"),
        TESSELLATION_EVALUATION_SHADER(GL_TESS_EVALUATION_SHADER, "Tessellation Evaluation Shader"),
        COMPUTE_SHADER(GL_COMPUTE_SHADER, "Compute Shader");

        int type;
        String name;

        ShaderType(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
