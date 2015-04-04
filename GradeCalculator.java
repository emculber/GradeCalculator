import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class GradeCalculator {

	private inputChecker input = new inputChecker();
	private ArrayList<String> catagoryName = new ArrayList<String>();
	private ArrayList<Double> catagoryPercents = new ArrayList<Double>();

	private String fileName = "";
	private File Data;

	public static void main(String[] args) {
		new GradeCalculator();
	}

	public GradeCalculator() {

		if (!SetUpFileToSaveOrLoad()) {
			CalcCatagoryPercents();
			CalcAllGrades();
		}
		DisplayGrades();
	}

	private boolean SetUpFileToSaveOrLoad() {
		File theDir = new File("SavedData");
		System.out.print("\033[H\033[2J");
		System.out.flush();
		if (!theDir.exists()) {
			try {
				theDir.mkdir();
			} catch (SecurityException se) {
				se.getMessage();
			}
		}

		System.out.print("\033[H\033[2J");
		System.out.flush();
		File[] listOfFiles = theDir.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		if (input.getInputString("Would you like to load an existing file?(y/n)").equalsIgnoreCase("y")) {
			fileName = input.getInputString("Please Enter the Name of the Existing File:");
			Data = new File("SavedData/" + fileName + ".txt");
			return true;
		} else {
			System.out.print("\033[H\033[2J");
			System.out.flush();
			fileName = input.getInputString("Please Enter New File Name:");
			Data = new File("SavedData/" + fileName + ".txt");
			return false;
		}
	}

	private void CalcCatagoryPercents() {
		double maxPercent = 100;
		while (maxPercent != 0) {
			String name = input.getInputString("Please Enter Catagory Name:");
			double percent = input.getInputDouble("Please Enter Catagory Percent:");
			maxPercent -= percent;
			if (maxPercent < 0) {
				maxPercent = 0;
			}
			catagoryName.add(name);
			catagoryPercents.add(percent);
			System.out.print("\033[H\033[2J");
			System.out.flush();
			for (int i = 0; i < catagoryName.size(); i++) {
				System.out.println(catagoryName.get(i) + ", " + catagoryPercents.get(i));
			}
		}
	}

	private void CalcAllGrades() {
		Double[] percents = new Double[catagoryPercents.size() + 1];

		for (int x = 0; x < catagoryPercents.size() + 1; x++) {
			percents[x] = 0.0;
		}

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Data));

			String list = "--" + catagoryName.get(0) + ":" + catagoryPercents.get(0);
			for (int i = 1; i < catagoryName.size(); i++) {
				list += "," + catagoryName.get(i) + ":" + catagoryPercents.get(i);
			}
			output.write(list + "\n");

			while (percents[(catagoryPercents.size() - 1)] != 101) {
				double workingPercent = 0.0;
				for (int x = 0; x < catagoryPercents.size(); x++) {
					System.out.print(percents[x] + "*" + (((double) catagoryPercents.get(x)) / 100.0) + " + ");
					workingPercent += percents[x] * (((double) catagoryPercents.get(x)) / 100.0);
				}
				System.out.print("\b\b= " + workingPercent + "              \r");
				percents[catagoryPercents.size()] = workingPercent;

				list = Double.toString(percents[0]);
				for (int i = 1; i < percents.length; i++) {
					list += ":" + Double.toString(percents[i]);
				}
				list += "\n";
				output.write(list);

				percents[0]++;
				for (int x = 1; x < catagoryPercents.size(); x++) {
					if (percents[x - 1] >= 101) {
						percents[x]++;
						percents[x - 1] = 0.0;
					}
				}
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void DisplayGrades() {
		catagoryName = new ArrayList<String>();
		catagoryPercents = new ArrayList<Double>();
		do {
			double percent = input.getInputDouble("Please Enter Lookup Percent:");
			try (BufferedReader br = new BufferedReader(new FileReader(Data))) {
				String line;
				while ((line = br.readLine()) != null) {
					if (line.contains("--")) {
						String[] cat = line.split(",");
						for (int i = 0; i < cat.length; i++) {
							String[] catsplit = cat[i].split(":");
							catagoryName.add(catsplit[0]);
							catagoryPercents.add(Double.parseDouble(catsplit[1]));
						}
					} else {
						String[] lineSplit = line.split(":");
						if (Double.parseDouble(lineSplit[catagoryPercents.size()]) <= percent + 1 && Double.parseDouble(lineSplit[catagoryPercents.size()]) >= percent - 1) {
							System.out.print("Grade in that range :: ");
							displayGrade(lineSplit);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (input.getInputString("Would you like to search another?(y/n)").equalsIgnoreCase("y"));
	}

	private void displayGrade(String[] grade) {
		for (int i = 0; i < grade.length; i++) {
			if (catagoryName.size() <= i) {
				System.out.print("Final" + " : " + grade[i]);
			} else {
				System.out.print(catagoryName.get(i) + " : " + grade[i] + " :: ");
			}
		}
		System.out.print("\n");
	}

	private void Loadfile() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(Data))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
			}
		}
	}
}
