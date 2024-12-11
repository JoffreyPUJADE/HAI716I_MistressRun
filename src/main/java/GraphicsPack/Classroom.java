package GraphicsPack;

import LoaderDataPack.ClassroomData;
import LoaderDataPack.TileData;
import LoaderDataPack.ClassroomLoader;
import AlgoPack.Common;
import AlgoPack.Pair;
import TilePack.Tile;
import TilePack.Floor;
import TilePack.Board;
import TilePack.Desk;
import TilePack.Candy;
import TilePack.StudentChair;
import TilePack.TeacherChair;
import CharacterPack.Character;
import CharacterPack.Student;
import CharacterPack.Mistress;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;

public class Classroom extends JPanel
{
	private ArrayList<ArrayList<Tile>> m_classroom;
	private ArrayList<ArrayList<Student>> m_students;
	private ArrayList<ArrayList<Mistress>> m_mistresses;
	private int m_tileSize;
	private final ReentrantLock m_lockUpdatePosition = new ReentrantLock();
	private final ReentrantLock m_lockRepaint = new ReentrantLock();
	
	public Classroom()
	{
		super();
		setLayout(null);
		
		ClassroomData classroomDatas = ClassroomLoader.loadClassroom(Common.getStreamFromResource("classroom.json"));
		
		if(classroomDatas == null)
		{
			JOptionPane.showMessageDialog(null, "Classroom not found.");
			System.exit(-1);
		}
		
		m_classroom = new ArrayList<>();
		m_students = new ArrayList<>();
		m_mistresses = new ArrayList<>();
		
		ArrayList<ArrayList<TileData>> tilesDatas = classroomDatas.getClassroom();
		
		for(int i=0;i<tilesDatas.size();++i)
		{
			m_classroom.add(new ArrayList<>());
			m_students.add(new ArrayList<>());
			m_mistresses.add(new ArrayList<>());
			
			for(int j=0;j<tilesDatas.get(i).size();++j)
			{
				String orientation = tilesDatas.get(i).get(j).getOrientation();
				
				if(!verifyOrientation(tilesDatas.get(i).get(j).getType(), orientation))
				{
					JOptionPane.showMessageDialog(null, "Orientation not valid.");
					System.exit(-1);
				}
				
				switch(tilesDatas.get(i).get(j).getType())
				{
					case "floor":
						m_classroom.get(i).add(new Floor(null));
						m_students.get(i).add(null);
						m_mistresses.get(i).add(null);
					break;
					
					case "board":
						m_classroom.get(i).add(new Board());
						m_students.get(i).add(null);
						m_mistresses.get(i).add(null);
					break;
					
					case "desk":
						m_classroom.get(i).add(new Desk());
						m_students.get(i).add(null);
						m_mistresses.get(i).add(null);
					break;
					
					case "candy":
						m_classroom.get(i).add(new Candy(Common.randint(1, Common.countFilesInResourcesSubdirectory("candies"))));
						m_students.get(i).add(null);
						m_mistresses.get(i).add(null);
					break;
					
					case "studentChair":
						m_classroom.get(i).add(new StudentChair(orientation, null));
						m_students.get(i).add(new Student((StudentChair)m_classroom.get(i).get(j), i, j));
						m_classroom.get(i).get(j).takeTile(m_students.get(i).get(j));
						m_mistresses.get(i).add(null);
					break;
					
					case "teacherChair":
						m_classroom.get(i).add(new TeacherChair(orientation, null));
						m_students.get(i).add(null);
						m_mistresses.get(i).add(new Mistress((TeacherChair)m_classroom.get(i).get(j), i, j));
						m_classroom.get(i).get(j).takeTile(m_mistresses.get(i).get(j));
					break;
					
					default:
						JOptionPane.showMessageDialog(null, String.format("Invalid type of tile : %s.", tilesDatas.get(i).get(j).getType()));
						System.exit(-1);
					break;
				}
			}
		}
		
		m_tileSize = 48;
	}
	
	public int getGlobalWidth()
	{
		return m_classroom.get(0).size() * m_tileSize;
	}
	
	public int getGlobalHeight()
	{
		return m_classroom.size() * m_tileSize;
	}
	
	public int[] getTileCoords(Tile tile)
	{
		for(int i=0;i<m_classroom.size();++i)
		{
			for(int j=0;j<m_classroom.get(i).size();++j)
			{
				if(m_classroom.get(i).get(j) == tile)
				{
					return new int[]{i, j};
				}
			}
		}
		
		return null;
	}
	
