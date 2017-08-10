package gui;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import util.TextureCoor;
import drawer.VAOLoader;

public class CardModel
{
	public final int vaoId, vertexNumber;
	public Texture textNom = null, textBG = null, textChibi = null, textTXT = null;
	
	public CardModel(QuadPos[] pos, TextureCoor[] tc, Texture text, Texture bg, Texture chibi, Texture t)
	{
		this.vertexNumber = pos.length * 4;
		
		this.vaoId = VAOLoader.createVAO();
		
		this.textNom = text;
		this.textBG = bg;
		this.textChibi = chibi;
		this.textTXT = t;
		
		ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber * 8); // ScreenPos
		
		for (QuadPos ps : pos)
		{
			buf.putFloat(ps.x);
			buf.putFloat(ps.y);
			
			buf.putFloat(ps.x+ps.w);
			buf.putFloat(ps.y);
			
			buf.putFloat(ps.x+ps.w);
			buf.putFloat(ps.y+ps.h);
			
			buf.putFloat(ps.x);
			buf.putFloat(ps.y+ps.h);
		}

		VAOLoader.storeBufferInAttributeList(0, 2, buf, GL11.GL_FLOAT);
		
		buf = BufferUtils.createByteBuffer(this.vertexNumber * 8); // Textures
		
		for (float f : tc[0].inFloatArray(Drawer.THINGS))
			buf.putFloat(f);
		for (float f : tc[1].inFloatArray(Drawer.THINGS))
			buf.putFloat(f);
		for (float f : tc[2].inFloatArray(bg))
			buf.putFloat(f);
		for (float f : tc[3].inFloatArray(chibi))
			buf.putFloat(f);
		for (float f : tc[4].inFloatArray(text))
			buf.putFloat(f);
		for (float f : tc[5].inFloatArray(Drawer.THINGS))
			buf.putFloat(f);
		for (float f : tc[6].inFloatArray(t))
			buf.putFloat(f);
		for (float f : tc[7].inFloatArray(Drawer.THINGS))
			buf.putFloat(f);
		for (float f : tc[8].inFloatArray(Drawer.THINGS))
			buf.putFloat(f);
		if (tc.length > 9)
		{
			for (float f : tc[9].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
			for (float f : tc[10].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
			for (float f : tc[11].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
			for (float f : tc[12].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
		}
				
		VAOLoader.storeBufferInAttributeList(1, 2, buf, GL11.GL_FLOAT);
	}
	public static class QuadPos
	{
		float x, y, w, h;
		public QuadPos(float xx, float yy, float ww, float hh)
		{
			x = xx;y = yy;w = ww;h = hh;
		}
	}
}
