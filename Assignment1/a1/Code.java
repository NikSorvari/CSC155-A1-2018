
import java.nio.*;
import javax.swing.*;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLContext;
import com.jogamp.common.nio.Buffers;

import graphicslib3D.*;
import graphicslib3D.GLSLUtils;

import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.Math.*;




public class Code extends JFrame implements GLEventListener, KeyListener, MouseWheelListener
{
	private GLCanvas myCanvas;
	private int rendering_program;
	private int vao[] = new int[1];
	private GLSLUtils util = new GLSLUtils();
	
	private float x = 0.0f; //coordinates for triangle
	private float y = 0.0f;
	private float inc = 0.01f; // vertical movement speed
	private float size;
	private float angle = 0f;
	private int motionType = 0; // movement style (vertical or circle)
	private int sizeChange = 0; // flag to signal user's change in the triangle size
	private int colorType = 0;    // triangle color (solid blue or rgb gradient)
	private float colorFrag = 0.0f; // color flag for the  vshader
	   
	//panel and buttons to change motion style
	private JButton bCircle;
	private JButton bVertical;
	

	
	public Code()
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
		ColorChange myCommand = new ColorChange();
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
		new Code();
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

	public void display(GLAutoDrawable drawable) {
		
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(rendering_program);
		
		float bkg[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
		gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);
		
		if (motionType == 1)
		{
			if (y < -1.0f)
				inc= 0.01f;
			if(y > 1.0f)
				inc= -0.01f;
			
			y+=inc;
			
			int offset_loc = gl.glGetUniformLocation(rendering_program, "inc");
			gl.glProgramUniform1f(rendering_program, offset_loc, y);
			
		}
		if (motionType == 2)
		{
			angle += 0.1;
			x = (float) Math.cos(angle)/2;
			y = (float) Math.sin(angle)/2;
			
			int offset_loc = gl.glGetUniformLocation(rendering_program, "x");
			gl.glProgramUniform1f(rendering_program, offset_loc, x);
			int offset_loc2 = gl.glGetUniformLocation(rendering_program, "y");
			gl.glProgramUniform1f(rendering_program, offset_loc2, y);
		}
		
		int offset_loc = gl.glGetUniformLocation(rendering_program, "size");
	    gl.glProgramUniform1f(rendering_program, offset_loc, size);
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

		String vshaderSource[] = util.readShaderSource("a1/vert.shader");
		String fshaderSource[] = util.readShaderSource("a1/frag.shader");
		int lengths[];

		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

		gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
		gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);

		gl.glCompileShader(vShader);
		gl.glCompileShader(fShader);

		int vfprogram = gl.glCreateProgram();
		gl.glAttachShader(vfprogram, vShader);
		gl.glAttachShader(vfprogram, fShader);
		gl.glLinkProgram(vfprogram);
		return vfprogram;
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	public void dispose(GLAutoDrawable drawable) {}
	
}
