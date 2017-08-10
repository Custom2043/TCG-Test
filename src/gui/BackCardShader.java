package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import util.ScreenCoor;
import drawer.ScreenCoorShader;
import drawer.ShaderProgram;

public class BackCardShader extends ScreenCoorShader
{
	public BackCardShader(String vertexFile, String fragmentFile) {super(vertexFile, fragmentFile);}
	
	private int location_coorX, location_coorY, location_rot, location_alpha;
	protected void getAllUniformLocations() 
	{
		super.getAllUniformLocations();
		this.location_coorX = super.getUniformLocation("coorX");
		this.location_coorY = super.getUniformLocation("coorY");
		this.location_rot = super.getUniformLocation("rot");
		this.location_alpha = super.getUniformLocation("alpha");
		
		this.start();
		ShaderProgram.stop();
	}
	public void loadCoor(ScreenCoor sc)
	{
		super.loadVector2f(location_coorX, new Vector2f(sc.xScreen + sc.wScreen/2, sc.xGui + sc.wGui/2));
		super.loadVector2f(location_coorY, new Vector2f(sc.yScreen + sc.hScreen/2, sc.yGui + sc.hGui/2));
	}
	public void loadRot(float rotZ, float rotY, float gui)
	{
		super.loadVector(location_rot, new Vector3f(rotZ, rotY, gui));
	}
	public void loadAlpha(float alpha)
	{
		super.loadFloat(location_alpha, alpha);
	}
	protected void bindAttributes() 
	{
		super.bindAttribute(0, "pos");
		super.bindAttribute(1, "textureCoordinates");
	}
}
