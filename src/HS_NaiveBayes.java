import java.io.*;
import java.util.HashMap;


public class HS_NaiveBayes {
	public static final String TRAINING_FILE_NAME = "train.csv";
	public static final String TESTING_FILE_NAME = "test.csv";
	public static final String SUBMISSION_FILE_NAME = "submission.csv";
	public static final String[] CLASS_NAME =
	"ARSON,ASSAULT,BAD CHECKS,BRIBERY,BURGLARY,DISORDERLY CONDUCT,DRIVING UNDER THE INFLUENCE,DRUG/NARCOTIC,DRUNKENNESS,EMBEZZLEMENT,EXTORTION,FAMILY OFFENSES,FORGERY/COUNTERFEITING,FRAUD,GAMBLING,KIDNAPPING,LARCENY/THEFT,LIQUOR LAWS,LOITERING,MISSING PERSON,NON-CRIMINAL,OTHER OFFENSES,PORNOGRAPHY/OBSCENE MAT,PROSTITUTION,RECOVERED VEHICLE,ROBBERY,RUNAWAY,SECONDARY CODES,SEX OFFENSES FORCIBLE,SEX OFFENSES NON FORCIBLE,STOLEN PROPERTY,SUICIDE,SUSPICIOUS OCC,TREA,TRESPASS,VANDALISM,VEHICLE THEFT,WARRANTS,WEAPON LAWS".split(",");
	public static final int CLASS_NUM = CLASS_NAME.length;
	public static final int CLASS_INDEX = 1;

	public static final String[] TRAIN_ATTR_NAME = "Dates,Category,Descript,DayOfWeek,PdDistrict,Resolution,Address,X,Y".split(",");
	public static final String[] TEST_ATTR_NAME = "Id,Dates,DayOfWeek,PdDistrict,Address,X,Y".split(",");
	public static final int TRAIN_ATTR_NUM = TRAIN_ATTR_NAME.length;
	public static final int TEST_ATTR_NUM = TEST_ATTR_NAME.length;

	//public static final String TRAINING_FILE_NAME = "car_train.txt";
	//public static final String TESTING_FILE_NAME = "car_test.txt";
	//public static final String CLASS_NAME[] = {"acc", "unacc", "good", "vgood"};
	//public static final int CLASS_NUM = 4;

	public static HashMap<Integer, Boolean> ignore_test_index;
	public static HashMap<Integer, Boolean> ignore_train_index;

	public static void initialize(){
		ignore_test_index = new HashMap();
		ignore_test_index.put(0,true);
		ignore_test_index.put(1,true);

		ignore_train_index = new HashMap();
		ignore_train_index.put(0,true);
		ignore_train_index.put(1,true);
		ignore_train_index.put(2,true);
		ignore_train_index.put(5,true);
	}

	public static int getClassIndex(String s){
		for(int i=0; i<CLASS_NUM; i++){
			if(s.equals(CLASS_NAME[i]) == true)
				return i;
		}
		return -1;
	}

	public static void main(String[] args) {
		initialize();

		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new FileReader(TRAINING_FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HashMap<String, Integer>[] class_map = new HashMap[CLASS_NUM];
		for(int i=0; i<CLASS_NUM; i++)
			class_map[i] = new HashMap<>();
		int class_cnt[] = new int[CLASS_NUM], class_num = 0;
		String line;
		String[] split;

		System.out.println("Training...");
		try {
			bfr.readLine();
			while((line = bfr.readLine()) != null){
				split = line.split(",");
				
				for(int i=0; i<CLASS_NUM; i++){
					if(split[CLASS_INDEX].compareTo(CLASS_NAME[i]) == 0){
						class_num = i;
						break;
					}
				}
				class_cnt[class_num]++;

				for(int i=0; i<split.length; i++){
					if(ignore_train_index.get(i) != null)
						continue;

					if(class_map[class_num].get(split[i]) == null)
						class_map[class_num].put(split[i], 1);
					else
						class_map[class_num].put(split[i], class_map[class_num].get(split[i]) + 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		BufferedWriter bfw;
		System.out.println("Testing...");
		try {
			bfr = new BufferedReader(new FileReader(TESTING_FILE_NAME));
			bfw = new BufferedWriter(new FileWriter(SUBMISSION_FILE_NAME));
			bfw.write("Id,ARSON,ASSAULT,BAD CHECKS,BRIBERY,BURGLARY,DISORDERLY CONDUCT,DRIVING UNDER THE INFLUENCE,DRUG/NARCOTIC,DRUNKENNESS,EMBEZZLEMENT,EXTORTION,FAMILY OFFENSES,FORGERY/COUNTERFEITING,FRAUD,GAMBLING,KIDNAPPING,LARCENY/THEFT,LIQUOR LAWS,LOITERING,MISSING PERSON,NON-CRIMINAL,OTHER OFFENSES,PORNOGRAPHY/OBSCENE MAT,PROSTITUTION,RECOVERED VEHICLE,ROBBERY,RUNAWAY,SECONDARY CODES,SEX OFFENSES FORCIBLE,SEX OFFENSES NON FORCIBLE,STOLEN PROPERTY,SUICIDE,SUSPICIOUS OCC,TREA,TRESPASS,VANDALISM,VEHICLE THEFT,WARRANTS,WEAPON LAWS");
			bfw.newLine();
			double max;
			int maxi;

			bfr.readLine();
			while((line = bfr.readLine()) != null){
				double class_prob[] = new double[CLASS_NUM];
				for(int i=0; i<CLASS_NUM; i++)
					class_prob[i] = 1;
				
				max = maxi = -1;
				split = line.split(",");
				for(int i=0; i<CLASS_NUM; i++){
					for(int j=0; j<split.length; j++){
						if(ignore_test_index.get(j) != null)
							continue;

						if(class_map[i].get(split[j]) == null)
							class_prob[i] = 0;
						else
							class_prob[i] *= class_map[i].get(split[j]) / (double)class_cnt[i];
					}

					class_prob[i] *= class_cnt[i] / (double)(class_cnt[0]+class_cnt[1]);
					if(max < class_prob[i]){
						max = class_prob[i];
						maxi = i;
					}
				}
				bfw.write(split[0]);
				for(int i=0; i<CLASS_NUM; i++) {
					if (i == maxi)
						bfw.write(",1");
					else
						bfw.write(",0");
				}
				bfw.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}