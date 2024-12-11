package CharacterPack;

import TilePack.StudentChair;
import TilePack.Tile;
import TilePack.Candy;
import MainPack.Game;
import AlgoPack.Common;
import AlgoPack.Pair;
import GraphicsPack.Classroom;

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
	
	/*@Override
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
		
		return new Pair<>(null, new int[]{-1, -1});
	}*/
	
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
    if (m_touched) {
        return false; // L'étudiant est déjà touché et retourné à sa chaise
    }
    m_touched = true;
    /*boolean success = goToChair();
    if (success) {
        m_touched = false; // Réinitialiser l'état une fois l'étudiant retourné à sa chaise
    }
    return success;*/
	return true;
}

	
	public void incrementCandyCounter()
	{
		++m_candyCounter;
	}
	
	public void decrementCandyCounter()
	{
		--m_candyCounter;
	}
	
	@Override
public boolean goToChair()
{
    if (m_returningToChair) {
        return false; // L'étudiant est déjà en train de revenir à sa chaise
    }
    m_returningToChair = true;
    boolean result = !super.goToChair(); // Effectuer le déplacement vers la chaise
    m_returningToChair = false; // Réinitialiser l'état de retour
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
		// Potentially, where the students' strategy(ies) will be implemented.
		
		if(goToNearestCandy() && candyIsAtOneCaseOrLess())
		{
			++m_candyCounter;
			
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
		tryToGoAtCandy();
		goToChair();
	}
}