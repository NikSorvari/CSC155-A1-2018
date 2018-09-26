package a1;
import java.nio.*;
import javax.swing.*;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL4.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.common.nio.Buffers;

import graphicslib3D.*;
import graphicslib3D.GLSLUtils;

import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.Math.*;




public class Starter extends JFrame implements GLEventListener, KeyListener, MouseWheelListener
{
	private GLCanvas myCanvas;
	private int rendering_program;
	private int vao[] = new int[1];
	private GLSLUtils util = new GLSLUtils();
	
	private float x = 0.0f; //coordinates for triangle
	private float y = 0.0f;
	private float inc = 0.01f; // vertical movement speed
	private static float cf = 0;
	private float size;
	private float angle = 0f;
	private int motionType = 0; // movement style (vertical or circle)
	private int sizeChange = 0; // flag to signal user's change in the triangle size
	private int colorType = 0;    // triangle color (solid blue or rgb gradient)
	private float colorFrag = 0.0f; // color flag for the  vshader
	
	private int offset_loc;
	private int offset_loc1;
	private int offset_loc2;
	private int size_loc;
	private int color_loc;
	   
	//panel and buttons to change motion style
	private JButton bCircle;
	private JButton bVertical;
	
	ColorChange myCommand = new ColorChange();
	

	
	public Starter()
	{
		setTitle("Assignment 1");
		setSize(500, 500);
		
		JPanel topPanel = new JPanel();
		this.add(topPanel,BorderLayout.NORTH);
		bCircle = new JButton ("Circle");
		bVertical = new JButton ("Vertical");
		topPanel.add(bCircle);
		topPanel.add(bVertical);
		
		
		
		// get the content pane of the JFrame (this)
		JComponent contentPane = (JComponent) this.getContentPane();
		// get the "focus is in the window" input map for the content pane
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = contentPane.getInputMap(mapName);
		// create a keystroke object to represent the "c" key
		KeyStroke cKey = KeyStroke.getKeyStroke('c');
		// put the "cKey" keystroke object into the content pane’s "when focus is
		// in the window" input map under the identifier name "color“
		imap.put(cKey, "color");
		// get the action map for the content pane
		ActionMap amap = contentPane.getActionMap();
		
		// put the "myCommand" command object into the content pane's action map
		amap.put("color", myCommand);
		//have the JFrame request keyboard focus
		this.requestFocus();
		
		
		 //button for vertical movement
	     bVertical.addActionListener(
	     new ActionListener()
	     {
	    	 public void actionPerformed(ActionEvent e)
	         {
	    		 System.out.println("Vertical");
	             motionType = 1;
	             x=0.0f;
	             y=0.0f;
	             inc=0.01f;
	             angle=0f;
	             myCanvas.requestFocus();
	         }
	     });
	     
	     //button for circular movement
	     bCircle.addActionListener(
	     new ActionListener()
	     {
	         public void actionPerformed(ActionEvent e)
	         {
	             System.out.println("Circle");
	             motionType = 2;
	             x=0.0f;
	             y=0.0f;
	             inc=0.01f;
	             angle=0f;
	             
	             myCanvas.requestFocus();
	         }
	     });
	     
	     //adding elements to window
	     myCanvas = new GLCanvas();
		 myCanvas.addGLEventListener(this);
		 myCanvas.setFocusable(true);
	     myCanvas.addMouseWheelListener(this);
		 this.add(myCanvas);
		 setVisible(true);
		 
		 myCanvas.requestFocus();
		 FPSAnimator animator = new FPSAnimator(myCanvas, 30);
		 animator.start();
	}

