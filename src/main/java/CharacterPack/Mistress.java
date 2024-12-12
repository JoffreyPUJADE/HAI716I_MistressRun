package CharacterPack;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import AlgoPack.Pair;
import GraphicsPack.Classroom;
import GraphicsPack.MistressInfo;
import MainPack.Game;
import TilePack.TeacherChair;
import TilePack.Tile;

public class Mistress extends Character implements Runnable
{
	private boolean m_returningToChair;
	private int m_touchedStudents;
	private final ReentrantLock m_lock = new ReentrantLock();
	
	public Mistress(TeacherChair chair, int i, int j)
	{
		super("sprites/mistress.png", chair, i, j);
		
		m_touchedStudents = 0;
		m_moveSleepDuration = 200;
	}
	
	public int getTouchedStudents()
	{
		return m_touchedStudents;
	}
	
	public ArrayList<Student> getStudentsAround(ArrayList<ArrayList<Student>> arrayStudents, ArrayList<ArrayList<Tile>> arrayTiles)
	{
		m_lock.lock();
		ArrayList<Student> studentsAround = new ArrayList<>();

		try
		{
			Pair<Tile, int[]> currentPosition = getCurrentPosition();
		
			int[] dx = {-1, 1, 0, 0};
			int[] dy = {0, 0, -1, 1};

			for(int i=0;i<4;++i)
			{
				int newX = currentPosition.getValue()[0] + dx[i];
				int newY = currentPosition.getValue()[1] + dy[i];

				if(isInBounds(newX, newY, arrayTiles) && arrayStudents.get(newX).get(newY) != null)
				{
					Student student = arrayStudents.get(newX).get(newY);


					if(!student.isTouched())
					{
						studentsAround.add(student);
					}
				}
			}
		}
		finally
		{
			m_lock.unlock();
		}

		return studentsAround;
	}
	
	public ArrayList<Student> getEscapingStudents(ArrayList<ArrayList<Student>> arrayStudents)
	{
		m_lock.lock();
		ArrayList<Student> arrayRes = new ArrayList<>();

		try
		{
			for(int i=0;i<arrayStudents.size();++i)
			{
				for(int j=0;j<arrayStudents.get(i).size();++j)
				{
					if(arrayStudents.get(i).get(j) != null)
					{
						if(arrayStudents.get(i).get(j).isEscaping())
						{
							arrayRes.add(arrayStudents.get(i).get(j));
						}
					}
				}
			}
		}
		finally
		{
			m_lock.unlock();
		}
		
		return arrayRes;
	}

	@Override
	public boolean isTouched() 
	{
		return false;
	}

	@Override
	public boolean isEscaping()
	{
		return false;
	}

	@Override
	public boolean goToChair()
	{
		boolean result = !super.goToChair(); 
		m_returningToChair = false; 
		return result;
	}

	public void followStudent()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Student>> students = classroom.getStudents();
		ArrayList<Student> escapingStudents = getEscapingStudents(students);

		if(escapingStudents.isEmpty())
		{
			goToChair();
		}
	
		for(Student escapingStudent : escapingStudents)
		{
			if(escapingStudent == null)
			{
				continue; 
			}
	
			move(getCurrentPosition(), escapingStudent.getCurrentPosition());
	
			ArrayList<Student> nearbyStudents = getStudentsAround(students, arrayTiles);
                        
			boolean touchedNearby = false;
	
			for(Student nearbyStudent : nearbyStudents)
			{
				if(nearbyStudent.isEscaping() && !nearbyStudent.isTouched())
				{
					nearbyStudent.touched();
					touchedNearby = true;
					aStudentIsTouched();
					break; 
				}
			}

		}
	}
	
	
	@Override
	public String toString()
	{
		return String.format("%s ; TouchedStudents : %d", super.toString(), m_touchedStudents);
	}

	public void run()
	{
		while(true)
		{ 
			followStudent();
		}
			
	}

	@Override
	public boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass, int x, int y)
	{
		return map.get(x).get(y).isObstacle();
	}

	private void aStudentIsTouched()
	{
		++m_touchedStudents;

		MistressInfo mi = (MistressInfo)Game.getInstance().getCharInfo(this);
		System.out.println(m_touchedStudents);
		mi.updateTouchedStudents(m_touchedStudents);
	}
}