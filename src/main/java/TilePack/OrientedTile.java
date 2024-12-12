package TilePack;

import CharacterPack.Character;

public abstract class OrientedTile extends NeedBackground
{
    protected Orientation m_orientation;
    private String m_resourceName;
	
	public OrientedTile(String resourceName, String orientation, boolean isObstacle, Character occupiedBy)
	{
		super(getFullResourceName(resourceName, strToEnum(orientation)), "tiles/floor.png", isObstacle, occupiedBy);
		
		m_orientation = strToEnum(orientation);
        m_resourceName = resourceName;
	}
	
	public OrientedTile(String resourceName, Orientation orientation, boolean isObstacle, Character occupiedBy)
	{
		super(getFullResourceName(resourceName, orientation), "tiles/floor.png", isObstacle, occupiedBy);
		
		m_orientation = orientation;
        m_resourceName = resourceName;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s ; Orientation : %s", super.toString(), m_orientation.toString());
	}
	
	static private Orientation strToEnum(String str)
	{
		switch(str)
		{
			case "front":
			case "FRONT":
				return Orientation.FRONT;
			
			case "back":
			case "BACK":
				return Orientation.BACK;
			
			case "left":
			case "LEFT":
				return Orientation.LEFT;
			
			case "right":
			case "RIGHT":
				return Orientation.RIGHT;
			
			default:
				return null;
			
		}
	}
	
	static private String getFullResourceName(String resourceName, Orientation orientation)
	{
		String fullName = resourceName.split("\\.")[0]; // Beginning of the resource name.
		
		switch(orientation)
		{
			case FRONT:
				fullName = fullName.concat("Front");
			break;
			
			case BACK:
				fullName = fullName.concat("Back");
			break;
			
			case LEFT:
				fullName = fullName.concat("Left");
			break;
			
			case RIGHT:
				fullName = fullName.concat("Right");
			break;
			
			default:
				fullName = "tiles/void";
			break;
		}
		
		return fullName.concat(".png");
	}

    protected void orientationUpdated()
    {
        setSprite(getFullResourceName(m_resourceName, m_orientation));
    }
}
