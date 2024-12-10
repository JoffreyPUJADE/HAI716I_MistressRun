package CharacterPack;

import TilePack.TeacherChair;
import TilePack.Tile;
import MainPack.Game;
import AlgoPack.Pair;
import GraphicsPack.Classroom;

import java.util.ArrayList;

public class Mistress extends Character implements Runnable
{
	private int m_touchedStudents;
	
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
	
	/*@Override
	public Pair<Tile, int[]> getCurrentPosition()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Mistress>> arrayMistresses = classroom.getMistresses();
		
		for(int i=0;i<arrayMistresses.size();++i)
		{
			for(int j=0;j<arrayMistresses.get(i).size();++j)
			{
				if(arrayMistresses.get(i).get(j) == this)
				{
					return new Pair<Tile, int[]>(arrayTiles.get(i).get(j), new int[]{i, j});
				}
			}
		}
		
		return null;
	}*/
	
	public ArrayList<Student> getStudentsAround()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Student>> arrayStudents = classroom.getStudents();
		Pair<Tile, int[]> currentPosition = getCurrentPosition();
		ArrayList<Student> studentsAround = new ArrayList<>();
		
		// Positions possibles (haut, bas, gauche, droite)
		int[] dx = {-1, 1, 0, 0};
		int[] dy = {0, 0, -1, 1};
		
		for(int i=0;i<4;++i)
		{
			int newX = currentPosition.getValue()[0] + dx[i];
			int newY = currentPosition.getValue()[1] + dy[i];
			
			if(isInBounds(newX, newY, classroom.getTiles()) && arrayStudents.get(newX).get(newY) != null)
			{
				studentsAround.add(arrayStudents.get(newX).get(newY));
			}
		}
		
		return studentsAround;
	}
	
	public ArrayList<Student> getEscapingStudents()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Student>> arrayStudents = classroom.getStudents();
		ArrayList<Student> arrayRes = new ArrayList<>();
		
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
		
		return arrayRes;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s ; TouchedStudents : %d", super.toString(), m_touchedStudents);
	}
	
	public boolean touchStudent(Student student)
	{
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
		student.goToChair();
		++m_touchedStudents;
		return true;
	}

	public void followStudent() {
    ArrayList<Student> escapingStudents = getEscapingStudents();
    
    for (Student escapingStudent : escapingStudents) {
        // Déplacer vers l'étudiant qui s'échappe
        boolean isOverStudent = move(getCurrentPosition(), escapingStudent.getCurrentPosition());

        if (isOverStudent) {
            // Si on est au-dessus d'un étudiant qui s'échappe, on le touche
            touchStudent(escapingStudent);
        } else {
            // Vérifier les étudiants à proximité
            ArrayList<Student> nearbyStudents = getStudentsAround();
            boolean touchedNearby = false;
            
            for (Student nearbyStudent : nearbyStudents) {
                if (nearbyStudent.isEscaping()) {
                    touchStudent(nearbyStudent);
                    touchedNearby = true;
                    break; // On arrête dès qu'on touche un étudiant
                }
            }

            if (!touchedNearby) {
                System.out.println("really no way");
            }
        }
    }
}

	
	@Override
	protected boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass, int x, int y, Character currentChar)
	{
		return map.get(x).get(y).isObstacle();
	}

	public void getStudent()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();

		classroom.getStudents();

	}

	public void run()
	{
		/*for(;;)
		{

			followStudent();

		}*/
		while(true)
		followStudent();
	}
	


}
