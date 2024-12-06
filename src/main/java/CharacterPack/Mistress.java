package CharacterPack;

import TilePack.TeacherChair;
import TilePack.Tile;
import MainPack.Game;
import AlgoPack.Pair;
import GraphicsPack.Classroom;

import java.util.ArrayList;

public class Mistress extends Character
{
	private int m_touchedStudents;
	
	public Mistress(TeacherChair chair)
	{
		super("sprites/mistress.png", chair);
		
		m_touchedStudents = 0;
	}
	
	public int getTouchedStudents()
	{
		return m_touchedStudents;
	}
	
	@Override
	public Pair<Tile, int[]> getCurrentPosition()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Mistress>> arrayMistresses = classroom.getMistresses();
		
		for(int i=0;i<arrayMistresses.size();++i)
		{
			for(int j=0;j<arrayMistresses.get(i).size();++j)
			{
				if(arrayMistresses.get(i).get(j) == this)
				{
					return new Pair<Tile, int[]>(arrayTiles.get(i).get(j), new int[]{i, j});
				}
			}
		}
		
		return null;
	}
}
