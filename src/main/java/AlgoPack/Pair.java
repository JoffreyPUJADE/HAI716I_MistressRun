package AlgoPack;

public class Pair<T, U>
{
	private T m_key;
	private U m_value;
	
	public Pair(T key, U value)
	{
		m_key = key;
		m_value = value;
	}
	
	public T getKey()
	{
		return m_key;
	}
	
	public U getValue()
	{
		return m_value;
	}
	
	public void setKey(T key)
	{
		m_key = key;
	}
	
	public void setValue(U value)
	{
		m_value = value;
	}
	
	@Override
	public String toString()
	{
		return "(" + m_key + ", " + m_value + ")";
	}
}
