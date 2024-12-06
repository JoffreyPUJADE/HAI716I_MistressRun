package AlgoPack;

import java.util.Objects;

public class Node
{
	int m_x;
	int m_y;
	Node m_parent;
	int m_gCost;
	int m_hCost;
	int m_fCost;
	
	public Node(int x, int y, Node parent, int gCost, int hCost)
	{
		m_x = x;
		m_y = y;
		m_parent = parent;
		m_gCost = gCost;
		m_hCost = hCost;
		m_fCost = m_gCost + m_hCost;
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public int getY()
	{
		return m_y;
	}
	
	public int getGCost()
	{
		return m_gCost;
	}
	
	public int getHCost()
	{
		return m_hCost;
	}
	
	public int getFCost()
	{
		return m_fCost;
	}
	
	public void setX(int x)
	{
		m_x = x;
	}
	
	public void setY(int y)
	{
		m_y = y;
	}
	
	public void setGCost(int gCost)
	{
		m_gCost = gCost;
	}
	
	public void setFCost(int fCost)
	{
		m_fCost = fCost;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		
		Node node = (Node)obj;
		
		return m_x == node.getX() && m_y == node.getY();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(m_x, m_y);
	}
}
