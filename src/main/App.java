package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.AgingThread;
import model.Entry;
import model.PT;
import model.TLB;

public class App {
	
	private final static int PAGES_PER_PROCESS = 64;
	private final static String DIR_PROCESSES = "processes";

	private static boolean isFinished = false;
	
	public static void main(String[] args) throws IOException {
		
		int transTime = 0;
		int loadTime = 0;
		
		// Reads input
		int[] inputs = readInput();
		TLB tlb = new TLB(inputs[0]);
		PT pt = new PT(PAGES_PER_PROCESS, inputs[1]);
		new AgingThread(pt).start();
		
		String filename = new File(DIR_PROCESSES).list()[inputs[2]];
		String path = DIR_PROCESSES + File.separator + filename;
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while(line != null) {
			int reference = Integer.parseInt(line);
			
			int translation = tlb.consult(reference);
			transTime += 2;
			// TLB miss
			if(translation == -1) {
				translation = pt.consult(reference);
				transTime += 30;
				// PT miss
				if (translation == -1) {
					translation = pt.replace(reference); // Load from swap
					transTime += 30;
					loadTime += 10000000;
				}
				tlb.replace(new Entry(reference, translation));
			}
			loadTime += 30;
			
			line = reader.readLine();
		}
		reader.close();
		isFinished = true;
		
		System.out.println(transTime);
		System.out.println(loadTime);
	}
	
	private static int[] readInput() throws IOException {
		int[] inputs = new int[3];
		boolean fin = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(!fin) {
			try {
				System.out.println("Ingrese el numero de entradas de la TLB:\n\r");
				inputs[0] = Integer.parseInt(br.readLine());

				System.out.println("Ingrese el numero de marcos de pagina en RAM asignados al procesos:\n\r");
				inputs[1] = Integer.parseInt(br.readLine());

				int numFiles = printFiles();
				System.out.println("Ingrese el id del archivo del proceso a cargar:\n\r");
				inputs[2] = Integer.parseInt(br.readLine());

				if(inputs[0] < 0 || inputs[1] < 0 || (inputs[2] < 0 || inputs[2] > numFiles-1))
					throw new NumberFormatException();

				fin = true;

			} catch (NumberFormatException e) {
				System.out.println("\nPor favor ingresar valores válidos (naturales)\n\n");
			}
		}
		br.close();
		return inputs;
	}
	
	private static int printFiles() {
		String[] pathnames;
		File f = new File(DIR_PROCESSES);
		pathnames = f.list();
		for (int i = 0; i < pathnames.length; i++) {
			System.out.println(i + ". " + pathnames[i]);
		}
		return pathnames.length;
	}
	
	public static boolean isFinished() {
		return isFinished;
	}
}
