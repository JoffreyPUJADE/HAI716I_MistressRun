package TilePack;

public class Candy extends Tile
{
	public Candy(int numSprite)
	{
		super(String.format("candies/candy%d.png", numSprite), false, null);
	}
}
