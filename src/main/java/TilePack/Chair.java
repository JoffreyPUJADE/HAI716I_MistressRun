package TilePack;

import CharacterPack.Character;

public abstract class Chair extends OrientedTile
{
	public Chair(String resourceName, String orientation, Character occupiedBy)
	{
		super(resourceName, orientation, false, occupiedBy);
	}
	
	public Chair(String resourceName, Orientation orientation, Character occupiedBy)
	{
		super(resourceName, orientation, false, occupiedBy);
		
		m_orientation = orientation;
	}
}
