package CharacterPack;

import TilePack.StudentChair;

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
}
