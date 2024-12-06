package TilePack;

import CharacterPack.Character;

public class Floor extends Tile
{
	public Floor(Character occupiedBy)
	{
		super("tiles/floor.png", false, occupiedBy);
	}
}
