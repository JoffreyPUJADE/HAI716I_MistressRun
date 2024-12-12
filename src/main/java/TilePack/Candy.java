package TilePack;

public class Candy extends NeedBackground
{
	public Candy(int numSprite)
	{
		super(String.format("candies/candy%d.png", numSprite), "tiles/floor.png", false, null);
	}
}
