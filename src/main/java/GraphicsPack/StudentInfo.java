package GraphicsPack;

import java.awt.Color;

public class StudentInfo extends CharacterInfo
{
    public StudentInfo(int x, int y, int width, int height, String strategy, int score)
    {
        super(x, y, width, height, Color.RED, strategy, "Candy counter", score);
    }

    public int getCandyCounter()
    {
        return m_score;
    }

    public void updateCandyCounter(int candyCounter)
    {
        m_score = candyCounter;
        redraw();
    }
}
