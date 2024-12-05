package TilePack;

import AlgoPack.Common;

import java.io.InputStream;

public abstract class Tile
{
	protected String m_sprite;
	protected InputStream m_spriteStream;
	
	public Tile(String resourceName)
	{
		m_sprite = resourceName;
		m_spriteStream = Common.getStreamFromResource(m_sprite);
	}
	
	public String getSprite()
	{
		return m_sprite;
	}
}
