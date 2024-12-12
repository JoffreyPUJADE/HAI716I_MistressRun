package TilePack;

import java.io.InputStream;

import AlgoPack.Common;
import CharacterPack.Character;

public abstract class Tile
{
	protected String m_sprite;
	protected boolean m_isObstacle;
	protected Character m_occupiedBy;
	protected InputStream m_spriteStream;
	
	public Tile(String resourceName, boolean isObstacle, Character occupiedBy)
	{
		m_sprite = resourceName;
		m_isObstacle = isObstacle;
		m_occupiedBy = occupiedBy;
		
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
	
	public Character occupiedBy()
	{
		return m_occupiedBy;
	}
	
	public void takeTile(Character character)
	{
		m_occupiedBy = character;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s => Sprite : %s ; IsObstacle : %b ; OccupiedBy : %s", this.getClass().getSimpleName(), m_sprite, m_isObstacle, m_occupiedBy == null ? "null" : "someone");
	}
}
