package AlgoPack;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Random;
import java.io.File;
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

	static public boolean isJar()
	{
		String path = Common.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return path.endsWith(".jar");
	}
	
	static public int countFilesInResourcesSubdirectory(String subdirPath)
	{
		try
		{
			if(isJar())
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
			else
			{
				File dir = new File("src/main/resources/" + subdirPath);

				if(!dir.exists() || !dir.isDirectory())
				{
					System.out.println("The specified directory does not exist.");
					return -1;
				}
				
				String[] files = dir.list((d, name) -> new File(d, name).isFile());
				return (files != null) ? files.length : 0;
			}
		}
		catch(IOException err)
		{
			err.printStackTrace();
			
			return -1;
		}
	}
	
	static public int distanceManhattan(int x1, int y1, int x2, int y2)
	{
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}
}
