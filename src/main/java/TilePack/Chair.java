package TilePack;

public abstract class Chair extends Tile
{
	protected Orientation m_orientation;
	
	public Chair(String resourceName, String orientation)
	{
		super(getFullResourceName(resourceName, strToEnum(orientation)), false);
		
		m_orientation = strToEnum(orientation);
	}
	
	public Chair(String resourceName, Orientation orientation)
	{
		super(getFullResourceName(resourceName, orientation), false);
		
		m_orientation = orientation;
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
}
