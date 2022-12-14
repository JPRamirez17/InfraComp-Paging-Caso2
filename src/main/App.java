package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.AgingThread;
import model.PT;
import model.TLB;

/**
 * Main app where the model is created, user inputs parameters, and threads are run.
 */
public class App {
	
	// ----------------------------------------------------------------------------
	// CONSTANTS
	// ----------------------------------------------------------------------------

	/**
	 * Number of pages per process.
	 */
	private final static int PAGES_PER_PROCESS = 64;

	/**
	 * Processes virtual page reference files directory.
	 */
	private final static String DIR_PROCESSES = "processes";

	// ----------------------------------------------------------------------------
	// ATTRIBUTES
	// ----------------------------------------------------------------------------

	/**
	 * Total virtual address translation time (TLB and PT hits and miss).
	 */
	private static long transTime = 0;

	/**
	 * Total data loading time (swap to RAM and RAM to CPU).
	 */
	private static long loadTime = 0;

	/**
	 * Determines whether the App thread is done.
	 */
	private static boolean isFinished = false;
	
	// ----------------------------------------------------------------------------
	// MAIN
	// ----------------------------------------------------------------------------

	/**
	 * Main method where the input is read, and the virtual memory model is created and initialized.
	 * Reference file is read and all virtual addresses are translated.
	 * @param args Ignored in this case, input is obtained with method {@link #readInput readInput}.
	 * @throws IOException if there was a problem or interruption in the I/O operations.
	 */
	public static void main(String[] args) throws IOException {
		
		// Reads input
		int[] inputs = readInput();

		// Create virtual memory model
		TLB tlb = new TLB(inputs[0]);
		PT pt = new PT(PAGES_PER_PROCESS, inputs[1]);
		new AgingThread(pt).start();
		
		// Reads the selected file and resolves all virtual addresses.
		String filename = new File(DIR_PROCESSES).list()[inputs[2]];
		String path = DIR_PROCESSES + File.separator + filename;
		resolveAddresses(path, tlb, pt);
		
		// Prints total times and finishes
		System.out.println("Filename: " + path);
		System.out.println("Total translation time: " + transTime + " ns");
		System.out.println("Total data loading time: " + loadTime + " ns");
		isFinished = true;
	}

	// ----------------------------------------------------------------------------
	// AUX. METHODS
	// ----------------------------------------------------------------------------
	
	/**
	 * Read user's input corresponding to the amount of entries in the TLB, number of frames assigned 
	 * to each process, and filename of the process references.
	 * The request repeats until the user input correct values (natural).
	 * @return integer array of length 3, the first position (0) correspond to the amount of entries in the TLB, 
	 * the second position (1) to number of frames assigned to the process, and the third position (2) to the reference filename.
	 * @throws IOException if there was a problem or interruption in the I/O operations.
	 */
	private static int[] readInput() throws IOException {
		int[] inputs = new int[3];
		boolean fin = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(!fin) {
			try {
				System.out.println("Ingrese el numero de entradas de la TLB:\n\r");
				inputs[0] = Integer.parseInt(br.readLine());

				System.out.println("Ingrese el numero de marcos de pagina en RAM asignados al proceso:\n\r");
				inputs[1] = Integer.parseInt(br.readLine());

				int numFiles = printFiles();
				System.out.println("Ingrese el id del archivo del proceso a cargar:\n\r");
				inputs[2] = Integer.parseInt(br.readLine());

				if(inputs[0] < 0 || inputs[1] < 0 || (inputs[2] < 0 || inputs[2] > numFiles-1))
					throw new NumberFormatException();

				fin = true;

			} catch (NumberFormatException e) {
				System.out.println("\nPor favor ingresar valores validos (naturales)\n\n");
			}
		}
		br.close();
		return inputs;
	}
	
	/**
	 * Prints all possible files with virtual page references generated by the process.
	 * @return Number of possible files with virtual page references.
	 */
	private static int printFiles() {
		String[] pathnames;
		File f = new File(DIR_PROCESSES);
		pathnames = f.list();
		for (int i = 0; i < pathnames.length; i++) {
			System.out.println(i + ". " + pathnames[i]);
		}
		return pathnames.length;
	}

	/**
	 * Reads each virtual page reference in the file and resolves it with the virtual memory model.
	 * For each virtual page it will search its translation in the TLB, if it misses, search continues in the PT, 
	 * if it misses again, the page is loaded from the swap area, and table entries are updated.
	 * Each search in the tables and data load has a fixed time:
	 * TLB search 2ns.
	 * PT search 30ns.
	 * PT miss 2 PT search.
	 * Load from swap 10ms.
	 * Load from RAM 30ns.
	 * @param filepath Path of the file with the virtual page references to load. filepath != null.
	 * @param tlb Initialized empty TLB. tlb != null.
	 * @param pt Initialized empty PT. pt != null.
	 * @throws IOException if there was a problem or interruption in the I/O operations.
	 */
	private static void resolveAddresses(String filepath, TLB tlb, PT pt) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line = reader.readLine();
		while(line != null) {
			// Virtual page reference
			int reference = Integer.parseInt(line);
			
			int translation = tlb.consult(reference);

			// TLB miss
			if(translation == -1) {
				translation = pt.consult(reference);
				transTime += 30; // PT search 30 ns

				// PT miss
				if (translation == -1) {
					translation = pt.replace(reference);
					transTime += 30; // PT replace 30 ns
					loadTime += 10000000; // Load page from swap 10ms
				}
				else
					loadTime += 30; // Load page from main memory 30ns
				
					tlb.replace(pt.getTable().get(reference)); // Updates TLB
			} else {
				transTime += 2; // TLB search hit 2 ns
				loadTime += 30; // Load page from main memory 30ns
			}

			// Simulate all actions
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			line = reader.readLine();
		}
		reader.close();
	}
	
	/**
	 * Determines whether the App thread is done.
	 * @return true if the {@link #main main} is done, false if not.
	 */
	public static boolean isFinished() {
		return isFinished;
	}
}
