package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.security.auth.callback.Callback;

import org.apache.log4j.helpers.DateTimeDateFormat;
import org.apache.poi.ss.formula.functions.Column;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

// duplica hijos en panel, entra varias veces. ver si el la copia de la tebla tambie se hace , muchas veces 

public class PrinterTable extends PrintPaper{
	
	private static char[] alphabet = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
	private static int productoWidth = 0;
	private static int detalleWidth = 0;
	private static AnchorPane pane;
	private static Label dateLb;
	private static Label numPageLb;
	private static double topMargin;
	private static double bottomMargin;
	private static double leftMargin;
	private static double rightMargin;
	private static double paperHeightPrint; 
	private static double paperWidthPrint;
	private static double paperHeight;
	private static double paperWidth;
	/* 
	 * el anchorPane tiene que tener el tamaño del area de impresion 
	 * la fecha y el numero de hoja la posicion del topMargin 
	 * la tabla la posicion marginLeft, marginRigh,MarginButtom y el MarginTop + 5
	 * tener en cuenta que los margenes entan en el minimo por lo tanto tratar de calcular lor recomendados
	 * ver como hacer que las columnas no se pasen del la tabla
	*/ 
	
	
	
	public static <T> void printTable(TableView<Producto> tableView, PrinterJob jobArg) {
		boolean createJob = jobArg == null;
		PrinterJob job;
				
		if (createJob) {
			job = PrinterJob.createPrinterJob();
		} else {
			job = jobArg;
		}
		
		printWithJobTable(tableView, job);
		if (createJob) {
			job.endJob();
		}
		
	}

	private static void printWithJobTable(TableView<Producto> tableView, PrinterJob job) {
		
		Printer printer = job.getPrinter();
		PageLayout pageLayout = job.getJobSettings().getPageLayout();
		
		pageLayout = printer.createPageLayout(pageLayout.getPaper(),pageLayout.getPageOrientation(),
												Printer.MarginType.HARDWARE_MINIMUM);
		margin(pageLayout);
		sizePaper(pageLayout);
		pane = new AnchorPane();
		pane.setPrefSize(paperWidth, paperHeight);
		datePrint(pane);
		TableView<Producto> copyView = createTableCopy(tableView, job,pageLayout);
		printList(tableView, copyView, job, pageLayout,pane);
	
	}
	
	private static void printList(TableView<Producto> tableView, TableView<Producto> copyTableView,PrinterJob job,PageLayout pageLayout,AnchorPane pane) {
				
		ArrayList<Producto> itemList;
		// ver como ir cambiando la letra
		for (int i = 0; i < alphabet.length; i++) {
			char letter = alphabet[i];
		
			itemList =  new ArrayList<Producto>(tableView.getItems()
					 									 .filtered(prod -> isLetterStart(prod.getIdNegocio(), letter)));
			int num = 1;
			while (itemList != null && !itemList.isEmpty()) {
				printPage(job, copyTableView, itemList,pageLayout,letter,pane,num);
				 num+= 1;
			}
		}
	
	}
	
