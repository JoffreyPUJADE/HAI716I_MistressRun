package GraphicsPack;

import java.awt.Color;

public class MistressInfo extends CharacterInfo
{
    public MistressInfo(int x, int y, int width, int height, String strategy, int score)
    {
        super(x, y, width, height, Color.BLUE, strategy, "Touched students", score);
    }

    public int getTouchedStudents()
    {
        return m_score;
    }

    public void updateTouchedStudents(int touchedStudents)
    {
        m_score = touchedStudents;
        redraw();
    }
}
