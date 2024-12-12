package TilePack;

public class Computer extends OrientedTile
{
    public Computer(String orientation)
    {
        super("tiles/computer.png", orientation, true, null);

        checkOrientation();
    }

    public Computer(Orientation orientation)
    {
        super("tiles/computer.png", orientation, true, null);

        checkOrientation();
    }

    private void checkOrientation()
    {
        if(m_orientation == Orientation.LEFT)
        {
            m_orientation = Orientation.FRONT;
        }
        else
        {
            m_orientation = (m_orientation == Orientation.RIGHT) ? Orientation.BACK : m_orientation;
        }

        orientationUpdated();
    }
}