	public ArrayList<ArrayList<Tile>> getTiles()
	{
		return m_classroom;
	}
	
	public ArrayList<ArrayList<Student>> getStudents()
	{
		return m_students;
	}
	
	public ArrayList<ArrayList<Mistress>> getMistresses()
	{
		return m_mistresses;
	}
	
	public ArrayList<ArrayList<Character>> getCharacters()
	{
		ArrayList<ArrayList<Character>> arrayRes = new ArrayList<>();
		
		for(int i=0;i<m_students.size();++i)
		{
			arrayRes.add(new ArrayList<>());
			
			for(int j=0;j<m_students.get(i).size();++j)
			{
				arrayRes.get(i).add((Character)m_students.get(i).get(j));
			}
		}
		
		for(int i=0;i<m_mistresses.size();++i)
		{
			for(int j=0;j<m_mistresses.get(i).size();++j)
			{
				if(arrayRes.get(i).get(j) == null)
				{
					arrayRes.get(i).set(j, (Character)m_mistresses.get(i).get(j));
				}
			}
		}
		
		return arrayRes;
	}
	
	public ArrayList<Pair<Candy, int[]>> getCandies()
	{
		ArrayList<Pair<Candy, int[]>> arrayRes = new ArrayList<>();
		
		for(int i=0;i<m_classroom.size();++i)
		{
			for(int j=0;j<m_classroom.get(i).size();++j)
			{
				if(m_classroom.get(i).get(j) instanceof Candy)
				{
					arrayRes.add(new Pair<Candy, int[]>((Candy)m_classroom.get(i).get(j), new int[]{i, j}));
				}
			}
		}
		
		return arrayRes;
	}

	public int getStudentsCount()
	{
		int studentsCounter = 0;

		for(int i=0;i<m_students.size();++i)
		{
			for(int j=0;j<m_students.get(i).size();++j)
			{
				if(m_students.get(i).get(j) != null)
				{
					++studentsCounter;
				}
			}
		}

		return studentsCounter;
	}
	
	public synchronized void charPosChanged(/*ArrayList<ArrayList<Character>> charInClass, */ Character character, int oldX, int oldY, int newX, int newY)
	{
		/*boolean isStudent = (m_students.get(oldX).get(oldY) != null);// && m_students.get(oldX).get(oldY) instanceof Student;
		boolean isMistress = (m_mistresses.get(oldX).get(oldY) != null);*/
		
		m_lockUpdatePosition.lock();
		try
		{
			Student student = character instanceof Student ? (Student)character : null;
			Mistress mistress = character instanceof Mistress ? (Mistress)character : null;
			
			if(student != null)
			{
				m_students.get(oldX).set(oldY, null);
				m_students.get(newX).set(newY, student);
			}
			else if(mistress != null)
			{
				m_mistresses.get(oldX).set(oldY, null);
				m_mistresses.get(newX).set(newY, mistress);
			}

			if(student != null)
			{
				System.out.println("Students counter : " + getStudentsCount());
			}
		}
		finally
		{
			m_lockUpdatePosition.unlock();
		}
	}
	
	public void moveFirstStudent(int a, int b)
	{
		Student student = null;
		Mistress mistress = null;
		
		/*int c = 0;
		int d = 0;*/
		
		for(int i=0;i<m_students.size();++i)
		{
			for(int j=0;j<m_students.get(i).size();++j)
			{
				if(m_students.get(i).get(j) != null)
				{
					student = m_students.get(i).get(j);
					/*c = i;
					d = j;*/
					
					System.out.println(i + " " + j);
					
					break;
				}
			}
		}
		
		for(int i=0;i<m_mistresses.size();++i)
		{
			for(int j=0;j<m_mistresses.get(i).size();++j)
			{
				if(m_mistresses.get(i).get(j) != null)
				{
					mistress = m_mistresses.get(i).get(j);
				}
			}
		}
		
		//student.move(new Pair<Tile, int[]>((Tile)student.getChair(), new int[]{c, d}), new Pair<Tile, int[]>(m_classroom.get(a).get(b), new int[]{a, b}));
		student.tryToGoAtCandy();
		System.out.println(student.getCurrentPosition().getValue()[0] + ", " + student.getCurrentPosition().getValue()[1]);
		mistress.followStudent();
		//student.goToChair();
	}
	
