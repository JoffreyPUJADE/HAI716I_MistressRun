package GraphicsPack;

import AlgoPack.Common;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Window extends JFrame implements WindowListener, KeyListener
{
	private int m_width;
	private int m_height;
	private boolean m_runAsked;
	
	public Window(String windowName, int width, int height)
	{
		super(windowName);
		
		setLayout(null);
		
		m_width = width;
		m_height = height;
		m_runAsked = false;
		
		addWindowListener(this);
		addKeyListener(this);
		setSize(m_width, m_height);
		setVisible(true);
	}

	public boolean runAsked()
	{
		return m_runAsked;
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	
	@Override
	public void windowClosed(WindowEvent e) { System.exit(0); }
	
	@Override
	public void windowClosing(WindowEvent e) { System.exit(0); }
	
	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	@Override
	public void windowDeiconified(WindowEvent e) {}
	
	@Override
	public void windowIconified(WindowEvent e) {}
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_R) // "R" is for "Run".
		{
			m_runAsked = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_S) // "S" is for "ScreenShot".
		{
			takeScreenShot();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	private String takeScreenShot()
	{
		BufferedImage screenShot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		paint(screenShot.getGraphics());

		Common.createDirectoryIfNotExists("ScreenShots");
		String path = "ScreenShots/".concat(Common.getRandomString(8)).concat(".png");

		File outputFile = new File(path);

		try
		{
			if(ImageIO.write(screenShot, "png", outputFile))
			{
				return path;
			}

			return null;
		}
		catch(IOException err)
		{
			System.out.println(path);
			err.printStackTrace();

			return null;
		}
	}
}
