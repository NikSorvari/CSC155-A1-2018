
import java.nio.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;

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
	private int angle;
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
		
		 //button for vertical movement
	     bVertical.addActionListener(
	     new ActionListener()
	     {
	    	 public void actionPerformed(ActionEvent e)
	         {
	    		 System.out.println("Vertical");
	             motionType = 1;
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
	             myCanvas.requestFocus();
	         }
	     });
	     
	     //adding elements to window
	     myCanvas = new GLCanvas();
		 myCanvas.addGLEventListener(this);
		 myCanvas.setFocusable(true);
	     myCanvas.addKeyListener((java.awt.event.KeyListener) this);
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
		
		if(e.getKeyCode()== KeyEvent.VK_C) {
	         if( colorType == 0)
	            colorType = 1;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(rendering_program);
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
