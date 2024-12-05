package TilePack;

import AlgoPack.Common;

import java.io.InputStream;

public abstract class Tile
{
	protected String m_sprite;
	protected boolean m_isObstacle;
	protected InputStream m_spriteStream;
	
	public Tile(String resourceName, boolean isObstacle)
	{
		m_sprite = resourceName;
		m_isObstacle = isObstacle;
		
		m_spriteStream = Common.getStreamFromResource(m_sprite);
	}
	
	public String getSprite()
	{
		return m_sprite;
	}
	
	public boolean isObstacle()
	{
		return m_isObstacle;
	}
}
