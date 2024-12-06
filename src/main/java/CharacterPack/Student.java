package CharacterPack;

import TilePack.StudentChair;
import TilePack.Tile;
import TilePack.Candy;
import MainPack.Game;
import AlgoPack.Common;
import AlgoPack.Pair;
import GraphicsPack.Classroom;

import java.util.ArrayList;

public class Student extends Character
{
	private int m_candyCounter;
	
	public Student(StudentChair chair)
	{
		super("sprites/students.png", chair);
		
		m_candyCounter = 0;
	}
	
	public int getCandyCounter()
	{
		return m_candyCounter;
	}
	
	@Override
	public Pair<Tile, int[]> getCurrentPosition()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Student>> arrayStudents = classroom.getStudents();
		
		for(int i=0;i<arrayStudents.size();++i)
		{
			for(int j=0;j<arrayStudents.get(i).size();++j)
			{
				if(arrayStudents.get(i).get(j) == this)
				{
					return new Pair<Tile, int[]>(arrayTiles.get(i).get(j), new int[]{i, j});
				}
			}
		}
		
		return null;
	}
	
	public void setCandyCounter(int candyCounter)
	{
		m_candyCounter = candyCounter;
	}
	
	public void incrementCandyCounter()
	{
		++m_candyCounter;
	}
	
	public void decrementCandyCounter()
	{
		--m_candyCounter;
	}
	
	public Candy findNearestCandy()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<Pair<Candy, int[]>> arrayCandies = classroom.getCandies();
		int minDistance = 50000;
		Candy result = null;
		
		for(int i=0;i<arrayCandies.size();++i)
		{
			int maxX = Math.max(arrayCandies.get(i).getValue()[1], classroom.getTileCoords((Tile)m_chair)[1]);
			int maxY = Math.max(arrayCandies.get(i).getValue()[0], classroom.getTileCoords((Tile)m_chair)[0]);
			
			int minX = Math.min(arrayCandies.get(i).getValue()[1], classroom.getTileCoords((Tile)m_chair)[1]);
			int minY = Math.min(arrayCandies.get(i).getValue()[0], classroom.getTileCoords((Tile)m_chair)[0]);
			
			if(Common.distanceManhattan(maxX, maxY, minX, minY) < minDistance)
			{
				minDistance = Common.distanceManhattan(maxX, maxY, minX, minY);
				result = arrayCandies.get(i).getKey();
			}
		}
		
		return result;
	}
	
	public boolean goToNearestCandy()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		Candy nearestCandy = findNearestCandy();
		Pair<Tile, int[]> candyPosition = new Pair<Tile, int[]>((Tile)nearestCandy, classroom.getTileCoords((Tile)nearestCandy));
		Pair<Tile, int[]> currentPosition = getCurrentPosition();
		
		return move(currentPosition, candyPosition);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s ; CandyCounter : %d", super.toString(), m_candyCounter);
	}
}
