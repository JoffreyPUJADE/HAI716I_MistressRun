package CharacterPack;

import TilePack.TeacherChair;
import TilePack.Tile;
import MainPack.Game;
import AlgoPack.Pair;
import GraphicsPack.Classroom;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Mistress extends Character implements Runnable
{
	private int m_touchedStudents;
	private final ReentrantLock m_lock = new ReentrantLock();
	
	public Mistress(TeacherChair chair, int i, int j)
	{
		super("sprites/mistress.png", chair, i, j);
		
		m_touchedStudents = 0;
		m_moveSleepDuration = 150;
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

			// Possible positions (up, down, left, right).
			int[] dx = {-1, 1, 0, 0};
			int[] dy = {0, 0, -1, 1};

			for(int i=0;i<4;++i)
			{
				int newX = currentPosition.getValue()[0] + dx[i];
				int newY = currentPosition.getValue()[1] + dy[i];

				if(isInBounds(newX, newY, arrayTiles) && arrayStudents.get(newX).get(newY) != null)
				{
					Student student = arrayStudents.get(newX).get(newY);

					// Check that the student hasn't already been hit.
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
	public boolean isTouched() // A teacher cannot be touched.
	{
		return false;
	}

	@Override
	public boolean isEscaping() // A teacher cannot be "escaping".
	{
		return false;
	}

	public void followStudent()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Student>> students = classroom.getStudents();
		ArrayList<Student> escapingStudents = getEscapingStudents(students);
		
		for(Student escapingStudent : escapingStudents)
		{
			// Moving towards the escaping student.
			move(getCurrentPosition(), escapingStudent.getCurrentPosition());

			// Checking nearby students.
			ArrayList<Student> nearbyStudents = getStudentsAround(students, arrayTiles);
			boolean touchedNearby = false;

			for(Student nearbyStudent : nearbyStudents)
			{
				if(nearbyStudent.isEscaping() && !nearbyStudent.isTouched())
				{
					nearbyStudent.touched();
					touchedNearby = true;
					break; // We stop as soon as we touch a student.
				}
			}

			if(!touchedNearby)
			{
				System.out.println("really no way");
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
	protected boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass, int x, int y, Character currentChar)
	{
		return map.get(x).get(y).isObstacle();
	}
}