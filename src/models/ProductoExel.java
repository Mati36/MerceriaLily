package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import controllers.ListProductoController;
import exeptions.ExelExeption;
import javafx.collections.ObservableList;


public class ProductoExel{
	
	public ProductoExel() {	}

	
	public void saveExel(ListProductoController list,File file) throws IOException, InvalidFormatException {
		FileOutputStream fileOutput = new FileOutputStream(file); // tratar de elegir la ruta
		ExelFile exel = new ExelFile();
		exel.createBook();
		
	// solo la primera vez que lo guardo mejorar
		ProductoTableExel.createTable(exel,exel.getBook());
			
	//	 Esto es bien 
		String sheet_name = ProductoTableExel.getSheetName();
	
		for (Producto producto : list.getListProducto()) {
			if ( !seach( producto.getIdNegocio(),exel.getSheet(sheet_name) ) ) {
				exel.addRow(sheet_name);
				for (int cell = 0; cell < ProductoTableExel.getIndexLast(); cell++)
					addCellContent(exel,exel.getLatsRow(sheet_name), producto,cell);
			}
		}
		
		exel.writeBook(fileOutput);
		exel.closeBook();
		fileOutput.close();
	}
	
	public void loadExel(ListProductoController list,File file) throws InvalidFormatException, FileNotFoundException, IOException {
		// ver como leer un archivo
		ExelFile exel = new ExelFile();
		FileInputStream fileInput = new FileInputStream(file);
		exel.loadBook(fileInput);
		for (Row row : exel.getSheet(ProductoTableExel.getSheetName())) { 
			if (row.getRowNum() != 0 && row != null) 
				exelToTable(row, list, exel);
		}
		
		fileInput.close();
		exel.closeBook();
	}

	public boolean seach(String idProducto,Sheet sheet) {
		for (Row row : sheet) {
			if (isProducto(row,ProductoTableExel.getIndexIdNegocio(),idProducto)) {
				return true;
			}
		}
		return false;
	}
	
	private void exelToTable(Row row, ListProductoController list,ExelFile exelFile) throws IOException {
		
		Producto producto = new Producto();
		for (int cell = 0; cell < row.getLastCellNum(); cell++) 
			getCellContent(cell, row.getCell(cell), producto,exelFile);
	
		list.add(producto);
			
	}

	private boolean isProducto(Row row,int indexCell ,String idProd) {
		return row.getCell(indexCell).getStringCellValue().equals(idProd);
	}
	
	private void addCellContent(ExelFile exel,Row row,Producto producto,int cell) throws ExelExeption {
		if (row != null && exel != null && producto != null ) {
			if (cell == ProductoTableExel.getIndexIdEmpresa())
				exel.addCellAndValue(row, cell,producto.getIdEmpresa());
			else if (cell == ProductoTableExel.getIndexIdNegocio())
				exel.addCellAndValue(row, cell,producto.getIdNegocio());
			else if (cell == ProductoTableExel.getIndexNombreProducto())
				exel.addCellAndValue(row, cell,producto.getNombre());
			else if (cell == ProductoTableExel.getIndexPrecioCantidad() )
				exel.addCellAndValue(row, cell,producto.getPrecioCantidad());
			else if (cell == ProductoTableExel.getIndexPrecioCosto())
				exel.addCellAndValue(row, cell,producto.getPrecioCosto());
			else if (cell == ProductoTableExel.getIndexPrecioVenta() )
				exel.addCellAndValue(row, cell,producto.getPrecioVenta());
			else if (cell == ProductoTableExel.getIndexRecargo())
				row.createCell(cell).setCellValue(producto.getRecargo());
			else if (cell == ProductoTableExel.getIndexDetalle() )
				exel.addCellAndValue(row, cell, producto.getDetalle());
			else if (cell == ProductoTableExel.getIndexCreatedAt())
				exel.addCellAndValue(row, cell, producto.getCreatedAt());
			else if (cell == ProductoTableExel.getIndexUpdatedAt())
				exel.addCellAndValue(row, cell, producto.getUpdateAt());
			else
				DialogShow.Error("Error exel","No se pudo ingresar ingresar datos en la celda "+cell
							+" puede que se paso de rango o el numero de celda no coincide con la base de datos");
		}
	}

	private void getCellContent(int index,Cell cell, Producto producto,ExelFile exelFile) throws ExelExeption {
		if (cell != null && producto != null && exelFile.isValue(cell)) {
			if (index == ProductoTableExel.getIndexIdEmpresa())
				producto.setIdEmpresa(exelFile.getCellValueToString(cell) );
			else if ( index == ProductoTableExel.getIndexIdNegocio())
				producto.setIdNegocio(exelFile.getCellValueToString(cell));
			else if (index == ProductoTableExel.getIndexNombreProducto())
				producto.setNombre(exelFile.getCellValueToString(cell));
			else if (index == ProductoTableExel.getIndexPrecioCantidad())
				producto.setPrecioCantidad(exelFile.getCellValueToDouble(cell));
			else if (index == ProductoTableExel.getIndexPrecioCosto())
				producto.setPrecioCosto(exelFile.getCellValueToDouble(cell));
			else if (index == ProductoTableExel.getIndexPrecioVenta())
				producto.setPrecioVenta(exelFile.getCellValueToDouble(cell));
			else if (index == ProductoTableExel.getIndexRecargo())
					producto.setRecargo(exelFile.getCellValueToDouble(cell));
			else if (index == ProductoTableExel.getIndexDetalle())
				producto.setDetalle(exelFile.getCellValueToString(cell));
			else if (index == ProductoTableExel.getIndexCreatedAt())
				producto.setCreatedAt(exelFile.getCellValueToDateTime(cell).toLocalDate());
			else if (index == ProductoTableExel.getIndexUpdatedAt())
				producto.setUpdatedAt(exelFile.getCellValueToDateTime(cell).toLocalDate());
			else
				DialogShow.Error("Error exel","No se pudo extraer datos en la celda "+cell+" inidice"+ index 
						+" puede que se paso de rango o el numero de celda no coincide con la base de datos");
		}
	}
	
	public void printExelSave(ObservableList<Producto> tableList,File file) throws IOException, InvalidFormatException {
		
		FileOutputStream fileOutput = new FileOutputStream(file); // tratar de elegir la ruta
		ExelFile exel = new ExelFile();
		
	// solo la primera vez que lo guardo mejorar
		exel.createBook();
		ProductoTableExel.createPrintTable(exel,exel.getBook());
	// Esto es bien 
		String sheet_name = ProductoTableExel.getSheetName();
		
		for (Producto producto : tableList) {
			if ( !seach( producto.getIdNegocio(),exel.getSheet(sheet_name) ) ) {
				exel.addRow(sheet_name);
				for (int cell = 0; cell < ProductoTableExel.getIndexLast(); cell++)
					if (cell != ProductoTableExel.getIndexRecargo() && cell != ProductoTableExel.getIndexPrecioCosto())
						addCellContent(exel,exel.getLatsRow(sheet_name), tableList.get(cell),cell);
			}
		}
		
		exel.writeBook(fileOutput);
		exel.closeBook();
		fileOutput.close();
	}


}
