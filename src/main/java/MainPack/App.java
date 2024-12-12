package MainPack;

public class App
{
	public static void main(String[] args)
	{
		System.out.println("Hello, world !");
		
		Game game = Game.getInstance();
		game.launch();
	}
}
