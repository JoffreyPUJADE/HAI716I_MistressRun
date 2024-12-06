package TilePack;

enum Orientation
{
	FRONT,
	BACK,
	LEFT,
	RIGHT,
	;
	
	@Override
	public String toString()
	{
		switch(this.ordinal())
		{
			case 0:
				return "FRONT";
			case 1:
				return "BACK";
			case 2:
				return "LEFT";
			case 3:
				return "RIGHT";
			default:
				return null;
		}
	}
}
