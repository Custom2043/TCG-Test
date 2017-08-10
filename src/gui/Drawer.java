package gui;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import gui.CardModel.QuadPos;

import java.util.Iterator;

import main.Client;
import main.Player;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import util.CardPosition;
import util.Logger;
import util.Matrix;
import util.MovementCard;
import util.QuadColor;
import util.ScreenCoor;
import util.TextureCoor;
import animation.FlipAnimation;
import animation.PoseAnimation;
import animation.SlotToCardAnimation;
import animation.TriggerAnimation;
import carte.Carte;
import carte.CarteList;
import carte.CimetiereWrapper;
import carte.ClientObjectWrapper;
import carte.ClientYTBWrapper;
import carte.Effet;
import carte.Fusion;
import carte.ObjectSlot;
import carte.Piege;
import carte.Posable;
import carte.Slot;
import carte.Talent;
import carte.Technique;
import carte.YTB;
import carte.YTBSlot;
import drawer.CustomDrawer;
import drawer.ScreenCoorModel;
import drawer.ScreenCoorShader;
import drawer.ShaderProgram;
import drawer.TextureManager;
import drawer.TexturedScreenCoorShader;

public class Drawer extends CustomDrawer
{
	public static TextureManager loader = new TextureManager("Ressources/textures/", "PNG");
	public static final Texture TITLE = loader.loadTexture("Title.png"),
								CIMETIEREVIDE = loader.loadTexture("Cimetiere.png"),
								THINGS = loader.loadTexture("atlas/Things.png"),
								BACKGROUND = loader.loadTexture("background.png");
								;
								
	public static final Texture[] NOMS = new Texture[]{loader.loadTexture("atlas/Noms0.png"), loader.loadTexture("atlas/Noms1.png")};
	public static final Texture[] BG = new Texture[]{loader.loadTexture("atlas/BG0.png"), loader.loadTexture("atlas/BG1.png"), loader.loadTexture("atlas/BG2.png"), loader.loadTexture("atlas/BG3.png"), loader.loadTexture("atlas/BG4.png")};
	public static final Texture[] CHIBI = new Texture[]{loader.loadTexture("atlas/CHIBI0.png"), loader.loadTexture("atlas/CHIBI1.png"), loader.loadTexture("atlas/CHIBI2.png"), loader.loadTexture("atlas/CHIBI3.png"), loader.loadTexture("atlas/CHIBI4.png")};
	public static final Texture[] TXT = new Texture[]{loader.loadTexture("atlas/TXT0.png"), loader.loadTexture("atlas/TXT1.png")};

	public static AngelCodeFont font;
	
