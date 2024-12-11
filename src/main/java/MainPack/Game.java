package MainPack;

import GraphicsPack.Classroom;
import GraphicsPack.Window;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Game
{
	private Classroom m_classroom;
	private Window m_window;
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
		
		m_window = new Window("MistressRun", width, height);
		m_window.add(m_classroom);
		m_window.setVisible(true);
		
		m_window.repaint();
	}
	
	public Classroom getClassroom()
	{
		return m_classroom;
	}
	
	public void move()
	{
		//m_classroom.moveFirstStudent(0, 14);
		m_classroom.moveAllStudentToCandy();
		//m_classroom.moveMistress();
		//m_classroom.moveMistressToFirstStudent();
	}
	
	public static Game getInstance()
	{
		return m_instance;
	}
}