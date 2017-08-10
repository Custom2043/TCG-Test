package packet;

import util.IDList;

public class PacketList
{
	public static IDList<Class <? extends Packet>> packet = new IDList<Class <? extends Packet>>();

	public static Class <? extends Packet> getPacketForID(int id)
	{
		return packet.getObjectForID(id);
	}

	public static int getIDForPacket(Class <? extends Packet> c)
	{
		return packet.getIDForObject(c);
	}
	static
	{
		packet.add(DPacketPlayerConnect.class, 1);
		packet.add(SPacketQuitGame.class, 2);
		packet.add(CPacketJoinServer.class, 3);
		packet.add(CPacketPlayerQuit.class, 4);
		packet.add(CPacketServerClosed.class, 5);
		packet.add(DPacketPing.class, 6);
		packet.add(DPacketCancel.class, 7);
		packet.add(SPacketAnswer.class, 8);
		packet.add(DPacketDefi.class, 9);
		packet.add(CPacketNewGame.class, 10);
		packet.add(CPacketPioche.class, 11);
		packet.add(DPacketEndTurn.class, 12);
		packet.add(DPacketMovement.class, 13);
		packet.add(DPacketRelease.class, 14);
		packet.add(DPacketPoseYTB.class, 15);
		packet.add(CPacketWin.class, 16);
		packet.add(DPacketUse.class, 17);
		packet.add(CPacketRandom.class, 18);
		packet.add(DPacketTechnique.class, 19);
		packet.add(DPacketPoseObject.class, 20);
		packet.add(DPacketObjectUse.class, 21);
		packet.add(DPacketDecks.class, 22);
		packet.add(CPacketTrigger.class, 23);
	}
}
