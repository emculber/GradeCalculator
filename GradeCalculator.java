import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GradeCalculator {

	private inputChecker input = new inputChecker();
	private HashMap<String, Double> catagoryPercents = new HashMap<String, Double>();
	private ArrayList<Double[]> grades = new ArrayList<Double[]>();

	public static void main(String[] args) {
		new GradeCalculator();
	}

	public GradeCalculator() {
		CalcCatagoryPercents();
		CalcAllGrades();
		DisplayGrades();
	}

	private void CalcCatagoryPercents() {
		double maxPercent = 100;
		while (maxPercent != 0) {
			String name = input.getInputString("Please Enter Catagory Name:");
			double percent = input
					.getInputDouble("Please Enter Catagory Percent:");
			maxPercent -= percent;
			if (maxPercent < 0) {
				maxPercent = 0;
			}
			catagoryPercents.put(name, percent);
			System.out.print("\033[H\033[2J");
			System.out.flush();
			for (Map.Entry<String, Double> entry : catagoryPercents
					.entrySet()) {
				System.out.println(entry.getKey() + ", " + entry.getValue());
			}
		}
	}

	private void CalcAllGrades() {
		Double[] percents = new Double[catagoryPercents.size() + 1];
		ArrayList<Double> values = new ArrayList<Double>(
				catagoryPercents.values());

		for (int x = 0; x < catagoryPercents.size()+1; x++) {
			percents[x] = 0.0;
		}

		while (percents[(catagoryPercents.size() - 1)] != 101) {
			double workingPercent = 0.0;
			for (int x = 0; x < catagoryPercents.size(); x++) {
				System.out.print(percents[x] + "*"
						+ (((double) values.get(x)) / 100.0) + " + ");
				workingPercent += percents[x]
						* (((double) values.get(x)) / 100.0);
			}
			System.out.print("\b\b= " + workingPercent + "              \r");
			percents[catagoryPercents.size()] = workingPercent;
			
			grades.add(percents.clone());
			
			percents[0]++;
			for (int x = 1; x < catagoryPercents.size(); x++) {
				if (percents[x - 1] >= 101) {
					percents[x]++;
					percents[x - 1] = 0.0;
				}
			}
		}
	}

	private void DisplayGrades() {
		double percent = input.getInputDouble("Please Enter Lookup Percent:");
		for(int i = 0; i < grades.size(); i++) {
			//displayGrade(grades.get(i));
			if(grades.get(i)[catagoryPercents.size()] <= percent+1 && grades.get(i)[catagoryPercents.size()] >= percent-1) {
				System.out.print("Grade in that range :: ");
				displayGrade(grades.get(i));
			}
		}
	}
	
	private void displayGrade(Double[] grade) {
		ArrayList<String> keys = new ArrayList<String>(
				catagoryPercents.keySet());
		for(int i = 0; i < grade.length; i++) {
			if(keys.size() <= i) {
				System.out.print("Final" + " : " + grade[i]);
			}else {
			System.out.print(keys.get(i) + " : " + grade[i] + " :: ");
		}
		}
		System.out.print("\n");
	}
}
