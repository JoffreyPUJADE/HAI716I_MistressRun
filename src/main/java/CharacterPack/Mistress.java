package CharacterPack;

import TilePack.TeacherChair;

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
}
