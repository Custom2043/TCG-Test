package gui;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import util.ScreenCoor;
import drawer.ScreenCoorShader;
import drawer.ShaderProgram;

public class YTBSlotShader extends ScreenCoorShader
{
	public YTBSlotShader(String vertexFile, String fragmentFile) {super(vertexFile, fragmentFile);}
	
	private int location_coorX, location_coorY, location_gui, location_datas;
	private int location_bg, location_chibi, location_alpha;
	protected void getAllUniformLocations() 
	{
		super.getAllUniformLocations();
		this.location_coorX = super.getUniformLocation("coorX");
		this.location_coorY = super.getUniformLocation("coorY");
		this.location_gui = super.getUniformLocation("gui");
		this.location_bg = super.getUniformLocation("bg");
		this.location_chibi = super.getUniformLocation("chibi");
		this.location_datas = super.getUniformLocation("datas");
		this.location_alpha = super.getUniformLocation("alpha");
		
		this.start();
		
		GL20.glUniform1i(location_bg, 1);
		GL20.glUniform1i(location_chibi, 2);
		
		loadGui(1);
		
		ShaderProgram.stop();
	}
	public void loadCoor(ScreenCoor sc)
	{
		super.loadVector2f(location_coorX, new Vector2f(sc.xScreen + sc.wScreen/2, sc.xGui + sc.wGui/2));
		super.loadVector2f(location_coorY, new Vector2f(sc.yScreen + sc.hScreen/2, sc.yGui + sc.hGui/2));
	}
	public void loadGui(float gui)
	{
		super.loadFloat(location_gui, gui);
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
