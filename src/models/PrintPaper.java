package models;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


	/**
	 * This class provides a method to print the content of a {@link TableView}. It
	 * is at an early stage and currently doesn't support advanced CellFactorys.
	 */
	public class PrintPaper {
		
		/**
	 * Prints the content of the provided {@link TableView}.
	 * 
	 * @param tableView
	 *            See description.
	 * @param jobArg
	 *            The {@link PrinterJob} to use. When the value is
	 *            <code>null</code> this method creates a default
	 *            {@link PrinterJob} and ends it. When a valid
	 *            {@link PrinterJob} is provided, the caller must close it.
	 */
	public static <T> void print(TableView<T> tableView, PrinterJob jobArg) {
		boolean createJob = jobArg == null;
		PrinterJob job;
		if (createJob) {
			job = PrinterJob.createPrinterJob();
		} else {
			job = jobArg;
		}
		printWithJob(tableView, job);
		if (createJob) {
			job.endJob();
		}
		
	}
		
	/**
	 * The entry method for printing the table contents where {@link PrinterJob}
	 * is guaranteed to not be <code>null</code>.
	 * 
	 * @param tableView
	 *            See description.
	 * @param job
	 *            See description.
	 */
	public static <T> void printWithJob(TableView<T> tableView, PrinterJob job) {
		System.out.println("estot en printPaper");
		Printer printer = job.getPrinter();
		PageLayout pageLayout = job.getJobSettings().getPageLayout();
			
		pageLayout = printer.createPageLayout(pageLayout.getPaper(),pageLayout.getPageOrientation(), 
												Printer.MarginType.HARDWARE_MINIMUM);
				
		TableView<T> copyView = createTableCopy(tableView, job,pageLayout);
		ArrayList<T> itemList = new ArrayList<>(tableView.getItems());
		while (itemList.size() > 0) {
			
			printPage(job, copyView, itemList,pageLayout);
		}
	}
	
	/**
	 * Prints the first n-th items of the list with the help of the provided
	 * {@link TableView}. The concrete number of items that will be printed is
	 * the maximum of items that you can add to the table so that the vertical
	 * scrollbar of the table will not be visible.<br>
	 * All printed items are removed of the given list.
	 * 
	 * @param job
	 *            The job used for printing.
	 * @param copyView
	 *            See description.
	 * @param itemList
	 *            See description.
	 */
	protected static <T> void printPage(PrinterJob job, TableView<T> copyView, ArrayList<T> itemList,PageLayout pageLayout) {
		ScrollBar verticalScrollbar = getVerticalScrollbar(copyView);
		ObservableList<T> batchItemList = FXCollections.observableArrayList();
		copyView.setItems(batchItemList);
		batchItemList.add(itemList.remove(0));
		while (!verticalScrollbar.isVisible() && itemList.size() > 0) {
			T item = itemList.remove(0);
			batchItemList.add(item);
			copyView.layout();
		}
		if (batchItemList.size() > 1 && verticalScrollbar.isVisible()) {
			T item = batchItemList.remove(batchItemList.size() - 1);
			itemList.add(0, item);
			copyView.layout();
		}
		job.printPage(pageLayout,copyView);
	
	}
	
	/**
	 * Create a new {@link TableView} that copies serveral settings from the
	 * given one but uses the width/height based on the settings of the print
	 * job.
	 * 
	 * @param tableView
	 *            See description.
	 * @param job
	 *            See description.
	 * @return See description.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected
	static <T> TableView<T> createTableCopy(TableView<T> tableView, PrinterJob job,PageLayout pageLayout) {
		TableView<T> copyView = new TableView<>();
					
		double topMargin 	 = pixelRound(pageLayout.getTopMargin());
		double bottomMargin	 = pixelRound(pageLayout.getBottomMargin());
		double leftMargin	 = pixelRound(pageLayout.getLeftMargin());
		double rightMargin	 = pixelRound(pageLayout.getRightMargin());
		
		double paperHeightPrint = pageLayout.getPrintableHeight() - bottomMargin - topMargin; 
		double paperWidthPrint = pageLayout.getPrintableWidth() - rightMargin - leftMargin;
					
		if (pageLayout.getPageOrientation().equals(PageOrientation.PORTRAIT)) {
			copyView.setPrefHeight(paperHeightPrint);
			copyView.setPrefWidth(paperWidthPrint);
			
		} else {
			copyView.setPrefHeight(paperWidthPrint);
			copyView.setPrefWidth(paperHeightPrint);
		}
		
		copyView.setTranslateY(topMargin);
		copyView.setColumnResizePolicy(tableView.getColumnResizePolicy());
		
		for (TableColumn<T, ?> colum : tableView.getColumns()) {
			TableColumn cloneColumn = new TableColumn(colum.getText());
			cloneColumn.setMaxWidth(colum.getMaxWidth()); // pasar el string mas largo
			if (colum.getCellValueFactory() != null) 
				cloneColumn.setCellValueFactory(colum.getCellValueFactory());
			
			if (colum.getCellFactory() != null) 
				cloneColumn.setCellFactory(colum.getCellFactory());
			
			copyView.getColumns().add(cloneColumn);
		}
		new Scene(copyView);
	//			copyView.getScene().getStylesheets().add(PrintPaper.class.getResource("TablePrint.css").toString());
		return copyView;
	}
	
	/**
	 * Searches the vertical scrollbar in the {@link TableView}. The scrollbar
	 * won't be available on a off screen {@link TableView} (one that was never
	 * added to a visible stage) until at least once the snapshot method was
	 * called. The snapshot method is somehow expensive thus it can't be called
	 * too often. Thus this entry method is needed.
	 * 
	 * @param tableView
	 *            See description.
	 * @return The found {@link ScrollBar} or <code>null</code>, wenn none was
	 *         found.
	 */
	protected static <T> ScrollBar getVerticalScrollbar(TableView<T> tableView) {
		tableView.snapshot(new SnapshotParameters(), null);
		return getVerticalScrollbarForParent(tableView);
	}
	
	/**
	 * Searches for {@link ScrollBar} in the given {@link Parent} but stops when
	 * the node is {@link TableCell}
	 * 
	 * @param p
	 *            See description.
	 * @return The found {@link ScrollBar} or <code>null</code>, wenn none was
	 *         found.
	 */
	private static ScrollBar getVerticalScrollbarForParent(Parent p) {
		for (Node n : p.getChildrenUnmodifiable()) {
			if (n instanceof ScrollBar) {
				ScrollBar s = (ScrollBar) n;
				if (s.getOrientation() == Orientation.VERTICAL) 
					return s;
				
			}
			if (n instanceof Parent && !(p instanceof TableCell)) {
				ScrollBar scrollbar = getVerticalScrollbarForParent((Parent) n);
				if (scrollbar != null) 
					return scrollbar;
				
			}
		}
		return null;
	}
			
		
	static private void printMargin(PageLayout pageLayout) {
		System.out.println("\n margin top "+pageLayout.getTopMargin());
		System.out.println(" margin bottom "+pageLayout.getBottomMargin());
		System.out.println(" margin left "+pageLayout.getLeftMargin());
		System.out.println(" margin right "+pageLayout.getRightMargin());
	}
		
	static private void printPaperSize(Paper paper) {
		System.out.println("\n paper heigth "+paper.getHeight());
		System.out.println(" paper width "+paper.getWidth());
	}
	
	static private void printeablePaper(PageLayout pageLayout) {
		System.out.println("\n printable whidth "+pageLayout.getPrintableWidth());
		System.out.println(" printable Height "+pageLayout.getPrintableHeight());
	}
	
	protected static Double pixelRound(Double px) {
		return new BigDecimal(px).setScale(2, RoundingMode.UP).doubleValue(); 
	}
}
	

