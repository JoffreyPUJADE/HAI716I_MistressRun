package CharacterPack;

import AlgoPack.Common;
import AlgoPack.Node;
import AlgoPack.Pair;
import TilePack.Chair;
import TilePack.Tile;
import MainPack.Game;
import MovementPack.AStarMoveStrategy;
import GraphicsPack.Classroom;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.io.InputStream;
import java.io.IOException;

import MovementPack.AstarForMistress;
import MovementPack.AvoidMoveStrategy;
import MovementPack.Longestpath;
import java.util.Random;


public abstract class Character
{
    private  int m_index; 
    private String m_spriteSheet;
    private BufferedImage[] m_walkUpSprites;
    private BufferedImage[] m_walkDownSprites;
    private BufferedImage[] m_walkLeftSprites;
    private BufferedImage[] m_walkRightSprites;
    private int m_currentSpriteIndex;
    private String m_direction;
    protected Chair m_chair;
    protected int m_moveSleepDuration;

    private static boolean  mistressGotochair = true;
    private static int s_globalIndex = 0;
    static private Map<Character, Pair<Tile, int[]>> m_positions = new HashMap<>();

    public Character(String spriteSheet, Chair chair, int i, int j)
    {
        m_index = s_globalIndex++; 
        m_spriteSheet = spriteSheet;
        m_chair = chair;
        m_positions.put(this, new Pair<>((Tile) m_chair, new int[] { i, j }));

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

		if(m_index==2)
        {
			m_moveSleepDuration = 200;
		}
        else
        {
            m_moveSleepDuration = 500;
        }
    }

    public int getIndex()
    {
        return m_index;
    }

    public Chair getChair()
    {
        return m_chair;
    }

	public boolean getMistresses()
    {
        return mistressGotochair;
    }

    public Pair<Tile, int[]> getCurrentPosition()
    {
        return m_positions.get(this);
    }

    public void setDirection(String direction)
    {
        m_direction = direction;
    }

    public abstract boolean isTouched();

    public abstract boolean isEscaping();

    public boolean isAtChair()
    {
        Game game = Game.getInstance();
        Classroom classroom = game.getClassroom();

        int[] currentPosition = getCurrentPosition().getValue();
        int[] chairPosition = classroom.getTileCoords((Tile) m_chair);

        return ((currentPosition[0] == chairPosition[0]) && (currentPosition[1] == chairPosition[1]));
    }

    public boolean goToChair()
    {
		if(m_index==0)
        {
			mistressGotochair=true;
		}

        Game game = Game.getInstance();
        Classroom classroom = game.getClassroom();

        Pair<Tile, int[]> currentPosition = getCurrentPosition();
        int[] chairPosition = classroom.getTileCoords((Tile) m_chair);

        return move(currentPosition, new Pair<Tile, int[]>((Tile) m_chair, chairPosition));
    }

    public boolean move(Pair<Tile, int[]> currentTile, Pair<Tile, int[]> targetTile)
    {
        boolean objectifReached = false;

        Game game = Game.getInstance();
        Classroom classroom = game.getClassroom();
        ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
        ArrayList<ArrayList<Character>> charInClass = classroom.getCharacters();
		List<Node> path = new ArrayList<>();
		Random random = new Random();
				
		switch(m_index)
        {
			case 0:
				mistressGotochair = false;
				path = AstarForMistress.findPath(currentTile, targetTile, arrayTiles, charInClass, m_index);
				break;
            
			case 1:
				path = AvoidMoveStrategy.findPath(currentTile, targetTile, arrayTiles, charInClass, m_index);
				break;
            
			case 2:
				path = Longestpath.findPath(currentTile, targetTile, arrayTiles, charInClass, m_index);
				break;
            
			case 3:
				path = AStarMoveStrategy.findPath(currentTile, targetTile, arrayTiles, charInClass, m_index);
				break;
            
			case 4:
				path = AStarMoveStrategy.findPath(currentTile, targetTile, arrayTiles, charInClass, m_index);
				break;
            
			default:
			break;
		}

        if(!path.isEmpty())
        {
            objectifReached = moveAlongPath(path, currentTile);
        }

        return objectifReached;
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

    @Override
    public String toString()
    {
        Pair<Tile, int[]> currentPosition = getCurrentPosition();
        return String.format("%s => Index: %d; SpriteSheet : %s ; CurrentSpriteIndex : %d ; Direction : %s ; Chair : %s ; CurrentPosition : [%d, %d]", this.getClass().getSimpleName(), m_index, m_spriteSheet, m_currentSpriteIndex, m_direction, m_chair.toString(), currentPosition.getValue()[0], currentPosition.getValue()[1]);
    }

    private boolean moveAlongPath(List<Node> path, Pair<Tile, int[]> currentPosition)
    {
        Game game = Game.getInstance();
        Classroom classroom = game.getClassroom();
        ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
        ArrayList<ArrayList<Character>> charInClass = classroom.getCharacters();

        for(Node node : path)
        {
            if(isTouched() && isEscaping())
            {
                return false;
            }

            Tile tile = arrayTiles.get(node.getX()).get(node.getY());

            if(!tile.isObstacle() && charInClass.get(node.getX()).get(node.getY()) == null)
            {
                int oldX = currentPosition.getValue()[0];
                int oldY = currentPosition.getValue()[1];
                int newX = node.getX();
                int newY = node.getY();

                Tile currentTile = currentPosition.getKey();
                currentTile.takeTile(null);
                charInClass.get(oldX).set(oldY, null);

                currentPosition.setValue(new int[] { node.getX(), node.getY() });
                tile.takeTile(this);

                classroom.charPosChanged(this, oldX, oldY, newX, newY);
                updatePosition(this, tile, new int[] { newX, newY });
                classroom.repaint();

                try
                {
                    TimeUnit.MILLISECONDS.sleep(m_moveSleepDuration);
                }
                catch(InterruptedException err)
                {
                    Thread.currentThread().interrupt();
                }

                if(node.getX() == path.get(path.size() - 1).getX() && node.getY() == path.get(path.size() - 1).getY())
                {
                    return true;
                }
            }
        }

        return false;
    }

    private void loadSprite(BufferedImage spriteSheet)
    {
        m_walkUpSprites = new BufferedImage[3];
        m_walkDownSprites = new BufferedImage[3];
        m_walkLeftSprites = new BufferedImage[3];
        m_walkRightSprites = new BufferedImage[3];

        for(int i = 0; i < 3; ++i)
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

        for(int x = 0; x < width; ++x)
        {
            for(int y = 0; y < height; ++y)
            {
                flipped.setRGB(width - 1 - x, y, original.getRGB(x, y));
            }
        }

        return flipped;
    }

    static private void updatePosition(Character character, Tile tile, int[] position)
    {
        m_positions.put(character, new Pair<>(tile, position));
    }

    public boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass, int x, int y)
    {
        return map.get(x).get(y).isObstacle() || charInClass.get(x).get(y) != null;
    }

    public static boolean isInBounds(int x, int y, ArrayList<ArrayList<Tile>> map)
    {
        return x >= 0 && x < map.size() && y >= 0 && y < map.get(x).size();
    }
}
