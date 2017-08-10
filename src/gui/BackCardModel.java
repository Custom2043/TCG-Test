package gui;

import gui.CardModel.QuadPos;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import util.TextureCoor;
import drawer.VAOLoader;

public class BackCardModel
{
	public final int vaoId, vertexNumber;
	public final Texture texture;
	public BackCardModel(QuadPos ps, TextureCoor tc, Texture text)
	{
		this.vertexNumber =  4;
		
		this.vaoId = VAOLoader.createVAO();
		
		texture = text;
		
		ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber * 8); // ScreenPos
		
		buf.putFloat(ps.x);
		buf.putFloat(ps.y);
		
		buf.putFloat(ps.x+ps.w);
		buf.putFloat(ps.y);
		
		buf.putFloat(ps.x+ps.w);
		buf.putFloat(ps.y+ps.h);
		
		buf.putFloat(ps.x);
		buf.putFloat(ps.y+ps.h);

		VAOLoader.storeBufferInAttributeList(0, 2, buf, GL11.GL_FLOAT);
		
		buf = BufferUtils.createByteBuffer(this.vertexNumber * 8); // Textures
		
		for (float f : tc.inFloatArray(text))
			buf.putFloat(f);
				
		VAOLoader.storeBufferInAttributeList(1, 2, buf, GL11.GL_FLOAT);
	}
}
