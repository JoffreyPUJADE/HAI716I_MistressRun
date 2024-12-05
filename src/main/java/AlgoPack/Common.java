package AlgoPack;

import java.io.InputStream;

public class Common
{
	static public InputStream getStreamFromResource(String resourceName)
	{
		ClassLoader classLoader;
		
		try
		{
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		catch(SecurityException err)
		{
			err.printStackTrace();
			
			return null;
		}
		
		if(classLoader == null)
		{
			System.out.println("Cannot load class.");
			
			return null;
		}
		
		InputStream is = classLoader.getResourceAsStream(resourceName);
		
		if(is == null)
		{
			System.out.println(String.format("Cannot load resource named \"%s\".", resourceName));
			
			return null;
		}
		
		return is;
	}
}
