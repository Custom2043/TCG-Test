package packet;

import java.io.IOException;

import main.Client;
import util.CustomInputStream;
import util.CustomOutputStream;
import carte.CarteList;
import carte.Piege;

public class CPacketTrigger extends Packet implements Packet.PacketToClient
{
	public int slotId, piegeId;
	public CPacketTrigger(){}
	public CPacketTrigger(Piege p, int s){piegeId = CarteList.getCarteForClass(p.getClass()).id; slotId = s;}
	
	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		piegeId = cis.readInt();slotId = cis.readInt();
	}
	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeInt(piegeId);cos.writeInt(slotId);
	}
	@Override
	public void handleClient()
	{
		Client.game.objectSlots[slotId].carte = ((carte.Object)CarteList.getCarteForID(piegeId).instance(Client.game.getPlayingPlayer()));
		Client.game.objectSlots[slotId].carte.slot = Client.game.objectSlots[slotId];
	}
}
