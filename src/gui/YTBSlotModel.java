package gui;

import gui.CardModel.QuadPos;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import util.TextureCoor;
import drawer.VAOLoader;

public class YTBSlotModel
{
	public final int vaoId, vertexNumber;
	public Texture textBG = null, textChibi = null;
	
	public YTBSlotModel(QuadPos[] pos, TextureCoor[] tc, Texture bg, Texture chibi)
	{
		this.vertexNumber = pos.length * 4;
		
		this.vaoId = VAOLoader.createVAO();
		
		this.textBG = bg;
		this.textChibi = chibi;
		
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
		for (float f : tc[1].inFloatArray(bg))
			buf.putFloat(f);
		for (float f : tc[2].inFloatArray(chibi))
			buf.putFloat(f);
		if (tc.length > 3)
		{
			for (float f : tc[3].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
			for (float f : tc[4].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
			for (float f : tc[5].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
			for (float f : tc[6].inFloatArray(Drawer.THINGS))
				buf.putFloat(f);
		}
		
		VAOLoader.storeBufferInAttributeList(1, 2, buf, GL11.GL_FLOAT);
	}
}
