package gui;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import util.ScreenCoor;
import drawer.ScreenCoorShader;
import drawer.ShaderProgram;

public class YTBShader extends ScreenCoorShader
{
	public YTBShader(String vertexFile, String fragmentFile) {super(vertexFile, fragmentFile);}
	
	private int location_coorX, location_coorY, location_rot, location_datas;
	private int location_noms, location_bg, location_chibi, location_txt, location_alpha;
	protected void getAllUniformLocations() 
	{
		super.getAllUniformLocations();
		this.location_coorX = super.getUniformLocation("coorX");
		this.location_coorY = super.getUniformLocation("coorY");
		this.location_rot = super.getUniformLocation("rot");
		this.location_noms = super.getUniformLocation("noms");
		this.location_bg = super.getUniformLocation("bg");
		this.location_chibi = super.getUniformLocation("chibi");
		this.location_datas = super.getUniformLocation("datas");
		this.location_txt = super.getUniformLocation("txt");
		this.location_alpha = super.getUniformLocation("alpha");
		
		this.start();
		
		GL20.glUniform1i(location_bg, 1);
		GL20.glUniform1i(location_chibi, 2);
		GL20.glUniform1i(location_noms, 3);
		GL20.glUniform1i(location_txt, 4);
		
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
	public void loadData(float mana, float attack, float vie)
	{
		super.loadVector(location_datas, new Vector3f(mana, attack, vie));
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