	public void moveAllStudentToCandy()
	{
		ArrayList<Student> arrayStudents = new ArrayList<>();
		
		for(int i=0;i<m_students.size();++i)
		{
			for(int j=0;j<m_students.get(i).size();++j)
			{
				if(m_students.get(i).get(j) != null)
				{
					arrayStudents.add(m_students.get(i).get(j));
				}
			}
		}
		
		ArrayList<Thread> threads = new ArrayList<>();
		Thread t = null;

		try
		{
			for(int i=0;i<arrayStudents.size();++i)
			{
				threads.add(new Thread(arrayStudents.get(i)));
				
				threads.get(i).start();
			}
			
			for(int i=0;i<m_mistresses.size();++i)
			{
				for(int j=0;j<m_mistresses.get(i).size();++j)
				{
					if(m_mistresses.get(i).get(j) != null)
					{
						t = new Thread(m_mistresses.get(i).get(j));
					}
				}
			}

			t.start();

			for(int i=0;i<arrayStudents.size();++i)
			{
				threads.get(i).join();
			}
		}
		catch(InterruptedException err)
		{
			err.printStackTrace();
		}
	}
	
	public void moveMistress()
	{
		Mistress mistress = null;
		
		for(int i=0;i<m_mistresses.size();++i)
		{
			for(int j=0;j<m_mistresses.get(i).size();++j)
			{
				if(m_mistresses.get(i).get(j) != null)
				{
					mistress = m_mistresses.get(i).get(j);
				}
			}
		}
		
		int a = 0;
		int b = 0;
		
		mistress.move(mistress.getCurrentPosition(), new Pair<Tile, int[]>(m_classroom.get(a).get(b), new int[]{a, b}));
	}
	
	public void moveMistressToFirstStudent()
	{
		Student student = null;
		Mistress mistress = null;
		
		for(int i=0;i<m_students.size();++i)
		{
			for(int j=0;j<m_students.get(i).size();++j)
			{
				if(m_students.get(i).get(j) != null)
				{
					student = m_students.get(i).get(j);
					
					break;
				}
			}
		}
		
		for(int i=0;i<m_mistresses.size();++i)
		{
			for(int j=0;j<m_mistresses.get(i).size();++j)
			{
				if(m_mistresses.get(i).get(j) != null)
				{
					mistress = m_mistresses.get(i).get(j);
				}
			}
		}
		
		mistress.move(mistress.getCurrentPosition(), student.getCurrentPosition());
	}
	
	@Override
	protected synchronized void paintComponent(Graphics g)
	{
		m_lockRepaint.lock();
		
		try
		{
		super.paintComponent(g);
		 Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.WHITE); // Fond blanc
    g2d.fillRect(0, 0, getWidth(), getHeight()); // Efface le dessin précédent
		
		// Dessin classe.
		for(int i=0;i<m_classroom.size();++i)
		{
			for(int j=0;j<m_classroom.get(i).size();++j)
			{
				String currentSprite = m_classroom.get(i).get(j).getSprite();
				InputStream is = Common.getStreamFromResource(currentSprite);
				
				try
				{
					BufferedImage image = ImageIO.read(is);
					
					g.drawImage(image, j * m_tileSize, i * m_tileSize, m_tileSize, m_tileSize, this);
				}
				catch(IOException err)
				{
					err.printStackTrace();
				}
			}
		}
		
		// Dessin élèves.
		for(int i=0;i<m_students.size();++i)
		{
			for(int j=0;j<m_students.get(i).size();++j)
			{
				if(m_students.get(i).get(j) != null)
				{
					int xStudent = j * 48;
					int yStudent = i * 48;
					
					m_students.get(i).get(j).draw(g, xStudent, yStudent);
				}
			}
		}
		
		// Dessin maîtresses.
		for(int i=0;i<m_mistresses.size();++i)
		{
			for(int j=0;j<m_mistresses.get(i).size();++j)
			{
				if(m_mistresses.get(i).get(j) != null)
				{
					int xMistress = j * 48;
					int yMistress = i * 48;
					
					m_mistresses.get(i).get(j).draw(g, xMistress, yMistress);
				}
			}
		}
		}
		finally{m_lockRepaint.unlock();}
	}
	
	private boolean verifyOrientation(String type, String orientation)
	{
		boolean isOriented = (type.equals("studentChair") || type.equals("teacherChair"));
		boolean validOrientation = (orientation.equals("front") || orientation.equals("back") || orientation.equals("left") || orientation.equals("right"));
		
		return ((isOriented && validOrientation) || (!isOriented && orientation.equals("null")));
	}
}