	public static YTBShader ytbShader = new YTBShader("Ressources/shaders/YTBVertex.txt", "Ressources/shaders/YTBFragment.txt");
	public static ScreenCoorShader shader = new TexturedScreenCoorShader("Ressources/shaders/TexturedScreenCoorVertex.txt", "Ressources/shaders/TexturedScreenCoorFragment.txt");
	public static ScreenCoorModel background = new ScreenCoorModel(ScreenCoor.AllScreen, new QuadColor(Color.white), TextureCoor.allPicture, BACKGROUND);
	public static BackCardModel backCard = new BackCardModel(new QuadPos(-744/12f, -1034/12f, 744/6f, 1034/6f), new TextureCoor(580, 517, 372, 517), THINGS);
	public static ScreenCoorModel[] emptyYTB = new ScreenCoorModel[YTBSlot.NUMBER * 2 + ObjectSlot.NUMBER * 2];
	public static BackCardShader backCardShader = new BackCardShader("Ressources/shaders/BackCardVertex.txt", "Ressources/shaders/BackCardFragment.txt");
	public static YTBSlotShader ytbSlotShader = new YTBSlotShader("Ressources/shaders/YTBSlotVertex.txt", "Ressources/shaders/YTBSlotFragment.txt");
	public static YTBSlotModel faceCache = new YTBSlotModel(
			new QuadPos[]{//i=151
					new QuadPos(-744/12f, -688/12f, 744/6f, 688/6f), //contour
					new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //bg
					new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //chibi
				},
				new TextureCoor[]{
					new TextureCoor(580, 1034+344, 372, 344), //cadre
					new TextureCoor(340 * (1), 323 * (1), 340, 323), //bg
					new TextureCoor(340 * (1), 323 * (1), 340, 323),  // chibi
						
				}, BG[4], CHIBI[4]);
	public static final CardModel YTBS[] = new CardModel[151];
	public static final YTBSlotModel YTBSlots[] = new YTBSlotModel[151];
	public static YTBSlotModel playerSlot;
	public static final ScreenCoorModel cims[] = new ScreenCoorModel[2];
	public static void init()
	{
		playerSlot = new YTBSlotModel( //Init Player YTBSlot i = 152 
				new QuadPos[]{
					new QuadPos(-744/12f, -688/12f, 744/6f, 688/6f), //contour
					new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //bg
					new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //chibi
					
					new QuadPos(19/6f-744/12f, 180/6f-688/12f, 68/3f, 123/3f),//Attaque
					new QuadPos(28/6f-744/12f, 240/6f-688/12f, 116/6f, 106/6f),//Texte Attaque
					new QuadPos(590/6f-744/12f, 180/6f-688/12f, 68/3f, 123/3f),//Vie
					new QuadPos(603/6f-744/12f, 240/6f-688/12f, 116/6f, 106/6f),//Texte Vie
				},
				new TextureCoor[]{
					new TextureCoor(580, 1034+344, 372, 344), //cadre
					new TextureCoor(340 * 2, 323 * 1, 340, 323), //bg
					new TextureCoor(340 * 2, 323 * 1, 340, 323),  // chibi
					new TextureCoor(58*3+58, 53*9, 68, 123),//attack
					new TextureCoor(0, 0, 58, 53),//attack txt
					new TextureCoor(58*3+58+68, 53*9, 68, 123),//vie
					new TextureCoor(0, 0, 58, 53),//vie txt
						
				}, BG[4], CHIBI[4]);
		for (int i=0;i<YTBSlot.NUMBER;i++)
		{
			emptyYTB[i] = new ScreenCoorModel(GuiIngame.getYTBSlotCoor(false, i), new QuadColor(Color.white), new TextureCoor(580, 517*2, 372, 344), THINGS);
			emptyYTB[i+YTBSlot.NUMBER] = new ScreenCoorModel(GuiIngame.getYTBSlotCoor(true, i), new QuadColor(Color.white), new TextureCoor(580, 517*2, 372, 344), THINGS);
		}
		for (int i=0;i<ObjectSlot.NUMBER;i++)
		{
			emptyYTB[i+YTBSlot.NUMBER*2] = new ScreenCoorModel(GuiIngame.getObjectSlotCoor(false, i), new QuadColor(Color.white), new TextureCoor(580, 517*2+344, 372, 344), THINGS);
			emptyYTB[i+YTBSlot.NUMBER*2+ObjectSlot.NUMBER] = new ScreenCoorModel(GuiIngame.getObjectSlotCoor(true, i), new QuadColor(Color.white), new TextureCoor(580, 517*2+344, 372, 344), THINGS);
		}
		cims[0] = new ScreenCoorModel(GuiIngame.getCimSlotCoor(false), new QuadColor(Color.white), TextureCoor.allPicture, CIMETIEREVIDE);
		cims[1] = new ScreenCoorModel(GuiIngame.getCimSlotCoor(true), new QuadColor(Color.white), TextureCoor.allPicture, CIMETIEREVIDE);

		for (int i=0;i<YTBS.length;i++)
		{
			int j = i - 126 * (i/126);
			int k = i - 36 * (i/36);
			CarteList c = CarteList.getCarteForID(i+1);
			int categorie;
			if (c != null)
				categorie = c.categorie;
			else
				categorie = 0;
			
			if (categorie < 5) // YTB
			{
				YTBS[i] = new CardModel(new QuadPos[]{
						new QuadPos(33/6f-744/12f,748/6f-1034/12f, 680/6f, 254/6f), //bas (Style)
						new QuadPos(-744/12f, -1034/12f, 744/6f, 1034/6f), //cadre
						new QuadPos(-744/12f+33/6f, -1034/12f+33/6f, 680/6f, 646/6f) , //bg
						new QuadPos(-744/12f+33/6f, -1034/12f+33/6f, 680/6f, 646/6f), //chibi
						new QuadPos(33/6f-744/12f, 105/6f, 680/6f, 193/6f), //nom
						new QuadPos(584/6f-744/12f, 33/6f-1034/12f, 128/6f, 128/6f),// Caté
						new QuadPos(33/6f-744/12f, 815/6f-1034/12f, 680/6f, 188/6f), //txt
						new QuadPos(43/6f-744/12f, 33/6f-1034/12f, 145/6f, 180/6f),//Mana
						new QuadPos(56/6f-744/12f, 70/6f-1034/12f, 115/6f, 106/6f), //Texte Mana
						new QuadPos(33/6f-744/12f, 772/6f-1034/12f, 122/6f, 230/6f),//Attaque
						new QuadPos(28/6f-744/12f, 832/6f-1034/12f, 115/6f, 106/6f),//Texte Attaque
						new QuadPos(590/6f-744/12f, 772/6f-1034/12f, 122/6f, 230/6f),//Vie
						new QuadPos(603/6f-744/12f, 832/6f-1034/12f, 115/6f, 106/6f),//Texte Vie
					}
					,
					new TextureCoor[]{
						new TextureCoor(0, 658+127*categorie, 340, 127),//bas
						new TextureCoor(580, 0, 372, 517), //cadre
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323), //bg
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323),  // chibi
						new TextureCoor(340 * (j%6), 97*(j/6), 340, 97),//nom
						new TextureCoor(categorie < 3 ? categorie*64 : (categorie-3)*64, categorie < 3 ? 530 : 600, 64, 64),
						new TextureCoor(340 * (j%6), 94*(j/6), 340, 94),//txt
						new TextureCoor(58*3+58+68*2, 477, 73, 90),//mana
						new TextureCoor(0, /*53*6*/0, 58, 53),//mana texte
						new TextureCoor(58*3+58+(33-19)/2, 53*9+1, 61, 115),//attack
						new TextureCoor(0, 0, 58, 53),//attack txt
						new TextureCoor(58*3+68+58, 53*9+1, 61, 115),//vie
						new TextureCoor(0, /*53*3*/0, 58, 53),//vie txt
					}
				, NOMS[i/126], BG[i/36], CHIBI[i/36], TXT[i/126]);
				
				YTBSlots[i] = new YTBSlotModel(
					new QuadPos[]{
						new QuadPos(-744/12f, -688/12f, 744/6f, 688/6f), //contour
						new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //bg
						new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //chibi
						
						new QuadPos(19/6f-744/12f, 180/6f-688/12f, 68/3f, 123/3f),//Attaque
						new QuadPos(28/6f-744/12f, 240/6f-688/12f, 116/6f, 106/6f),//Texte Attaque
						new QuadPos(590/6f-744/12f, 180/6f-688/12f, 68/3f, 123/3f),//Vie
						new QuadPos(603/6f-744/12f, 240/6f-688/12f, 116/6f, 106/6f),//Texte Vie
					},
					new TextureCoor[]{
						new TextureCoor(580, 1034+344, 372, 344), //cadre
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323), //bg
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323),  // chibi
						new TextureCoor(58*3+58, 53*9, 68, 123),//attack
						new TextureCoor(0, 0, 58, 53),//attack txt
						new TextureCoor(58*3+58+68, 53*9, 68, 123),//vie
						new TextureCoor(0, 0, 58, 53),//vie txt
							
					}, BG[i/36], CHIBI[i/36]);
			}
			else
			{
				YTBS[i] = new CardModel(new QuadPos[]{
						new QuadPos(33/6f-744/12f,748/6f-1034/12f, 680/6f, 254/6f), //bas (Style)
						new QuadPos(-744/12f, -1034/12f, 744/6f, 1034/6f), //cadre
						new QuadPos(-744/12f+33/6f, -1034/12f+33/6f, 680/6f, 646/6f) , //bg
						new QuadPos(-744/12f+33/6f, -1034/12f+33/6f, 680/6f, 646/6f), //chibi
						new QuadPos(33/6f-744/12f, 105/6f, 680/6f, 193/6f), //nom
						new QuadPos(584/6f-744/12f, 33/6f-1034/12f, 128/6f, 128/6f),// Caté
						new QuadPos(33/6f-744/12f, 815/6f-1034/12f, 680/6f, 188/6f), //txt
						new QuadPos(43/6f-744/12f, 33/6f-1034/12f, 145/6f, 180/6f),//Mana
						new QuadPos(56/6f-744/12f, 70/6f-1034/12f, 115/6f, 106/6f), //Texte Mana
					}
					,
					new TextureCoor[]{
						new TextureCoor(0, 658+127*categorie, 340, 127),//bas
						new TextureCoor(580, 0, 372, 517), //cadre
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323), //bg
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323),  // chibi
						new TextureCoor(340 * (j%6), 97*(j/6), 340, 97),//nom
						new TextureCoor(categorie < 3 ? categorie*64 : (categorie-3)*64, categorie < 3 ? 530 : 600, 64, 64),
						new TextureCoor(340 * (j%6), 94*(j/6), 340, 94),//txt
						new TextureCoor(58*3+58+68*2, 477, 73, 90),//mana
						new TextureCoor(0, 0, 58, 53),//mana texte
					}
				, NOMS[i/126], BG[i/36], CHIBI[i/36], TXT[i/126]);
				
				YTBSlots[i] = new YTBSlotModel(
					new QuadPos[]{
						new QuadPos(-744/12f, -688/12f, 744/6f, 688/6f), //contour
						new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //bg
						new QuadPos(-744/12f+32f/6, -688/12f+33/6f, 680/6f, 646/6f), //chibi
					},
					new TextureCoor[]{
						new TextureCoor(580, 1034+344, 372, 344), //cadre
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323), //bg
						new TextureCoor(340 * (k%6), 323 * (k/6), 340, 323),  // chibi
							
					}, BG[i/36], CHIBI[i/36]);
			}
				
		}
		ScreenCoorShader.setMatrix(Matrix.createOrthographicMatrix(Display.getWidth(), Display.getHeight()));
		ScreenCoorShader.setScreenData(Display.getWidth(), Display.getHeight(), 1);
		try 
		{
			font = new AngelCodeFont("Tahoma_35.fnt","Tahoma_35.png");
		}
		catch (SlickException e) {Logger.error(e, Drawer.class);}
	}
	public static void quit()
	{
		loader.quit();
		ShaderProgram.quit();
		Display.destroy();
	}
	public static void update()
	{
		if (CustomDrawer.hasScreenOrGuiBeenModified())
		{
			ScreenCoorShader.setMatrix(Matrix.createOrthographicMatrix(Display.getWidth(), Display.getHeight()));
			ScreenCoorShader.setScreenData(Display.getWidth(), Display.getHeight(), 1);
		}
		CustomDrawer.updateScreenSize();
		if (Display.getHeight() < 1440 || Display.getWidth() < 2560)
			setGui(1);
		else if (Display.getHeight() < 2880 || Display.getWidth() < 5120)
			setGui(2);
		else
			setGui(4);
		CustomDrawer.update();
	}
	public static void drawPlayer(boolean bas)
	{
		ytbSlotShader.start();
		ytbSlotShader.loadCoor(GuiIngame.getPlayerCoor(bas));
		ytbSlotShader.loadData(0, (bas ? Client.game.bas : Client.game.haut).mana(), (bas ? Client.game.bas : Client.game.haut).getVie());
		ytbSlotShader.loadGui(1);
		ytbSlotShader.loadAlpha(1);
		drawYTBSlotModel(playerSlot, ALL);
		ShaderProgram.stop();
	}
	public static void drawMovementCard(Player p, CardPosition cp, Carte c)
	{
		drawMovementCard(p, cp, c, ALL);
	}
	public static void drawMovementCard(Player p, CardPosition cp, Carte c, int mod)
	{
		drawMovementCard(p, cp, c, mod, 1);
	}
	public static void drawMovementCard(Player p, CardPosition cp, Carte c, int mod, float alpha)
	{
		if (c == null)
			drawBackCard(p, cp, alpha);
		else if (cp.y > Math.PI/2 || cp.y < -Math.PI/2)
			drawBackCard(p, cp, alpha);
		else if (c instanceof YTB)
			drawYTBInMain(p, (YTB)c,  cp, mod, alpha);
		else if (c instanceof Piege)
			drawPiegeInMain(p, (Piege)c,  cp, mod, alpha);
		else if (c instanceof Technique)
			drawTechniqueInMain(p, (Technique)c,  cp, mod, alpha);
		else if (c instanceof Talent)
			drawTalentInMain(p, (Talent)c,  cp, mod, alpha);
	}
	private static int ALL = 0, CARD = 1, CARDCHIBI = 2, BEFORECHIBI = 3, AFTERCHIBI = 4;
	public static void drawYTBInMain(Player p, YTB b, CardPosition cp)
	{
		drawYTBInMain(p, b, cp, ALL, 1);
	}
	public static void drawYTBInMain(Player p, YTB b, CardPosition cp, int mod, float alpha)
	{
		ytbShader.start();
		ytbShader.loadCoor(cp.coor);
		ytbShader.loadRot(cp.z, cp.y, cp.gui);
		if (p == null)
			ytbShader.loadData(b instanceof Fusion ? -1 : b.getCost(p), b.getAttack(), b.getVie());
		else
			ytbShader.loadData(b instanceof Fusion ? -1 : b.getCost(p), b.baseAttaque, b.baseVie);
		ytbShader.loadAlpha(alpha);
		drawYTBModel(YTBS[CarteList.getCarteForClass(b.getClass()).id-1], mod);
		ShaderProgram.stop();
	}
	public static void drawTalentInMain(Player p, Talent b, CardPosition cp)
	{
		drawTalentInMain(p, b, cp, ALL, 1);
	}
	public static void drawTalentInMain(Player p, Talent b, CardPosition cp, int mod, float alpha)
	{
		ytbShader.start();
		ytbShader.loadCoor(cp.coor);
		ytbShader.loadRot(cp.z, cp.y, cp.gui);
		ytbShader.loadData(b.getCost(p), 0, 0);
		ytbShader.loadAlpha(alpha);
		drawYTBModel(YTBS[CarteList.getCarteForClass(b.getClass()).id-1], mod);
		ShaderProgram.stop();
	}
	public static void drawPiegeInMain(Player p, Piege b, CardPosition cp)
	{
		drawPiegeInMain(p, b, cp, ALL, 1);
	}
	public static void drawPiegeInMain(Player p, Piege b, CardPosition cp, int mod, float alpha)
	{
		ytbShader.start();
		ytbShader.loadCoor(cp.coor);
		ytbShader.loadRot(cp.z, cp.y, cp.gui);
		ytbShader.loadData(b.getCost(p), 0, 0);
		ytbShader.loadAlpha(alpha);
		drawYTBModel(YTBS[CarteList.getCarteForClass(b.getClass()).id-1], mod);
		ShaderProgram.stop();
	}
	public static void drawTechniqueInMain(Player p, Technique b, CardPosition cp)
	{
		drawTechniqueInMain(p, b, cp, ALL, 1);
	}
	public static void drawTechniqueInMain(Player p, Technique b, CardPosition cp, int mod, float alpha)
	{
		ytbShader.start();
		ytbShader.loadCoor(cp.coor);
		ytbShader.loadRot(cp.z, cp.y, cp.gui);
		ytbShader.loadData(b.getCost(p), 0, 0);
		ytbShader.loadAlpha(alpha);
		drawYTBModel(YTBS[CarteList.getCarteForClass(b.getClass()).id-1], mod);
		ShaderProgram.stop();
	}
	public static void drawBackground()
	{
		shader.start();
		CustomDrawer.drawModel(background);
		ShaderProgram.stop();
	}
	public static void drawZoomedCarte(Player p, Carte c, ScreenCoor coor)
	{
		if (coor.getMiddleY() - 3*coor.hGui/2 < 0)
			coor = coor.addYGui(-coor.getMiddleY() + 3*coor.hGui/2);
		if (coor.getMiddleY() + 3*coor.hGui/2 > Display.getHeight())
			coor = coor.addYGui(Display.getHeight()-coor.getMiddleY()-3*coor.hGui/2);
		drawMovementCard(p, new CardPosition(coor, 0, 0, 3), c, ALL);
		int i=0;
		if (c instanceof YTB && ((YTB) c).slot != null)
			for (Effet e : ((YTB)c).effets)
			{
				if (coor.getStartX() > Display.getWidth()/2)
					Drawer.drawString(coor.getMiddleX()-744/4f-5, coor.getMiddleY()-1034/4f + i * 25, -1, 0, e.toString(), font, Color.blue);
				else
					Drawer.drawString(coor.getMiddleX()+744/4f+5, coor.getMiddleY()-1034/4f + i * 25, 0, 0, e.toString(), font, Color.blue);
				i++;
			}
	}
	public static void drawString(float x, float y, float xx, float yy, String text, AngelCodeFont font, Color c)
	{
		CustomDrawer.drawString(x + font.getWidth(text) * xx, y + font.getHeight(text) * yy, false, false, text, font, c);
	}
	public static void drawCimetiere(CimetiereWrapper cw)
	{
		shader.start();
		CustomDrawer.drawModel(cims[cw.slot.possesseur.bas ? 1 : 0]);
		ScreenCoorShader.stop();
		if (cw.slot.carte != null)
			drawMovementCard(cw.slot.possesseur, new CardPosition(cw.coor, 0, 0, 1), cw.slot.carte);
		
		for (int i=0;i<cw.cartes.size();i++)
		{
			MovementCard mc = cw.cartes.get(i);
			mc.update();
			if (!mc.inAnim())
				cw.slot.putCarte((Posable)mc.carte);
		}
		
		for (Iterator<MovementCard> iter = cw.cartes.iterator() ; iter.hasNext() ; )
		{
			MovementCard mc = iter.next();
			if (mc.inAnim())//Quand slot to card, géré par le slot
			{
				drawMovementCard(cw.slot.possesseur, mc.getLastCardPosition(), mc.carte, ALL);
			}
			else if (!mc.inAnim())
			{
				drawMovementCard(cw.slot.possesseur, new CardPosition(cw.coor, 0, 0, 1), cw.slot.carte);
				iter.remove();
			}
		}
				
	}
	public static void drawYTBModel(CardModel model, int mod)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textTXT.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textNom.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textChibi.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textBG.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL_TEXTURE_2D, THINGS.getTextureID());
		TextureImpl.bindNone();
		
		GL30.glBindVertexArray(model.vaoId);
		for (int i=0;i<2;i++)
			GL20.glEnableVertexAttribArray(i);
		if (mod == ALL)
			GL11.glDrawArrays(GL11.GL_QUADS, 0, model.vertexNumber);
		if (mod == CARD)
		{
			GL11.glDrawArrays(GL11.GL_QUADS, 0, 12);
			GL11.glDrawArrays(GL11.GL_QUADS, 16, model.vertexNumber - 16);
		}
		if (mod == CARDCHIBI)
			GL11.glDrawArrays(GL11.GL_QUADS, 12, 4);
		if (mod == BEFORECHIBI)
			GL11.glDrawArrays(GL11.GL_QUADS, 0, 12);
		if (mod == AFTERCHIBI)
			GL11.glDrawArrays(GL11.GL_QUADS, 16, model.vertexNumber - 16);

		for (int i=0;i<2;i++)
			GL20.glDisableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
	}
	public static void drawEmptySlot(int id)
	{
		shader.start();
		CustomDrawer.drawModel(emptyYTB[id]);
		ShaderProgram.stop();
	}
	public static void drawBackCard(Player p, CardPosition cp, float alpha)
	{
		backCardShader.start();
		backCardShader.loadCoor(cp.coor);
		backCardShader.loadRot(cp.z, cp.y, cp.gui);
		backCardShader.loadAlpha(alpha);
		backCard.texture.bind();
		
		GL30.glBindVertexArray(backCard.vaoId);
		
		for (int i=0;i<2;i++)
			GL20.glEnableVertexAttribArray(i);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, backCard.vertexNumber);
		for (int i=0;i<2;i++)
			GL20.glDisableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
		
		ShaderProgram.stop();
	}
	public static void drawYTBSlot(ClientYTBWrapper s)
	{
		s.movement.update();
		if (s.slot.getCarte() == null)
		{
			drawEmptySlot(Client.game.getSlotId(s.slot));
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof FlipAnimation)
		{
			drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte());
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof PoseAnimation)
		{
			float dif = s.movement.getLastDif();
			if (s.movement.getAnim().getId() == 5)
			{
				drawSlot(s.movement.getLastCardPosition(), s.slot, BEFORECHIBI, dif);
				drawSlot(s.movement.getLastCardPosition(), s.slot, CARDCHIBI, 1);
				drawSlot(s.movement.getLastCardPosition(), s.slot, AFTERCHIBI, dif);
			}
			else if (s.movement.getAnim().getId() == 3)
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(), s.movement.getLastCardPosition(), CARDCHIBI, 1);
			else if (s.movement.getAnim().getId() == 2)
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(), s.movement.getLastCardPosition(), CARDCHIBI, 1);
			else if (s.movement.getAnim().getId() == 1)
			{
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(), new CardPosition(((PoseAnimation)s.movement.getAnim()).base, 0, 0, 1.3f-0.2f*dif), CARD, 1-dif);
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(), s.movement.getLastCardPosition(), CARDCHIBI, 1);
			}
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof SlotToCardAnimation)
		{
			if (s.movement.getAnim().getId() == 1)
			{
				drawSlot(new CardPosition(s.coor, 0, 0, 1), s.slot, ALL, 1-s.movement.getLastDif());
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(),s.movement.getLastCardPosition(), CARDCHIBI, 1);
			}
			else if (s.movement.getAnim().getId() == 2)
			{
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(),s.movement.getLastCardPosition(), CARD, s.movement.getLastDif());
				drawYTBInMain(s.slot.possesseur, s.slot.getCarte(), s.movement.getLastCardPosition(), CARDCHIBI, 1);
			}
		}
		else
		{
			drawSlot(s.movement.getLastCardPosition(), s.slot, ALL, 1);
			CustomDrawer.drawRect(ScreenCoor.gui(s.coor.getMiddleX()-10, s.coor.getEndY()-10, 20, 20), s.slot.getCarte().isNextAttackSpecial() ? Color.blue : s.slot.getCarte().pouvoir.canAttack() ? Color.green : Color.red);
		}
	}
	public static void drawYTBSlotModel(YTBSlotModel model, int mod)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textChibi.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textBG.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL_TEXTURE_2D, THINGS.getTextureID());
		TextureImpl.bindNone();
		
		GL30.glBindVertexArray(model.vaoId);
		
		for (int i=0;i<2;i++)
			GL20.glEnableVertexAttribArray(i);
		if (mod == ALL)
			GL11.glDrawArrays(GL11.GL_QUADS, 0, model.vertexNumber);
		if (mod == CARD)
		{
			GL11.glDrawArrays(GL11.GL_QUADS, 0, 8);
			GL11.glDrawArrays(GL11.GL_QUADS, 12, model.vertexNumber - 12);
		}
		if (mod == CARDCHIBI)
			GL11.glDrawArrays(GL11.GL_QUADS, 8, 4);
		if (mod == BEFORECHIBI)
			GL11.glDrawArrays(GL11.GL_QUADS, 0, 8);
		if (mod == AFTERCHIBI)
			GL11.glDrawArrays(GL11.GL_QUADS, 12, model.vertexNumber - 12);
		for (int i=0;i<2;i++)
			GL20.glDisableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
	}
	/*public static void drawObjectSlot(ClientObjectWrapper s)
	{
		CardPosition cp = s.movement.getCardPosition();
		if (s.slot.faceCache)
		{
			if (s.movement.inAnim() && s.movement.getAnim() instanceof PoseCacheAnimation)
			{
				float dif = s.movement.getLastDif();
				
				if (s.movement.getAnim().getId() == 5)
				{
					ytbSlotShader.start();
					ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
					ytbSlotShader.loadData(0, 0, 0);
					ytbSlotShader.loadAlpha(dif);
					drawYTBSlotModel(faceCache, BEFORECHIBI);
					ytbSlotShader.loadAlpha(1);
					drawYTBSlotModel(faceCache, CARDCHIBI);
					ytbSlotShader.loadAlpha(dif);
					drawYTBSlotModel(faceCache, AFTERCHIBI);
					ShaderProgram.stop();
				}
				else if (s.movement.getAnim().getId() == 3)
				{
					ytbSlotShader.start();
					ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
					ytbSlotShader.loadAlpha(1);
					ytbSlotShader.loadGui(s.movement.getCardPosition().gui);
					drawYTBSlotModel(faceCache, CARDCHIBI);
					ShaderProgram.stop();
				}
				else if (s.movement.getAnim().getId() == 2)
				{
					ytbSlotShader.start();
					ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
					ytbSlotShader.loadAlpha(1);
					ytbSlotShader.loadGui(s.movement.getCardPosition().gui);
					drawYTBSlotModel(faceCache, CARDCHIBI);
					ShaderProgram.stop();
				}
				else if (s.movement.getAnim().getId() == 1)
				{
					drawMovementCard(s.slot.possesseur, new CardPosition(((PoseCacheAnimation)s.movement.getAnim()).base, 0, 0, 1.3f-0.2f*dif), null, CARD, 1-dif);
					ytbSlotShader.start();
					ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
					ytbSlotShader.loadAlpha(1);
					ytbSlotShader.loadGui(s.movement.getCardPosition().gui);
					drawYTBSlotModel(faceCache, CARDCHIBI);
					ShaderProgram.stop();
				}
			}
			else if (s.movement.inAnim() && s.movement.getAnim() instanceof SlotToCardAnimation)
			{
				if (s.movement.getAnim().getId() == 1)
				{
					ytbSlotShader.start();
					ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
					ytbSlotShader.loadAlpha(1);
					ytbSlotShader.loadGui(s.movement.getCardPosition().gui);
					drawYTBSlotModel(faceCache, CARDCHIBI);
					ShaderProgram.stop();
				}
				else if (s.movement.getAnim().getId() == 2)
				{
					ytbSlotShader.start();
					ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
					ytbSlotShader.loadAlpha(1);
					ytbSlotShader.loadGui(s.movement.getCardPosition().gui);
					drawYTBSlotModel(faceCache, CARDCHIBI);
					ShaderProgram.stop();
					drawBackCard(s.slot.possesseur, s.movement.getCardPosition(), s.movement.getLastDif());
				}
			}
			else
			{
				ytbSlotShader.start();
				ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
				ytbSlotShader.loadAlpha(1);
				ytbSlotShader.loadGui(1);
				GL13.glActiveTexture(GL13.GL_TEXTURE2);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, faceCache.textChibi.getTextureID());
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, faceCache.textBG.getTextureID());
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL_TEXTURE_2D, THINGS.getTextureID());
				TextureImpl.bindNone();
				
				GL30.glBindVertexArray(faceCache.vaoId);
				
				for (int i=0;i<2;i++)
					GL20.glEnableVertexAttribArray(i);
				GL11.glDrawArrays(GL11.GL_QUADS, 0, faceCache.vertexNumber);
				for (int i=0;i<2;i++)
					GL20.glDisableVertexAttribArray(i);
				
				GL30.glBindVertexArray(0);
				
				ShaderProgram.stop();
			}
		}
		else if (s.slot.getCarte() == null)
		{
			drawEmptySlot(Client.game.getSlotId(s.slot)+12);
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof PoseAnimation)
		{
			float dif = s.movement.getLastDif();
			
			if (s.movement.getAnim().getId() == 5)
			{
				YTBSlotModel model = YTBSlots[CarteList.getCarteForClass(s.slot.getCarte().getClass()).id-1];
				
				ytbSlotShader.start();
				ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
				ytbSlotShader.loadData(0, 0, 0);
				ytbSlotShader.loadGui(1);
				ytbSlotShader.loadAlpha(dif);
				drawYTBSlotModel(model, BEFORECHIBI);
				ytbSlotShader.loadAlpha(1);
				drawYTBSlotModel(model, CARDCHIBI);
				ytbSlotShader.loadAlpha(dif);
				drawYTBSlotModel(model, AFTERCHIBI);
				ShaderProgram.stop();
			}
			else if (s.movement.getAnim().getId() == 3)
				drawMovementCard(s.slot.possesseur, cp, s.slot.getCarte(), CARDCHIBI, 1);
			else if (s.movement.getAnim().getId() == 2)
			{
				drawMovementCard(s.slot.possesseur, cp, s.slot.getCarte(), CARDCHIBI, 1);
			}
			else if (s.movement.getAnim().getId() == 1)
			{
				drawMovementCard(s.slot.possesseur, new CardPosition(((PoseAnimation)s.movement.getAnim()).base, 0, 0, 1.3f-0.2f*dif), s.slot.getCarte(), CARD, 1-dif);
				drawMovementCard(s.slot.possesseur, cp, s.slot.getCarte(), CARDCHIBI, 1);
			}
		}
		else
		{
			YTBSlotModel model = YTBSlots[CarteList.getCarteForClass(s.slot.getCarte().getClass()).id-1];
			
			ytbSlotShader.start();
			ytbSlotShader.loadCoor(s.movement.getCardPosition().coor);
			ytbSlotShader.loadAlpha(1);
			ytbSlotShader.loadGui(1);
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textChibi.getTextureID());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.textBG.getTextureID());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL_TEXTURE_2D, THINGS.getTextureID());
			TextureImpl.bindNone();
			
			GL30.glBindVertexArray(model.vaoId);
			
			for (int i=0;i<2;i++)
				GL20.glEnableVertexAttribArray(i);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, model.vertexNumber);
			for (int i=0;i<2;i++)
				GL20.glDisableVertexAttribArray(i);
			
			GL30.glBindVertexArray(0);
			
			ShaderProgram.stop();
		}
	}*/
	public static void drawObjectSlot(ClientObjectWrapper s)
	{
		s.movement.update();
		if (s.slot.faceCache)
		{
			if (s.movement.inAnim() && s.movement.getAnim() instanceof PoseAnimation)
			{
				if (s.movement.getAnim().getId() == 5)
				{
					drawFaceCache(s.movement.getLastCardPosition(), BEFORECHIBI, s.movement.getLastDif());
					drawFaceCache(s.movement.getLastCardPosition(), CARDCHIBI, 1);
					drawFaceCache(s.movement.getLastCardPosition(), AFTERCHIBI, s.movement.getLastDif());
				}
				else if (s.movement.getAnim().getId() == 3)
					drawFaceCache(s.movement.getLastCardPosition(), CARDCHIBI, 1);
				else if (s.movement.getAnim().getId() == 2)
					drawFaceCache(s.movement.getLastCardPosition(), CARDCHIBI, 1);
				else if (s.movement.getAnim().getId() == 1)
				{
					drawMovementCard(s.slot.possesseur, new CardPosition(((PoseAnimation)s.movement.getAnim()).base, 0, 0, 1.3f-0.2f*s.movement.getLastDif()), null, CARD, 1-s.movement.getLastDif());
					drawFaceCache(s.movement.getLastCardPosition(), CARDCHIBI, 1);
				}
			}
			else if (s.movement.inAnim() && s.movement.getAnim() instanceof SlotToCardAnimation)
			{
				if (s.movement.getAnim().getId() == 1)
				{
					drawFaceCache(new CardPosition(s.coor, 0, 0, 1), CARD, 1 - s.movement.getLastDif());
					drawFaceCache(s.movement.getLastCardPosition(), CARDCHIBI, 1);
				}
				else if (s.movement.getAnim().getId() == 2)
				{
					drawFaceCache(s.movement.getLastCardPosition(), CARDCHIBI, 1);
					drawBackCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.movement.getLastDif());
				}
			}
			else
				drawFaceCache(s.movement.getLastCardPosition(), ALL, 1);
		}
		else if (s.slot.getCarte() == null)
		{
			drawEmptySlot(Client.game.getSlotId(s.slot) + YTBSlot.NUMBER * 2);
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof FlipAnimation)
		{
			drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte());
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof TriggerAnimation)
		{
			drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte(), ALL, 1);
			if (s.movement.getAnim().getId() == 0)
				drawMovementCard(s.slot.possesseur, new CardPosition(s.movement.getLastCardPosition().coor, 0, 0, 1.3f + 2*s.movement.getLastDif() * s.movement.getLastDif()), s.slot.getCarte(), CARDCHIBI, 1-s.movement.getLastDif()*s.movement.getLastDif());
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof PoseAnimation)
		{
			float dif = s.movement.getLastDif();
			if (s.movement.getAnim().getId() == 5)
			{
				drawSlot(s.movement.getLastCardPosition(), s.slot, BEFORECHIBI, dif);
				drawSlot(s.movement.getLastCardPosition(), s.slot, CARDCHIBI, 1);
				drawSlot(s.movement.getLastCardPosition(), s.slot, AFTERCHIBI, dif);
			}
			else if (s.movement.getAnim().getId() == 3)
				drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte(), CARDCHIBI, 1);
			else if (s.movement.getAnim().getId() == 2)
				drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte(), CARDCHIBI, 1);
			else if (s.movement.getAnim().getId() == 1)
			{
				drawMovementCard(s.slot.possesseur, new CardPosition(((PoseAnimation)s.movement.getAnim()).base, 0, 0, 1.3f-0.2f*dif), s.slot.getCarte(), CARD, 1-dif);
				drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte(), CARDCHIBI, 1);
			}
		}
		else if (s.movement.inAnim() && s.movement.getAnim() instanceof SlotToCardAnimation)
		{
			if (s.movement.getAnim().getId() == 1)
			{
				drawSlot(new CardPosition(s.coor, 0, 0, 1), s.slot, CARD, 1-s.movement.getLastDif());
				drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte(), CARDCHIBI, 1);
			}
			else if (s.movement.getAnim().getId() == 2)
			{
				drawMovementCard(s.slot.possesseur,s.movement.getLastCardPosition(), s.slot.getCarte(), CARD, s.movement.getLastDif());
				drawMovementCard(s.slot.possesseur, s.movement.getLastCardPosition(), s.slot.getCarte(), CARDCHIBI, 1);
			}
		}
		else
			drawSlot(s.movement.getLastCardPosition(), s.slot, ALL, 1);
	}
	public static void drawSlot(CardPosition cp, Slot y, int mod, float alpha)
	{
		ytbSlotShader.start();
		ytbSlotShader.loadCoor(cp.coor);
		if (y instanceof YTBSlot)
			ytbSlotShader.loadData(0, ((YTBSlot)y).getCarte().getAttack(), ((YTBSlot)y).getCarte().getVie());
		ytbSlotShader.loadGui(1);
		ytbSlotShader.loadAlpha(alpha);
		drawYTBSlotModel(YTBSlots[CarteList.getCarteForClass(y.getCarte().getClass()).id-1], mod);
		ShaderProgram.stop();
	}
	public static void drawFaceCache(CardPosition cp, int mod, float alpha)
	{
		ytbSlotShader.start();
		ytbSlotShader.loadCoor(cp.coor);
		ytbSlotShader.loadGui(cp.gui);
		ytbSlotShader.loadAlpha(alpha);
		drawYTBSlotModel(faceCache, mod);
		ShaderProgram.stop();
	}
}