	public static void main(String[] args) 
	{
		new Starter();
	}
	

	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() == 1) {
	         size = size + 0.05f;
	      } 
	      else if(e.getWheelRotation() == -1) {
	         size = size - 0.05f;
	      }
	      if(size < 0.05f) {
	         size = 0.05f;
	      }
	      if(size > 1.0f) {
	         size = 1.0f;
	      }
	      
	      sizeChange = 1;
	      
	      
		
	      myCanvas.requestFocus();
		
	}

	public void keyPressed(KeyEvent e) {
		System.out.println("keyPressed");
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("keyReleased");
		if(e.getKeyCode()== KeyEvent.VK_C) {
	         if( colorType == 0)
	         {
	            colorType = 1;
	            System.out.println("colorChange");
	         }
		}
	}
	
	public static void setColor()
	{
		if(cf == 0.0f) {
			cf = 1.0f;
         } else {
        	 cf = 0.0f;
         }
		System.out.println(cf);
	}

	public void display(GLAutoDrawable drawable) {
		
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(rendering_program);
		
		float bkg[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
		gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);
		
		//circular motion
		if (motionType == 1)
		{
			if (y < -1.0f)
			{
				System.out.println("going up");
				inc= 0.01f;
			}
			if(y > 1.0f)
			{
				System.out.println("going down");
				inc= -0.01f;
			}
			
			y+=inc;
			
			
			
		}
		offset_loc = gl.glGetUniformLocation(rendering_program, "y");
		gl.glProgramUniform1f(rendering_program, offset_loc, y);
		
		//circular motion
		if (motionType == 2)
		{
			angle += 0.1;
			x = (float) Math.cos(angle)/2;
			y = (float) Math.sin(angle)/3;
			
			
		}
		
		offset_loc1 = gl.glGetUniformLocation(rendering_program, "x");
		gl.glProgramUniform1f(rendering_program, offset_loc1, x);
		offset_loc2 = gl.glGetUniformLocation(rendering_program, "y");
		gl.glProgramUniform1f(rendering_program, offset_loc2, y);
		
		int size_loc = gl.glGetUniformLocation(rendering_program, "size");
	    gl.glProgramUniform1f(rendering_program, size_loc, size);
		
		int color_loc = gl.glGetUniformLocation(rendering_program, "cf");
        gl.glProgramUniform1f(rendering_program, color_loc, cf);
      
        gl.glDrawArrays(GL_TRIANGLES,0,3);
	}


	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		rendering_program = createShaderProgram();
		gl.glGenVertexArrays(vao.length,  vao, 0);
		gl.glBindVertexArray(vao[0]);
		
	}

	
	
	private int createShaderProgram()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] vertCompiled = new int[1];
		int[] fragCompiled = new int[1];
		int[] linked = new int[1];


		String vshaderSource[] = util.readShaderSource("a1/vert.shader");
		String fshaderSource[] = util.readShaderSource("a1/frag.shader");
		int lengths[];

		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

		gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
		gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);

		gl.glCompileShader(vShader);
		gl.glCompileShader(fShader);
		
		checkOpenGLError();  // can use returned boolean if desired
		gl.glGetShaderiv(vShader, GL_COMPILE_STATUS, vertCompiled, 0);
		if (vertCompiled[0] == 1)
		{	System.out.println("vertex compilation success");
		} else
		{	System.out.println("vertex compilation failed");
			printShaderLog(vShader);
		}
		
		checkOpenGLError();  // can use returned boolean if desired
		gl.glGetShaderiv(fShader, GL_COMPILE_STATUS, fragCompiled, 0);
		if (fragCompiled[0] == 1)
		{	System.out.println("fragment compilation success");
		} else
		{	System.out.println("fragment compilation failed");
			printShaderLog(fShader);
		}

		int vfprogram = gl.glCreateProgram();
		gl.glAttachShader(vfprogram, vShader);
		gl.glAttachShader(vfprogram, fShader);
		
		gl.glLinkProgram(vfprogram);
		checkOpenGLError();
		gl.glGetProgramiv(vfprogram, GL_LINK_STATUS, linked, 0);
		if (linked[0] == 1)
		{	System.out.println("linking succeeded");
		} else
		{	System.out.println("linking failed");
			printProgramLog(vfprogram);
		}

		gl.glDeleteShader(vShader);
		gl.glDeleteShader(fShader);		
		return vfprogram;
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	public void dispose(GLAutoDrawable drawable) {}
	
	private void printShaderLog(int shader)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[] log = null;

		// determine the length of the shader compilation log
		gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len, 0);
		if (len[0] > 0)
		{	log = new byte[len[0]];
			gl.glGetShaderInfoLog(shader, len[0], chWrittn, 0, log, 0);
			System.out.println("Shader Info Log: ");
			for (int i = 0; i < log.length; i++)
			{	System.out.print((char) log[i]);
			}
		}
	}

	void printProgramLog(int prog)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[] log = null;

		// determine length of the program compilation log
		gl.glGetProgramiv(prog, GL_INFO_LOG_LENGTH, len, 0);
		if (len[0] > 0)
		{	log = new byte[len[0]];
			gl.glGetProgramInfoLog(prog, len[0], chWrittn, 0, log, 0);
			System.out.println("Program Info Log: ");
			for (int i = 0; i < log.length; i++)
			{	System.out.print((char) log[i]);
			}
		}
	}

	boolean checkOpenGLError()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		boolean foundError = false;
		GLU glu = new GLU();
		int glErr = gl.glGetError();
		while (glErr != GL_NO_ERROR)
		{	System.err.println("glError: " + glu.gluErrorString(glErr));
			foundError = true;
			glErr = gl.glGetError();
		}
		return foundError;
	}
	
}
