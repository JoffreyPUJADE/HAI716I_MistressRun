package CharacterPack;

import TilePack.StudentChair;
import TilePack.Tile;
import TilePack.Candy;
import MainPack.Game;
import AlgoPack.Common;
import AlgoPack.Pair;
import GraphicsPack.Classroom;
import GraphicsPack.StudentInfo;

import java.util.ArrayList;

public class Student extends Character implements Runnable
{
	private int m_candyCounter;
	private boolean m_returningToChair;
	private boolean m_touched;
	
	public Student(StudentChair chair, int i, int j)
	{
		super("sprites/students.png", chair, i, j);
		
		m_candyCounter = 0;
		m_returningToChair = false;
		m_touched = false;
	}
	
	public int getCandyCounter()
	{
		return m_candyCounter;
	}
	
	public void setCandyCounter(int candyCounter)
	{
		m_candyCounter = candyCounter;
	}

	@Override
	public boolean isTouched()
	{
		return m_touched;
	}

	@Override
	public boolean isEscaping()
	{
		return !isAtChair() && !m_returningToChair;
	}

	public boolean touched()
	{
		if(m_touched)
		{
			return false; 
		}

		m_touched = true;
		return true;
	}

	
	public void incrementCandyCounter()
	{
		++m_candyCounter;
		updateGraphicalCandyCount();
	}
	
	public void decrementCandyCounter()
	{
		--m_candyCounter;
		updateGraphicalCandyCount();
	}
	
	@Override
	public boolean goToChair()
	{
		System.err.println(super.getIndex()+" "+m_candyCounter);
		if(m_returningToChair)
		{
			return false; 
		}

		m_returningToChair = true;

		boolean result = !super.goToChair(); 
		m_returningToChair = false; 
		m_touched = false;

		return result;
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
	
	public boolean candyIsAtOneCaseOrLess()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		Candy nearestCandy = findNearestCandy();
		Pair<Tile, int[]> candyPosition = new Pair<Tile, int[]>((Tile)nearestCandy, classroom.getTileCoords((Tile)nearestCandy));
		Pair<Tile, int[]> currentPosition = getCurrentPosition();
		
		int maxX = Math.max(candyPosition.getValue()[1], currentPosition.getValue()[1]);
		int maxY = Math.max(candyPosition.getValue()[0], currentPosition.getValue()[0]);
		
		int minX = Math.min(candyPosition.getValue()[1], currentPosition.getValue()[1]);
		int minY = Math.min(candyPosition.getValue()[0], currentPosition.getValue()[0]);
		
		return Common.distanceManhattan(maxX, maxY, minX, minY) <= 1;
	}
	
	public boolean tryToGoAtCandy()
	{
		if(goToNearestCandy() && candyIsAtOneCaseOrLess())
		{
			incrementCandyCounter();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s ; CandyCounter : %d", super.toString(), m_candyCounter);
	}
	
	public void run()
	{ 
		while(true)
		{
			if(!getMistresses())
			{
				tryToGoAtCandy();
				goToChair();
			}

			if(getMistresses() && super.getIndex()==2)
			{
				// démarre quand maîtresse, vas a la chaise 
				tryToGoAtCandy();
				goToChair();
			}

			try
			{
				switch(super.getIndex())
				{
					case 1 :
						Thread.sleep(3000);
					break;

					case 2: 
						Thread.sleep(500);
					break;

					case 3: 
						Thread.sleep(1000);
					break;

					case 4: 
						Thread.sleep(1000);
					break;
				}
			}
			catch(InterruptedException err)
			{
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	private void updateGraphicalCandyCount()
	{
		StudentInfo si = (StudentInfo)Game.getInstance().getCharInfo(this);
		si.updateCandyCounter(m_candyCounter);
	}
}