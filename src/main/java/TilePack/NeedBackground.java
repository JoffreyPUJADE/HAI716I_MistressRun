package TilePack;

import CharacterPack.Character;

public abstract class NeedBackground extends Tile
{
    private String m_spriteBackground;

    public NeedBackground(String resourceName, String spriteBackground, boolean isObstacle, Character occupiedBy)
    {
        super(resourceName, isObstacle, occupiedBy);

        m_spriteBackground = spriteBackground;
    }

    public String getBackground()
    {
        return m_spriteBackground;
    }
}