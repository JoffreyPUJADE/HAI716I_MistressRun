package GraphicsPack;

import javax.swing.*;
import java.awt.event.*;

public class Window extends JFrame implements WindowListener
{
	int m_width;
	int m_height;
	
	public Window(String windowName, int width, int height)
	{
		super(windowName);
		
		setLayout(null);
		
		m_width = width;
		m_height = height;
		
		addWindowListener(this);
		setSize(m_width, m_height);
		setVisible(true);
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
}
