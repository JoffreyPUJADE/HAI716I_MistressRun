package CharacterPack;

import AlgoPack.Common;
import AlgoPack.Node;
import AlgoPack.Pair;
import TilePack.Chair;
import TilePack.Tile;
import MainPack.Game;
import GraphicsPack.Classroom;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.io.InputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

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
	//protected Tile m_currentTile;
	
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
	
	public abstract Pair<Tile, int[]> getCurrentPosition();
	
	public void setDirection(String direction)
	{
		m_direction = direction;
	}
	
	public boolean goToChair()
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		
		Pair<Tile, int[]> currentPosition = getCurrentPosition();
		int[] chairPosition = classroom.getTileCoords((Tile)m_chair);
		
		return move(currentPosition, new Pair<Tile, int[]>((Tile)m_chair, chairPosition));
	}
	
	public boolean move(Pair<Tile, int[]> currentTile, Pair<Tile, int[]> targetTile)
	{
		boolean objectifReached = false;
		
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Character>> charInClass = classroom.getCharacters();
		
		Tile currentT = currentTile.getKey();
		int[] currentPosition = currentTile.getValue();
		
		Tile targetT = targetTile.getKey();
		int[] targetPosition = targetTile.getValue();
		
		List<Node> openList = new ArrayList<>();
		Set<Node> closedList = new HashSet<>();
		Map<Node, Node> cameFrom = new HashMap<>();
		
		Node startNode = new Node(currentPosition[0], currentPosition[1], null, 0, calculateHCost(currentPosition, targetPosition));
		openList.add(startNode);
		
		while(!openList.isEmpty())
		{
			Node currentNode = openList.stream()
						   .min(Comparator.comparingInt(node -> node.getFCost()))
						   .orElseThrow(() -> new NoSuchElementException("Aucun noeud trouvé dans la liste ouverte"));
			
			openList.remove(currentNode);
			closedList.add(currentNode);
			
			if(currentNode.getX() == targetPosition[0] && currentNode.getY() == targetPosition[1])
			{
				List<Node> path = reconstructPath(cameFrom, currentNode);
				objectifReached = moveAlongPath(path, currentTile);
				
				break;
			}
			
			List<Node> neighbors = getNeighbors(currentNode, arrayTiles, charInClass, targetPosition, this);
			
			for(Node neighbor : neighbors)
			{
				if(closedList.contains(neighbor))
				{
					continue;
				}
				
				int tentativeGCost = currentNode.getGCost() + 1;  // Supposons que le coût pour chaque mouvement est de 1
				
				// Si ce nœud est dans la liste ouverte mais avec un coût plus élevé, on ignore ce voisin
				if(!openList.contains(neighbor) || tentativeGCost < neighbor.getGCost())
				{
					cameFrom.put(neighbor, currentNode);  // Enregistrer le parent du voisin
					neighbor.setGCost(tentativeGCost);
					neighbor.setFCost(neighbor.getGCost() + neighbor.getHCost());
					
					if(!openList.contains(neighbor))
					{
						openList.add(neighbor);
					}
				}
			}
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
		return String.format("%s => SpriteSheet : %s ; CurrentSpriteIndex : %d ; Direction : %s ; Chair : %s", this.getClass().getSimpleName(), m_spriteSheet, m_currentSpriteIndex, m_direction, m_chair.toString());
	}
	
	private int calculateHCost(int[] currentPosition, int[] targetPosition)
	{
		return Math.abs(currentPosition[0] - targetPosition[0]) + Math.abs(currentPosition[1] - targetPosition[1]);
	}
	
	private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node currentNode)
	{
		List<Node> path = new ArrayList<>();
		
		while(currentNode != null)
		{
			path.add(currentNode);
			currentNode = cameFrom.get(currentNode);
		}
		
		Collections.reverse(path);
		
		return path;
	}
	
	private boolean moveAlongPath(List<Node> path, Pair<Tile, int[]> currentPosition)
	{
		Game game = Game.getInstance();
		Classroom classroom = game.getClassroom();
		ArrayList<ArrayList<Tile>> arrayTiles = classroom.getTiles();
		ArrayList<ArrayList<Character>> charInClass = classroom.getCharacters();
		
		for(Node node : path)
		{
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
				
				currentPosition.setValue(new int[]{node.getX(), node.getY()});
				tile.takeTile(this);
				charInClass.get(newX).set(newY, this);
				
				classroom.charPosChanged(charInClass, oldX, oldY, newX, newY);
				classroom.repaint();
				
				try
				{
					TimeUnit.MILLISECONDS.sleep(500);
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
	
	private boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass, int x, int y, Character currentChar)
	{
		return map.get(x).get(y).isObstacle() || (charInClass.get(x).get(y) != null && charInClass.get(x).get(y) != currentChar);
	}
	
	private boolean isInBounds(int x, int y, ArrayList<ArrayList<Tile>> map)
	{
		return x >= 0 && x < map.size() && y >= 0 && y < map.get(x).size();
	}
	
	private List<Node> getNeighbors(Node node, ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass, int[] targetPosition, Character currentChar)
	{
		List<Node> neighbors = new ArrayList<>();
		
		// Déplacements possibles (haut, bas, gauche, droite)
		int[] dx = {-1, 1, 0, 0};
		int[] dy = {0, 0, -1, 1};
		
		for(int i = 0; i < 4; i++)
		{
			int newX = node.getX() + dx[i];
			int newY = node.getY() + dy[i];
			
			if (isInBounds(newX, newY, map) && !isObstacle(map, charInClass, newX, newY, currentChar))
			{
				int hCost = calculateHCost(new int[]{newX, newY}, targetPosition);
				neighbors.add(new Node(newX, newY, node, node.getGCost() + 1, hCost));
			}
		}
		
		return neighbors;
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