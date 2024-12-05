package GraphicsPack;

import LoaderDataPack.ClassroomData;
import LoaderDataPack.TileData;
import LoaderDataPack.ClassroomLoader;
import AlgoPack.Common;
import TilePack.Tile;
import TilePack.Floor;
import TilePack.Board;
import TilePack.Desk;
import TilePack.StudentChair;
import TilePack.TeacherChair;
import CharacterPack.Student;
import CharacterPack.Mistress;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.InputStream;
import java.util.ArrayList;
import java.io.IOException;

public class Classroom extends JPanel
{
	private ArrayList<ArrayList<Tile>> m_classroom;
	private ArrayList<ArrayList<Student>> m_students;
	private ArrayList<ArrayList<Mistress>> m_mistresses;
	private int m_tileSize;
	
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
						m_classroom.get(i).add(new Floor());
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
					
					case "studentChair":
						m_classroom.get(i).add(new StudentChair(orientation));
						m_students.get(i).add(new Student((StudentChair)m_classroom.get(i).get(j)));
						m_mistresses.get(i).add(null);
					break;
					
					case "teacherChair":
						m_classroom.get(i).add(new TeacherChair(orientation));
						m_students.get(i).add(null);
						m_mistresses.get(i).add(new Mistress((TeacherChair)m_classroom.get(i).get(j)));
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
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
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
	
	private boolean verifyOrientation(String type, String orientation)
	{
		boolean isOriented = (type.equals("studentChair") || type.equals("teacherChair"));
		boolean validOrientation = (orientation.equals("front") || orientation.equals("back") || orientation.equals("left") || orientation.equals("right"));
		
		return ((isOriented && validOrientation) || (!isOriented && orientation.equals("null")));
	}
}
