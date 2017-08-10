package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class PacketStream extends InputStream
{//A packet in this class represent a array of byte red in a frame, it can be a part of a packet or many of them
	private ArrayList<byte[]> packets = new ArrayList<byte[]>();
	private int index = 0;
	
	public int available()
	{
		int size = 0;
		for (byte[] b : packets)
			size += b.length;
		return size - index;
	}
	
	@Override
	public int read() throws IOException 
	{
		int start = 0;
		for (byte[] b : packets)
			if (start + b.length > index)
			{
				byte bb = b[index++ - start];
				return bb < 0 ? bb+256 : bb;
			}
			else
				start += b.length;
		throw new IOException();
	}
	
	public void addPacket(byte[] b)
	{
		packets.add(b);
	}
	
	public void reset(){index = 0;}
	public void delete()
	{
		//System.out.println(index);
		int start = 0;
		byte[] b = null;
		for (Iterator<byte[]> iter = packets.iterator() ; iter.hasNext() && start < index ; )
		{
			byte[] bb = iter.next();
			if (start + bb.length > index)
			{
				int len = start + bb.length - index;
				if (len > 0)
				{
					b = new byte[len];
					System.arraycopy(bb, index-start, b, 0, len);
					iter.remove();
				}
			}
			else
				iter.remove();
			start += bb.length;
		}
		if (b != null)
			packets.add(0, b);
		index = 0;
		//System.out.println("After delete : "+this);
	}
	
	public String toString()
	{
		String s = "";
		for (byte[] b : packets)
		{
			s+='[';
			for (int bb : b)
				s+=" "+bb+" ";
			s+="]\r\n";
		}
		return s;
	}
}
