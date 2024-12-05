package CharacterPack;

import AlgoPack.Common;
import TilePack.Chair;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

public abstract class Character
{
	private String m_spriteSheet;
	private BufferedImage[] m_walkUpSprites;
	private BufferedImage[] m_walkDownSprites;
	private BufferedImage[] m_walkLeftSprites;
	private BufferedImage[] m_walkRightSprites;
	private int m_currentSpriteIndex;
	private String m_direction;
	protected Chair m_chair;
	
	public Character(String spriteSheet, Chair chair)
	{
		m_spriteSheet = spriteSheet;
		m_chair = chair;
		
		InputStream is = Common.getStreamFromResource(m_spriteSheet);
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(is);
		}
		catch(IOException err)
		{
			err.printStackTrace();
		}
		
		loadSprite(image);
		
		m_currentSpriteIndex = 0;
		m_direction = "down";
	}
	
	public Chair getChair()
	{
		return m_chair;
	}
	
	public void setDirection(String direction)
	{
		m_direction = direction;
	}
	
	public void updateAnimation()
	{
		m_currentSpriteIndex = (m_currentSpriteIndex + 1) % 3;
	}
	
	public void draw(Graphics g, int x, int y)
	{
		BufferedImage currentSprite = null;
		
		switch(m_direction)
		{
			case "down":
				currentSprite = m_walkDownSprites[m_currentSpriteIndex];
			break;
			
			case "up":
				currentSprite = m_walkUpSprites[m_currentSpriteIndex];
			break;
			
			case "left":
				currentSprite = m_walkLeftSprites[m_currentSpriteIndex];
			break;
			
			case "right":
				currentSprite = m_walkRightSprites[m_currentSpriteIndex];
			break;
		}
		
		g.drawImage(currentSprite, x, y, null);
	}
	
	private void loadSprite(BufferedImage spriteSheet)
	{
		m_walkUpSprites = new BufferedImage[3];
		m_walkDownSprites = new BufferedImage[3];
		m_walkLeftSprites = new BufferedImage[3];
		m_walkRightSprites = new BufferedImage[3];
		
		for(int i=0;i<3;++i)
		{
			m_walkUpSprites[i] = spriteSheet.getSubimage(i * 16, 16, 16, 16);
			m_walkDownSprites[i] = spriteSheet.getSubimage(i * 16, 0, 16, 16);
			m_walkLeftSprites[i] = spriteSheet.getSubimage(i * 16, 32, 16, 16);
			m_walkRightSprites[i] = flipImage(m_walkLeftSprites[i]);
		}
	}
	
	private BufferedImage flipImage(BufferedImage original)
	{
		int width = original.getWidth();
		int height = original.getHeight();
		BufferedImage flipped = new BufferedImage(width, height, original.getType());
		
		for(int x=0;x<width;++x)
		{
			for(int y=0;y<height;++y)
			{
				flipped.setRGB(width - 1 - x, y, original.getRGB(x, y));
			}
		}
		
		return flipped;
	}
}
