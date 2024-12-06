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
	
	public Mistress(TeacherChair chair)
	{
		super("sprites/mistress.png", chair);
		
		m_touchedStudents = 0;
	}
	
	public int getTouchedStudents()
	{
		return m_touchedStudents;
	}
	
	@Override
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
	}
	
	@Override
	public String toString()
	{
		return String.format("%s ; TouchedStudents : %d", super.toString(), m_touchedStudents);
	}

	public void followStudent()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();

		ArrayList<ArrayList<Student>> listOfStudents = classroom.getStudents();

		Pair<Tile, int[]> current = getCurrentPosition();


		
		for(int i = 0; i < listOfStudents.size(); i++)
		{
			for (int x = 0; x < listOfStudents.get(x).size(); x++)
			{
				if(listOfStudents.get(i).get(x).getCurrentPosition().getValue() != classroom.getTileCoords(listOfStudents.get(i).get(x).getChair()) )
				{
					//Pair<Tile, int[]> target = listOfStudents[i].getCurrentPosition();
					move(current, listOfStudents.get(i).get(x).getCurrentPosition());

				}

			}

		}

		//if the position of the student and the student chair are not equal then
		//move towards the student.
		

	}

	public void getStudent()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();

		classroom.getStudents();

	}

	public void run()
	{
		for(;;)
		{

			followStudent();

		}
	}
	


}
