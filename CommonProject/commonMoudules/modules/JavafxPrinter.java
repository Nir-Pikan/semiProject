package modules;

import javafx.print.PageLayout;
import javafx.print.PageRange;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.stage.Window;

/**
 * class for printing JavaFx screens, all prints start with 2 dialogs to select
 * page properties and selecting printer
 * 
 * @author Or Man
 * @version 1.0
 * @since 23/12/2020
 * @see JavafxPrinter#printNode(Node, Window)
 * @see JavafxPrinter#printThisWindow(Window)
 * @see JavafxPrinter#printWinowOwnerOfNode(Node)
 */
public class JavafxPrinter {

	/**
	 * prints the {@link Window} that the {@link Node} enclosed in
	 * 
	 * @param toPrint {@link Node} in the window
	 * @see JavafxPrinter
	 */
	public static void printWinowOwnerOfNode(Node toPrint) {
		print(toPrint.getScene().getWindow(), toPrint.getScene().getRoot().snapshot(null, null));
	}

	/**
	 * prints selected {@link Node}
	 * 
	 * @param toPrint {@link Node} to print
	 * @see JavafxPrinter
	 */
	public static void printNode(Node toPrint) {
		print(toPrint.getScene().getWindow(), toPrint.snapshot(null, null));
	}

	/**
	 * prints selected {@link Window}
	 * 
	 * @param ownerWindow the {@link Window} to print
	 * @see JavafxPrinter
	 */
	public static void printThisWindow(Window ownerWindow) {
		print(ownerWindow, ownerWindow.getScene().getRoot().snapshot(null, null));
	}

	/**
	 * Executing the print, show the 2 dialogs
	 * 
	 * @param ownerWindow the {@link Window} which the dialogs should show in
	 * @param screenshot  the image to print
	 */
	private static void print(Window ownerWindow, WritableImage screenshot) {
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			job.getJobSettings().setPageRanges(new PageRange(1, 1));
			if (!job.showPageSetupDialog(ownerWindow) || !job.showPrintDialog(ownerWindow))
				return;

			// Scale image to full page
			final PageLayout pageLayout = job.getJobSettings().getPageLayout();
			final double scaleX = pageLayout.getPrintableWidth() / screenshot.getWidth();
			final double scaleY = pageLayout.getPrintableHeight() / screenshot.getHeight();
			final double scale = Math.min(scaleX, scaleY);
			final ImageView print_node = new ImageView(screenshot);
			print_node.getTransforms().add(new Scale(scale, scale));
			job.printPage(print_node);
			job.endJob();
		}
	}

}
