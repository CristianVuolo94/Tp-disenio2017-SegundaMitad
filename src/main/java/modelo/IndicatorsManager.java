package modelo;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import parser.IndicatorParser;


public class IndicatorsManager
{
	private static String filePath;
	private static String sheetName;
	
	public static void setFilePath(String path)
	{
		filePath=path;
	}
	
	public static void read() throws IOException  
	{
        File inputWorkbook = new File(filePath);
        Workbook w;
        try 
        {
        	WorkbookSettings wkbkSet = new WorkbookSettings();
        	wkbkSet.setSuppressWarnings(true);
            w = Workbook.getWorkbook(inputWorkbook,wkbkSet);
            
            // Para usar la primera hoja
            Sheet sheet = w.getSheet(0); 
            sheetName = sheet.getName();
            Cell indicators = getCellWithString(sheet,"Indicadores");
            
	        int columnNumber = indicators.getColumn();
	        int rowNumber = indicators.getRow();
	        int columnIndex = columnNumber;
	        int rowIndex = rowNumber+1;
	        
	        
            

			List<Indicator> list = new ArrayList<Indicator>();
            
	        while(!cellIsEmpty(sheet.getCell(columnIndex,rowIndex)))
	        {
	        	
	        	String name = sheet.getCell(columnIndex,rowIndex).getContents();
	        	String formula = sheet.getCell(columnIndex+1,rowIndex).getContents();
	        	Indicator indicator = new Indicator(name, formula, IndicatorParser.parseIndicator(formula));
	        	list.add(indicator);
	        	
	        	
	        	rowIndex++;
	        	
	        }
	        	w.close();
	        	IndicatorRepository.setIndicatorList(list);
	        	
        } catch (BiffException e) //Esta excepcion es por que este API solo lee .xls, Hay que catchearlo en la window para que le informe al usuario que solo soporta .xls
        {
            e.printStackTrace();
        }
    }
	
	 public static Cell getCellWithString(Sheet sheet,String string)
	 {
		 for (int j = 0; j < sheet.getColumns(); j++) 
	     {
			 for (int i = 0; i < sheet.getRows(); i++) 
	         {
				 Cell cell = sheet.getCell(j, i);
	             CellType type = cell.getType();
	                
	             if (type == CellType.LABEL) 
	             {
	            	 if(string.equals(cell.getContents()))
	                 {
	                	return cell;
	                 }
	             }
	                 
	        }
	     }
		 return null;
	 }
	
	public static Boolean cellIsEmpty(Cell cell)
	{
		return cell.getContents().isEmpty();
	}
	
	public static void writeExcel(Indicator indicator) throws BiffException

	{
		try
	    {
	    	
	        WorkbookSettings wkbkSet = new WorkbookSettings();
        	wkbkSet.setSuppressWarnings(true);
	        Workbook target_workbook = Workbook.getWorkbook(new File(filePath),wkbkSet);
	        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath), target_workbook);
	        
	        WritableSheet sheet = workbook.getSheet(sheetName);
	        Cell indicators = getCellWithString(sheet,"Indicadores");
            
	        int columnNumber = indicators.getColumn();
	        int rowNumber = indicators.getRow();
	        int columnIndex = columnNumber;
	        int rowIndex = rowNumber+1;	
            
	        while(!cellIsEmpty(sheet.getCell(columnNumber,rowIndex)))
	        {        	
	        	rowIndex++;
	        }
	        
	        Label label = new Label(columnIndex, rowIndex, indicator.getName());
	        sheet.addCell(label);
	        Label label2 = new Label(columnIndex+1, rowIndex, indicator.getFormula());
	        sheet.addCell(label2);
	        


	        workbook.write();
	        workbook.close();

	        }
	    catch (IOException ex)
	    {
	    	System.out.println("Error al crear el fichero.");//Por las pruebas, hacer con excepciones
	    }
	    catch (WriteException ex)
	    {
	    	System.out.println("Error al escribir el fichero.");//Por las pruebas, hacer con excepciones
	    }

	}
	
		
}
