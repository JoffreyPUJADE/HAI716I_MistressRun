package LoaderDataPack;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ClassroomLoader
{
	public static ClassroomData loadClassroom(InputStream stream)
	{
		Gson gson = new Gson();
		
		try(InputStreamReader reader = new InputStreamReader(stream, "UTF-8"))
		{
			return gson.fromJson(reader, ClassroomData.class);
		}
		catch(IOException err)
		{
			err.printStackTrace();
			
			return null;
		}
	}
}
