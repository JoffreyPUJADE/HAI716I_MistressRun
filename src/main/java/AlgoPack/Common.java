package AlgoPack;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Random;
import java.io.IOException;

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
	
	static public int randint(int min, int max)
	{
		if(min >= max)
		{
			throw new IllegalArgumentException("max must be greater than min");
		}
		
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	static public int countFilesInResourcesSubdirectory(String subdirPath)
	{
		try
		{
			String jarPath = Common.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			JarFile jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> entries = jarFile.entries();
			
			int fileCount = 0;
			
			while(entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				
				if(entry.getName().startsWith(subdirPath) && !entry.isDirectory())
				{
					++fileCount;
				}
			}
			
			jarFile.close();
			
			return fileCount;
		}
		catch(IOException err)
		{
			err.printStackTrace();
			
			return -1;
		}
	}
}
