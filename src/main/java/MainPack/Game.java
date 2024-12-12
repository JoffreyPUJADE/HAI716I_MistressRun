package MainPack;

import java.awt.Dimension;
import java.awt.Toolkit;

import GraphicsPack.Classroom;
import GraphicsPack.Window;
import GraphicsPack.CharacterInfo;
import GraphicsPack.MistressInfo;
import GraphicsPack.StudentInfo;
import CharacterPack.Character;
import CharacterPack.Mistress;
import CharacterPack.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class Game
{
	private Classroom m_classroom;
	private Window m_window;
	private HashMap<Character, CharacterInfo> m_charInfos;
	private static Game m_instance = new Game();
	
	public Game()
	{
		m_classroom = new Classroom();
		
		int classroomWidth = m_classroom.getGlobalWidth();
		int classroomHeight = m_classroom.getGlobalHeight();
		m_classroom.setBounds(25, 25, classroomWidth, classroomHeight);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();

		m_charInfos = new HashMap<>();
		ArrayList<ArrayList<Character>> chars = m_classroom.getCharacters();
		int charsCounter = 0;

		for(int i=0;i<chars.size();++i)
		{
			for(int j=0;j<chars.get(i).size();++j)
			{
				if(chars.get(i).get(j) != null)
				{
					if(chars.get(i).get(j) instanceof Mistress)
					{
						m_charInfos.put(chars.get(i).get(j), new MistressInfo(charsCounter * 200, (classroomHeight / 4) * 5, 200, 50, "strat", ((Mistress)chars.get(i).get(j)).getTouchedStudents()));
					}
					else if(chars.get(i).get(j) instanceof Student)
					{
						m_charInfos.put(chars.get(i).get(j), new StudentInfo(charsCounter * 200, (classroomHeight / 4) * 5, 200, 50, "strat", ((Student)chars.get(i).get(j)).getCandyCounter()));
					}

					++charsCounter;
				}
			}
		}
		
		m_window = new Window("MistressRun", width, height);
		m_window.add(m_classroom);

		for(CharacterInfo ci : m_charInfos.values())
		{
			m_window.add(ci);
		}

		m_window.setVisible(true);
		
		m_window.repaint();
	}
	
	public Classroom getClassroom()
	{
		return m_classroom;
	}

	public CharacterInfo getCharInfo(Character character)
	{
		return m_charInfos.get(character);
	}
	
	public void move()
	{
		m_classroom.moveAllStudentToCandy();
	}
	
	public static Game getInstance()
	{
		return m_instance;
	}
}