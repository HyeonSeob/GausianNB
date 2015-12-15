import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class HS_NaiveBayes {
	public static final String TRAINING_FILE_NAME = "train.csv";
	public static final String TESTING_FILE_NAME = "test.csv";
	public static final String CLASS_NAME[] =
	"ARSON,ASSAULT,BAD CHECKS,BRIBERY,BURGLARY,DISORDERLY CONDUCT,DRIVING UNDER THE INFLUENCE,DRUG/NARCOTIC,DRUNKENNESS,EMBEZZLEMENT,EXTORTION,FAMILY OFFENSES,FORGERY/COUNTERFEITING,FRAUD,GAMBLING,KIDNAPPING,LARCENY/THEFT,LIQUOR LAWS,LOITERING,MISSING PERSON,NON-CRIMINAL,OTHER OFFENSES,PORNOGRAPHY/OBSCENE MAT,PROSTITUTION,RECOVERED VEHICLE,ROBBERY,RUNAWAY,SECONDARY CODES,SEX OFFENSES FORCIBLE,SEX OFFENSES NON FORCIBLE,STOLEN PROPERTY,SUICIDE,SUSPICIOUS OCC,TREA,TRESPASS,VANDALISM,VEHICLE THEFT,WARRANTS,WEAPON LAWS".split(",");
	public static final int CLASS_NUM = CLASS_NAME.length;
	public static final int CLASS_INDEX = 2;
	
	//public static final String TRAINING_FILE_NAME = "car_train.txt";
	//public static final String TESTING_FILE_NAME = "car_test.txt";
	//public static final String CLASS_NAME[] = {"acc", "unacc", "good", "vgood"};
	//public static final int CLASS_NUM = 4;

	public static void main(String[] args) {
		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new FileReader(TRAINING_FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HashMap<String, Integer>[] class_map = new HashMap[CLASS_NUM];
		for(int i=0; i<CLASS_NUM; i++)
			class_map[i] = new HashMap<String, Integer>();
		int class_cnt[] = new int[CLASS_NUM], class_num = 0;
		String line;
		String[] split;
		
		try {
			bfr.readLine();
			while((line = bfr.readLine()) != null){
				split = line.split("\t");
				
				for(int i=0; i<CLASS_NUM; i++){
					if(split[CLASS_INDEX].compareTo(CLASS_NAME[i]) == 0){
						class_num = i;
						break;
					}
				}
				class_cnt[class_num]++;

				for(int i=0; i<split.length-1; i++){
					if(class_map[class_num].get(split[i]) == null)
						class_map[class_num].put(split[i], 1);
					else
						class_map[class_num].put(split[i], class_map[class_num].get(split[i]) + 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Estimate
		try {
			bfr = new BufferedReader(new FileReader(TESTING_FILE_NAME));
			double max;
			int maxi;

			bfr.readLine();
			while((line = bfr.readLine()) != null){
				double class_prob[] = new double[CLASS_NUM];
				for(int i=0; i<CLASS_NUM; i++)
					class_prob[i] = 1;
				
				max = maxi = -1;
				for(int i=0; i<CLASS_NUM; i++){
					for(String s : line.split(",")){
						if(class_map[i].get(s) == null)
							class_prob[i] = 0;
						else
							class_prob[i] *= class_map[i].get(s) / (double)class_cnt[i];
					}
					class_prob[i] *= class_cnt[i] / (double)(class_cnt[0]+class_cnt[1]);
					if(max < class_prob[i]){
						max = class_prob[i];
						maxi = i;
					}
				}
				System.out.println(line+","+CLASS_NAME[maxi]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}