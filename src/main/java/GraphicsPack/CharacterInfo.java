package GraphicsPack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JPanel;

public abstract class CharacterInfo extends JPanel
{
    private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;
    private Color m_color;
    private String m_strategy;
    private String m_scoreName;
    protected int m_score;

    public CharacterInfo(int x, int y, int width, int height, Color color, String strategy, String scoreName, int score)
    {
        super();
        setLayout(null);

        m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;

        updateBounds();

        m_color = color;
        m_strategy = strategy;
        m_scoreName = scoreName;
        m_score = score;

        setVisible(true);
    }

    public int getPosX()
    {
		return m_x;
	}
	
	public int getPosY()
    {
		return m_y;
	}
	
	public int getWidth()
    {
		return m_width;
	}
	
	public int getHeight()
    {
		return m_height;
	}

    public String getStrategy()
    {
        return m_strategy;
    }
	
	public void setPosX(int x)
    {
		m_x = x;
        updateBounds();
	}
	
	public void setPosY(int y)
    {
		m_y = y;
        updateBounds();
	}
	
	public void setWidth(int width)
    {
		m_width = width;
        updateBounds();
	}
	
	public void setHeight(int height)
    {
		m_height = height;
        updateBounds();
	}

    public void setStrategy(String strategy)
    {
        m_strategy = strategy;
        redraw();
    }

    public void redraw()
    {
		setVisible(true);
		revalidate();
		repaint();
	}

    private void updateBounds()
    {
        setBounds(m_x, m_y, m_width, m_height);
    }

    @Override
	protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
		
		int arcWidth = 50;
		int arcHeight = 50;

        Graphics2D g2d = (Graphics2D)g;
		GradientPaint gradient = new GradientPaint(0, 0, m_color.darker(), m_width, m_height, m_color.brighter());
		
		g2d.setPaint(gradient);
		g2d.fillRoundRect(0, 0, m_width, m_height, arcWidth, arcHeight);
		
		g2d.setColor(m_color.darker());
		g2d.drawRoundRect(0, 0, m_width - 1, m_height - 1, arcWidth, arcHeight);
		
		g.setColor(Color.WHITE);

        Font font = g.getFont().deriveFont(Font.BOLD, 14);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);

        String scoreStr = String.format("%s : %d", m_scoreName, m_score);
		
		int strategyWidth = metrics.stringWidth(m_strategy);
        int scoreWidth = metrics.stringWidth(scoreStr);
		int textHeight = metrics.getHeight();
		
		int xStrategy = (m_width - strategyWidth) / 2;
		int yStrategy = (m_height - textHeight) / 2 + metrics.getAscent() - 10;

        int xScore = (m_width - scoreWidth) / 2;
        int yScore = yStrategy + 15;

        g.drawString(m_strategy, xStrategy, yStrategy);
        g.drawString(scoreStr, xScore, yScore);
    }
}
