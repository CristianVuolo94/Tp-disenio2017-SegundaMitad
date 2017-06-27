package viewModel;
import java.io.FileNotFoundException;
import org.uqbar.commons.model.UserException;
import org.uqbar.commons.utils.Observable;
import com.google.gson.JsonSyntaxException;
import exceptions.JsonMappingException;
import exceptions.NoFileSelectedException;
import exceptions.ReadingFileException;
import javassist.compiler.SyntaxError;
import modelo.FileLoader;
import modelo.JsonMapper;

@Observable
public class LoadCalculationsVM 
{
	private String filePath;
	
	public LoadCalculationsVM()
	{

		filePath = "";
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath_) {
		this.filePath = filePath_;
		
	}

	public void parseFile()	
	{
		if(filePath != null)
		{
			this.parseJson();
		}
		else
		{
			throw new NoFileSelectedException("No se ha cargado un archivo");
		}
		
	}		

	
	
	private void parseJson()  
	{
		try
		{
			FileLoader fileLoader = new FileLoader(filePath);
			JsonMapper jsonMapper = new JsonMapper();
			jsonMapper.mapper(fileLoader.reader());		
		
		}
		catch(FileNotFoundException fileReaderException)	
		{						
			throw new ReadingFileException("Error en la lectura del archivo de Empresas");
		}
		
		catch(JsonSyntaxException jsonFormatException)
		{
			throw new JsonMappingException("Error en el formato del archivo de Empresas");
		}	
	}
}
