package com.blieve.dodgie.controller;

/*
public class GLRenderer implements Renderer {

    private Context context;
    private Sprite sprite;

    public GLRenderer(Context c) {
        if(Variables.getInstance().log){
            Log.d("CREATION", "GLRenderer(Context c)");
        }
        context = c;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set the bg color
        sprite = new Sprite();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        Log.d("CREATION", "drawFrame");
        glClear(GLES20.GL_COLOR_BUFFER_BIT); // Redraw background color
        sprite.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        glViewport(0, 0, width, height);
    }

}
 */

/*
public class ShaderHelper {

    public static int loadVertex(String shaderCode) {
        return loadShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int loadFragment(String shaderCode) {
        return loadShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int loadShader(int type, String shaderCode){
        final int shaderId = glCreateShader(type);
        if(shaderId == 0) {
            if(Variables.getInstance().log){
                Log.d("CREATION", "shaderId = 0");
            }
            return 0;
        }
        glShaderSource(shaderId, shaderCode);
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        if(compileStatus[0] == 0) {
            if(Variables.getInstance().log){
                Log.d("CREATION", "compileStatus = 0");
            }
            glDeleteShader(shaderId);
            return 0;
        }
        glCompileShader(shaderId);
        return shaderId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = glCreateProgram();
        if (programId == 0) {
            if(Variables.getInstance().log){
                Log.d("CREATION", "programId = 0");
            }
            return 0;
        }
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if(linkStatus[0] == 0) {
            if(Variables.getInstance().log){
                Log.d("CREATION", "linkStatus = 0");
            }
            glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }

}
*/

/*
public class Sprite {

    private static final int COORDS_PER_VERTEX = 2;
    private static final float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private final FloatBuffer vertexData;
    private int program;
    private int colorHandle,
            positionHandle;

    private float[] verts = {
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };
    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    public Sprite() {
        if(Variables.getInstance().log){
            Log.d("CREATION", "Sprite()");
        }
        vertexData = ByteBuffer.allocateDirect(verts.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(verts);
        vertexData.position(0);

        String vertexShaderCode = "attribute vec4 a_Position;" +
                // "gl_PointSize = 10.0;" +
                "void main() {" +
                "    gl_Position = a_Position;" +
                "}";
        String fragmentShaderCode = "precision mediump float;" +
                "uniform vec4 u_Color;" +
                "void main() {" +
                "    gl_FragColor = u_Color;" +
                "}";
        int vertexShader = ShaderHelper.loadVertex(vertexShaderCode);
        int fragmentShader = ShaderHelper.loadFragment(fragmentShaderCode);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
    }

    public void draw() {
        glUseProgram(program);
        positionHandle = glGetAttribLocation(program, "a_Position");
        colorHandle = glGetUniformLocation(program, "u_Color");

        glVertexAttribPointer(positionHandle, 3, GL_FLOAT,
                false, 3 * 4, vertexData);
        glEnableVertexAttribArray(positionHandle);

        glUniform4fv(colorHandle, 1, color, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableVertexAttribArray(positionHandle);
    }

}
*/

/*
public class GLView extends GLSurfaceView {

    private GLRenderer renderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    public GLView(Context c){
        super(c);
        init(c);
    }

    public GLView(Context c, AttributeSet attrs) {
        super(c, attrs);
        init(c);
    }

    private void init(Context c) {
        setEGLContextClientVersion(2); // Create an OpenGL ES 2.0 context
        renderer = new GLRenderer(c);
        //setEGLConfigChooser(8, 8, 8, 8, 16, 0); // test
        setRenderer(renderer); // Set the Renderer for drawing on the GLSurfaceView
        // Render the view only when there is a change in the drawing data
        // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
 */