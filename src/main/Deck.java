package main;

import java.util.ArrayList;

import carte.CarteList;

public class Deck 
{
	public ArrayList<CarteList> cards = new ArrayList<CarteList>();
	public boolean after(int i, CarteList cl)
	{
		for (int j = i+1;j<cards.size();j++)
			if (cards.get(j) == cl)
				return true;
		return false;
	}
	public boolean before(int i, CarteList cl)
	{
		for (int j = i-1;j>=0;j--)
			if (cards.get(j) == cl)
				return true;
		return false;
	}
	public int numberOf(CarteList cl)
	{
		int j=0;
		for (int i = 0;i<cards.size();i++)
			if (cards.get(i) == cl)
				j++;
		return j;
	}
}