	protected static <T> void printPage(PrinterJob job, TableView<T> copyView, ArrayList<T> itemList,PageLayout pageLayout,char letter,AnchorPane pane,int pageNum) {
		
		ObservableList<T> batchItemList = FXCollections.observableArrayList();
		copyView.setItems(batchItemList);
		ScrollBar verticalScrollbar = getVerticalScrollbar(copyView);
		
//		batchItemList.add(itemList.remove(0));
		while (!verticalScrollbar.isVisible() && itemList.size() > 0) {
			T item = itemList.remove(0);
			batchItemList.add(item);
			copyView.layout();
			
		}
		
		
		while (batchItemList.size() > 0 && verticalScrollbar.isVisible()) {
			
			T item = batchItemList.remove(batchItemList.size() - 1);
			itemList.add(0, item);
			copyView.layout();

		}
				
		numPagePrint(pane, letter,pageNum);

//		pane.setRightAnchor(copyView, 10.0);
//		pane.setLeftAnchor(copyView, 10.0);
		
		if (pane.getChildren().indexOf(copyView) < 0)
			pane.getChildren().add(copyView);
		
		resizeColumns(copyView);
		
		job.printPage(pageLayout,pane);
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected
	static <T> TableView<T> createTableCopy(TableView<T> tableView, PrinterJob job,PageLayout pageLayout) {
		TableView<T> copyView = new TableView<>();
							
		if (pageLayout.getPageOrientation().equals(PageOrientation.PORTRAIT)) {
			copyView.setPrefHeight(paperHeightPrint);
			copyView.setPrefWidth(paperWidthPrint);
			
		} else {
			copyView.setPrefHeight(paperWidthPrint);
			copyView.setPrefWidth(paperHeightPrint);
		}
		
		copyView.setTranslateY(topMargin + dateLb.getPrefHeight() + 10);
		copyView.setColumnResizePolicy(tableView.getColumnResizePolicy());

		for (TableColumn<T, ?> colum : tableView.getColumns()) {
			if (!colum.getText().equals(ProductoTableExel.getRowPrecioCosto()) 
					&& !colum.getText().equals(ProductoTableExel.getRowRecargo())) {
				TableColumn cloneColumn = columnText(colum);
				
//				columnMaxWidth(tableView, cloneColumn, colum);
				
				if (colum.getCellValueFactory() != null) 
					cloneColumn.setCellValueFactory(colum.getCellValueFactory());
				
				if (colum.getCellFactory() != null) 
					cloneColumn.setCellFactory(colum.getCellFactory());
				
				copyView.getColumns().add(cloneColumn);
				
			}
			
		}
		
		new Scene(copyView);
		copyView.getStylesheets().add(PrintPaper.class.getResource("PrinterTableStyle.css").toString());
		copyView.getStyleClass().add("tablePrinter");
		
		return copyView;
	}
	
	private static boolean isLetterStart(String id, Character letter) {
		char temp = Character.toUpperCase(letter);
		return id.toUpperCase().startsWith(Character.toString(temp));
	}
	
	private static <S> TableColumn<S, Producto> columnText(TableColumn cloneColumn) {
		String text = cloneColumn.getText().trim();
		if (text.equals(ProductoTableExel.getRowIdEmpresa()))
			text = ProductoTableExel.ROW_ID_EMPRESA_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowIdNegocio()))
			text = ProductoTableExel.ROW_ID_NEGOCIO_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowNombreProducto()))
			text = ProductoTableExel.ROW_PRODUCTO_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowDetalle()))
			text = ProductoTableExel.ROW_DETALLE_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowPrecioCantidad()))
			text = ProductoTableExel.ROW_PRECIO_CANTIDAD_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowPrecioVenta()))
			text = ProductoTableExel.ROW_PRECIO_VENTA_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowUpdateAt()))
			text = ProductoTableExel.ROW_UPDATE_AT_ABBREVIATED;
		return new TableColumn<S, Producto>(text);
	}
	
	private static void maxWidth(TableView<Producto> tableView) {
		
		for (Producto prod : tableView.getItems()) {
			int name = prod.getNombre().length();
			int detalle = prod.getDetalle().length();
			if(detalleWidth < detalle)
				detalleWidth = detalle;
			else if (productoWidth < name)
				productoWidth = name;
		}
		
	}
		
	private static void sizePaper(PageLayout pageLayout) {
		paperHeight = pageLayout.getPaper().getHeight();
		paperWidth = pageLayout.getPaper().getWidth();
		paperHeightPrint = pageLayout.getPrintableHeight() - bottomMargin - topMargin; 
		paperWidthPrint = pageLayout.getPrintableWidth();
	}
	
	private static void margin(PageLayout pageLayout) {
		topMargin 		= pixelRound(pageLayout.getTopMargin());
		bottomMargin	= pixelRound(pageLayout.getBottomMargin());
		leftMargin	 	= pixelRound(pageLayout.getLeftMargin());
		rightMargin	 	= pixelRound(pageLayout.getRightMargin());
	}
	
	
	private static void datePrint(AnchorPane pane) {
		
		LocalDate lDate = LocalDate.now();
		String date = "Fecha de impresion "+Producto.dateFormated(lDate).trim();
				
		double coordX =  paperWidthPrint - (paperWidthPrint / 4);
		if (pane.getChildren().indexOf(dateLb) < 0) {
			dateLb = new Label();
			dateLb.setTranslateX(coordX - date.length() );
			dateLb.setTranslateY(5);
			pane.getChildren().add(dateLb);
		}
		dateLb.setText(date);
	}
	
	private static void numPagePrint(AnchorPane pane, char letter, int numPag) {
		String pag = String.valueOf(letter).toUpperCase();
		String num = String.format("%02d" , numPag);
		
		if (pane.getChildren().indexOf(numPageLb) < 0) {
			numPageLb = new Label();
			numPageLb.setTranslateX(leftMargin + rightMargin);
			numPageLb.setTranslateY(5);
			pane.getChildren().add(numPageLb);
		}
				
		numPageLb.setText((pag+" "+num).trim());
		
		
	}

	public static <T> void resizeColumns( TableView<T> table )	{
		//Set the right policy
		
       table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
       ObservableList<TableColumn<T, ?>> columns = table.getColumns();
		
		for (int i = 0; i < columns.size(); i++) {
			TableColumn<T, ?> column = columns.get(i);
			String text = column.getText();
			Text t = new Text( text );
			if (text != ProductoTableExel.ROW_DETALLE_ABBREVIATED) {
				double max = t.getLayoutBounds().getWidth();
				
				for ( int j = 0; j < table.getItems().size(); j++ ){
		           //cell must not be empty
		           if ( column.getCellData( j ) != null ){
		               
		        	   t = new Text( column.getCellData( j ).toString() );
		               double calcwidth = t.getLayoutBounds().getWidth();
		               //remember new max-width
		               if ( calcwidth > max )
		            	   max = calcwidth;
		           }
				}
				column.setResizable(true);
				if (column.getText() != ProductoTableExel.ROW_DETALLE_ABBREVIATED) 
					column.setPrefWidth( max + 6.0d );
				}
			
		}
      	fixresizeColums(table);      
	}
	
	private static <T> void fixresizeColums(TableView<T> tableView) {
		ObservableList<TableColumn<T, ?>> columns = tableView.getColumns();
		double width = 0;
		for (int i = 0; i < columns.size(); i++) {
			if (i != 3 ) 
				width += columns.get(i).widthProperty().get();
						
		}
		
		columns.get(3).prefWidthProperty().bind(tableView.widthProperty().subtract(width).divide(1));
		Double size = columns.get(3).prefWidthProperty().get();
		
		if (width + size >= paperWidthPrint - leftMargin) {
			
			Double sobrante = (width + size)  -  (paperWidthPrint - leftMargin);

			if (sobrante == 0) { 
				Double num = (width + size) - paperWidthPrint;
				size = size - 10;
				System.out.println("ingreso");
			}
			else 			
				size -= sobrante;
				 
			columns.get(3).prefWidthProperty().unbind();
			columns.get(3).setPrefWidth(size);
			
		}
		
	}
	
	private static void autoResizetext( TableView<Producto> table )
	{
		for (int i = 0; i < table.getItems().size(); i++) {
			Producto item = table.getItems().get(i);
			
			if (item != null) 
				table.getItems().set(i, productSizeText(item));
						
		}
     
	}
	
	private static String resizeText(String value,int max) {
		value = value.replaceAll("\n", " ").trim();
		if (value.length() > max) {
     	   
			String[] aux = value.split(" ");
			String temp = "";
			int textSize = 0;
	 	   	for (int i = 0; i < aux.length; i++) {
	 	   		
	 	   		int wordSize = aux[i].length();
	 	   		
	 	   		if (textSize + wordSize >= max && wordSize > 4) {
	 	   			temp += "\n"+aux[i];
					textSize = wordSize;
				}
	 	   		else {
	 	   			temp += " "+aux[i];
	 	   			textSize += wordSize;
	 	   		}		   
	 	   	}
			value = temp.trim();
		}
		return value;
	}	
	
	private static Producto productSizeText(Producto producto ) {
		int max = 16;
		String idNegocio = producto.getIdNegocio();
		String idEmpresa = producto.getIdEmpresa();
		String name = producto.getNombre();
		String detalle = producto.getDetalle();
			
		producto.setIdNegocio(resizeText(idNegocio, max));
		producto.setIdEmpresa(resizeText(idEmpresa, max));
		producto.setNombre(resizeText(name, max));
		producto.setDetalle(resizeText(detalle, max));
		return producto;
	
	}
	public static int getProductoWidth() { return productoWidth;	}

	public static void setProductoWidth(int productoWidth) { PrinterTable.productoWidth = productoWidth;	}

	public static int getDetalleWidth() { return detalleWidth;	}

	public static void setDetalleWidth(int detalleWidth) { PrinterTable.detalleWidth = detalleWidth;	}

	public static double getTopMargin() { return topMargin;	}

	public static void setTopMargin(double topMargin) { PrinterTable.topMargin = topMargin;	}

	public static double getBottomMargin() { return bottomMargin;	}

	public static void setBottomMargin(double bottomMargin) { PrinterTable.bottomMargin = bottomMargin;	}

	public static double getLeftMargin() { return leftMargin;	}

	public static void setLeftMargin(double leftMargin) { PrinterTable.leftMargin = leftMargin;	}

	public static double getRightMargin() { return rightMargin;	}

	public static void setRightMargin(double rightMargin) { PrinterTable.rightMargin = rightMargin;	}

	public static double getPaperHeightPrint() { return paperHeightPrint;	}

	public static void setPaperHeightPrint(double paperHeightPrint) { PrinterTable.paperHeightPrint = paperHeightPrint;	}

	public static double getPaperWidthPrint() { return paperWidthPrint;	}

	public static void setPaperWidthPrint(double paperWidthPrint) { PrinterTable.paperWidthPrint = paperWidthPrint;	}

	public static double getPaperHeight() { return paperHeight;	}

	public static void setPaperHeight(double paperHeight) { PrinterTable.paperHeight = paperHeight;	}

	public static double getPaperWidth() { return paperWidth;	}

	public static void setPaperWidth(double paperWidth) { PrinterTable.paperWidth = paperWidth;	}
	
	
}	
	
