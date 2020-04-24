import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;

class DataTransformer  {
	
/* The global constants used in the program */	
public static int ROWCOUNT = 0; // rowCount of table
public static String calEquation = null; // equation used for calculation

/* ArrayList of ArrayList used for storing dataTable in the program */	
public static ArrayList<ArrayList<String>> dataTable = new ArrayList<>(ROWCOUNT);
/* ArrayList used for storing column name in the program */	
public static ArrayList<String> columnList =new ArrayList<String>();


/* Used to clear the entire object 
 * of dataTable. 
 */	
public static boolean clear() {
	dataTable.clear(); // using clear method to erase the object of dataTable
	return true;
}

/* Used to add a new 
 *  column to the dataTable 
 */	
public static boolean newColumn(String columnNameAdded) {
	String columnName = columnNameAdded.trim(); //removing all the spaces
	String columnNameWithoutSpace = columnName.replaceAll("\\s+",""); // remove all white spacing from the operation
	
	int hasNext = 0;
	for(String col : columnList) {               // checking whether column already exist or not
		if(col.equalsIgnoreCase(columnNameAdded)) {
			hasNext++;
		}
		
	}
	
	if(columnName.length() > 0 && hasNext == 0) {
		columnList.add(columnNameWithoutSpace);		//adding to the column name arrayList
		if(dataTable.size() > 0) {
			for (int i = 0; i < dataTable.size(); i++) { 
		        for (int j = 0; j < 1; j++) {
		        	if(i == 0) {
		        		dataTable.get(i).add(columnNameWithoutSpace); //adding column to the main dataTable
		        	}
		        } 
		    } 
		}else {
	    	System.out.println("No datatable exist.No column added.");
	    	return false;
	    }
		
	} else {
		System.out.println("Column not Added.Pls check input or Column name already exist");
		return false;
	}
	
	return true;
}

/* Used to calculate the operations
 * for  new or existing
 *  column added  to the dataTable 
 */	
public static int calculate(String calEquation) {
	String calculateEquation = calEquation.trim();
	boolean isEquationPresent = false;
	boolean isColumnNameOperand1 = false;
	boolean isColumnNameOperand2 = false;
	String[] operands = null;
	String[]  seperatedByEquals = null;
	String regexNumber = "\\d+";
	 boolean isConstant = false;
	 boolean isOneColumn = false;
	 boolean isConstantNegative = false;
	 String reg = "[+\\-*/]"; 												//  Regex equation
	 String withoutSpace = "";
	 boolean isEqualPresent = calculateEquation.contains("=");
	if(calculateEquation.length() > 0 && isEqualPresent == true) {
		isEquationPresent = true;
		seperatedByEquals = calculateEquation.split("=");		  // Separate by equals
		 withoutSpace = seperatedByEquals[1].replaceAll("\\s+",""); // remove all white spacing from the operation
		 operands = withoutSpace.split(reg);								  // split with operator
	}	
		if(calculateEquation.length() > 0 && isEqualPresent == true && operands.length >1 ) {										// check if column name is valid or not
			if(operands.length >1 ) {
				 isColumnNameOperand1 = columnList.contains(operands[0]);
				 isColumnNameOperand2 = columnList.contains(operands[1]);
				 if(operands[0].equals(seperatedByEquals[0]) || operands[1].equals(seperatedByEquals[0]) || columnList.contains(seperatedByEquals[0]) == false) {
					 isEquationPresent = false;
					 System.out.println("Invalid column");
				 }
			}
			
		} else {
			if(calculateEquation.length() > 0 && isEqualPresent == true) {
				isConstant = operands[0].matches(regexNumber);			//check for operation with single operand			
				isOneColumn = columnList.contains(operands[0]);
				if(isOneColumn == false && isConstant == false) {
					System.out.println("Invalid columns");
					isEquationPresent = false;
				}
				System.out.println("Finally" +isConstant +operands[0].matches("\\-"));
				if(isConstant == true && operands[0].matches("\\-")) {
					isConstantNegative = true;
				}
			}
			
		}
		if(isEquationPresent== true && isEqualPresent == true) {
			if(!columnList.contains(seperatedByEquals[0])) {
				System.out.println("Invalid columns");
				isEquationPresent = false;
			} 
		}
		
				 
	if(ROWCOUNT > 1 && dataTable.size()> 0 && isEquationPresent== true && seperatedByEquals.length>0 && isEqualPresent == true) { 		//operation calculation begins
		double[] operatorValue1 = new double[ROWCOUNT -1]; //Array to store first operand column
		double[] operatorValue2 = new double[ROWCOUNT -1]; //Array to store first operand column
		ArrayList<Double> alOp1 = new ArrayList<Double>(); //ArrayList to store first operand column
		ArrayList<Double> alOp2 = new ArrayList<Double>(); //ArrayList to store second operand column
		ArrayList<Double> alSol = new ArrayList<Double>(); //ArrayList to store solution of operation
		
		if( withoutSpace.contains("*") ) {	
			if(isColumnNameOperand1 == true && isColumnNameOperand2 == true) {
				for (int i = 1; i < dataTable.size(); i++) { 		//adding values to array for operand 1
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[0])));
	            	operatorValue1[i-1] = j1;
	        }
			for (int i = 1; i < dataTable.size(); i++) {  			//adding values to array for operand 2
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[1])));
	            	operatorValue2[i-1] = j1;
	        }
			for(int i=0;i<operatorValue1.length;i++) {				//adding to the arraylist and performing the calculation
				for(int j=0;j<operatorValue2.length;j++) {
					alOp1.add(operatorValue1[i]);
					alOp2.add(operatorValue2[j]);
					if(i==j) {
						alSol.add(operatorValue1[i]*operatorValue2[j]);
					}
				}
			}
			// storing in dataTable								// storing thr resulting arraylist in dataTable
				int hasNext = 1 ; 
				for(double elem : alSol){
				dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
				hasNext++;
			}
			alOp1.clear();
			alOp2.clear();
			alSol.clear();
			}
			
		else if (isColumnNameOperand1 == true || isColumnNameOperand2 == true){ 
			// for one operand as column name and one operand as constant
			// index for column name and numeric data  
			int columnNameIndex = 0;
			int numericIndex =0;
				if(isColumnNameOperand1) {
					columnNameIndex = 0;
					numericIndex=1;
				} else {
					columnNameIndex = 1;
					numericIndex = 0;
				}
				// initializing the value of ArrayList in array
				for (int i = 1; i < dataTable.size(); i++) {
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[columnNameIndex])));
	            	operatorValue1[i-1] = j1;
	        }
				// Calculation
				for(int i=0;i<operatorValue1.length;i++) {
					alOp1.add(operatorValue1[i]);
						alSol.add(operatorValue1[i]*Double.parseDouble(operands[numericIndex]));
				}
				// storing in dataTable
				int hasNext = 1 ; 
				for(double elem : alSol){
					dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
						hasNext++;
				}
		} else {
			for(int i=0;i<ROWCOUNT-1;i++) {
				alSol.add(Double.parseDouble(operands[0]) *Double.parseDouble(operands[1]));
		}
			// storing in dataTable
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
					hasNext++;
			}
		}
		}
		 
		if( withoutSpace.contains("-") && isConstantNegative == false) {
			if(isColumnNameOperand1 == true && isColumnNameOperand2 == true) {
				//storing in the array the value of two operands
				for (int i = 1; i < dataTable.size(); i++) {
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[0])));
	            	operatorValue1[i-1] = j1;
	        }
			for (int i = 1; i < dataTable.size(); i++) { 
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[1])));
	            	operatorValue2[i-1] = j1;
	        }
			//calculation in arrayList and storing the result in another arraylist
			for(int i=0;i<operatorValue1.length;i++) {
				for(int j=0;j<operatorValue2.length;j++) {
					alOp1.add(operatorValue1[i]);
					alOp2.add(operatorValue2[j]);
					if(i==j) {
						alSol.add(operatorValue1[i]-operatorValue2[j]);
					}
				}
			}
			//storing the arrayList in main dataTable
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
					hasNext++;
			}
			}else if (isColumnNameOperand1 == true || isColumnNameOperand2 == true){ 
				System.out.println("F R H");
				// index for column name and numeric data
				//one numeric and one column name operation
				int columnNameIndex = 0;
				int numericIndex =0;
					if(isColumnNameOperand1) {
						columnNameIndex = 0;
						numericIndex=1;
					} else {
						columnNameIndex = 1;
						numericIndex = 0;
					}
					// initializing the value of ArrayList in array
					for (int i = 1; i < dataTable.size(); i++) {
		            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[columnNameIndex])));
		            	operatorValue1[i-1] = j1;
		        }
					// Calculation
					for(int i=0;i<operatorValue1.length;i++) {
						alOp1.add(operatorValue1[i]);
							alSol.add(operatorValue1[i]-Double.parseDouble(operands[numericIndex]));
					}
					// storing in dataTable
					int hasNext = 1 ; 
					for(double elem : alSol){
						dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
							hasNext++;
					}
			}
			 else {
					for(int i=0;i<ROWCOUNT-1;i++) {
						alSol.add(Double.parseDouble(operands[0]) - Double.parseDouble(operands[1]));
				}
					// storing in dataTable
					int hasNext = 1 ; 
					for(double elem : alSol){
						dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
							hasNext++;
					}
				}
			alOp1.clear();
			alOp2.clear();
			alSol.clear();
			}
		 
		if( withoutSpace.contains("/")) {
					
			if(isColumnNameOperand1 == true && isColumnNameOperand2 == true) {
				// storing the two operand in two arrays
				for (int i = 1; i < dataTable.size(); i++) {
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[0])));
	            	operatorValue1[i-1] = j1;
	        }
			for (int i = 1; i < dataTable.size(); i++) { 
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[1])));
	            	operatorValue2[i-1] = j1;
	        }
			//calculation
			for(int i=0;i<operatorValue1.length;i++) {
				for(int j=0;j<operatorValue2.length;j++) {
					alOp1.add(operatorValue1[i]);
					alOp2.add(operatorValue2[j]);
					if(i==j) {
						alSol.add(operatorValue1[i]/operatorValue2[j]);
					}
				}
			}
			// storing in dataTable
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
					hasNext++;
			}
			} else if(isColumnNameOperand1 == true || isColumnNameOperand2 == true) {
				// index for column name and numeric data
						int columnNameIndex = 0;
							int numericIndex =0;
								if(isColumnNameOperand1) {
									columnNameIndex = 0;
									numericIndex=1;
								} else {
									columnNameIndex = 1;
									numericIndex = 0;
								}
								if(Integer.parseInt(operands[numericIndex]) != 0) {
									// initializing the value of ArrayList in array
									for (int i = 1; i < dataTable.size(); i++) {
						            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[columnNameIndex])));
						            	operatorValue1[i-1] = j1;
						        }
									// Calculation
									for(int i=0;i<operatorValue1.length;i++) {
										alOp1.add(operatorValue1[i]);
											alSol.add(operatorValue1[i]/Double.parseDouble(operands[numericIndex]));
									}
									// storing in dataTable
									int hasNext = 1 ; 
									for(double elem : alSol){
										dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
											hasNext++;
									}
								} else {
									for(int i=0;i<ROWCOUNT-1;i++) {
										alSol.add(Double.parseDouble(operands[numericIndex]));
								}
								int hasNext = 1 ; 
								for(double elem : alSol){
									dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
										hasNext++;
								}	
								}
									
								
			}else {
				for(int i=0;i<ROWCOUNT-1;i++) {
					alSol.add(Double.parseDouble(operands[0]) / Double.parseDouble(operands[1]));
			}
				// storing in dataTable
				int hasNext = 1 ; 
				for(double elem : alSol){
					dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
						hasNext++;
				}
			}
			
			alOp1.clear();
			alOp2.clear();
			alSol.clear();
			}
		 
		if( withoutSpace.contains("+") ) {
			if(isColumnNameOperand1 == true && isColumnNameOperand2 == true) {
				//adding the operands in the array
				for (int i = 1; i < dataTable.size(); i++) {
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[0])));
	            	operatorValue1[i-1] = j1;
	        }
			for (int i = 1; i < dataTable.size(); i++) { 
	            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[1])));
	            	operatorValue2[i-1] = j1;
	        }
			//calculation
			for(int i=0;i<operatorValue1.length;i++) {
				for(int j=0;j<operatorValue2.length;j++) {
					alOp1.add(operatorValue1[i]);
					alOp2.add(operatorValue2[j]);
					if(i==j) {
						alSol.add(operatorValue1[i]+operatorValue2[j]);
					}
				}
			}
			//storing in dataTable
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
					hasNext++;
			}
			}else if(isColumnNameOperand1 == true || isColumnNameOperand2 == true) {
				// index for column name and numeric data
				int columnNameIndex = 0;
				int numericIndex =0;
					if(isColumnNameOperand1) {
						columnNameIndex = 0;
						numericIndex=1;
					} else {
						columnNameIndex = 1;
						numericIndex = 0;
					}
					// initializing the value of ArrayList in array
					for (int i = 1; i < dataTable.size(); i++) {
		            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[columnNameIndex])));
		            	operatorValue1[i-1] = j1;
		        }
					// Calculation
					for(int i=0;i<operatorValue1.length;i++) {
						alOp1.add(operatorValue1[i]);
							alSol.add(operatorValue1[i]+Double.parseDouble(operands[numericIndex]));
					}
					// storing in dataTable
					int hasNext = 1 ; 
					for(double elem : alSol){
						dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
							hasNext++;
					}
			}
			 else {
					for(int i=0;i<ROWCOUNT-1;i++) {
						alSol.add(Double.parseDouble(operands[0]) + Double.parseDouble(operands[1]));
				}
					// storing in dataTable
					int hasNext = 1 ; 
					for(double elem : alSol){
						dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
							hasNext++;
					}
				}
			alOp1.clear();
			alOp2.clear();
			alSol.clear();
			}
		if(isConstant == true) {
			//if the column data added is constant
			for(int i=0;i<ROWCOUNT-1;i++) {
					alSol.add(Double.parseDouble(operands[0]));
			}
			//storing
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add(String.valueOf(Math.round(elem)));
					hasNext++;
			}		
			
		}
		if(isConstantNegative == true) {
			//for negative constant
			for(int i=0;i<ROWCOUNT-1;i++) {
					alSol.add(Double.parseDouble(operands[0]));
			}	
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add("-" +String.valueOf(Math.round(elem)));
					hasNext++;
			}		
			
		}
		if(isOneColumn == true) {
			//if new column value is equal to an old column 
			for (int i = 1; i < dataTable.size(); i++) {
            	int j1 = Integer.parseInt(dataTable.get(i).get(columnList.indexOf(operands[0])));
            	operatorValue1[i-1] = j1;
        }
			for(int i=0;i<operatorValue1.length;i++) {
				alOp1.add(operatorValue1[i]);
					alSol.add(operatorValue1[i]);
			}
			//storing in dataTable
			int hasNext = 1 ; 
			for(double elem : alSol){
				dataTable.get(hasNext).add( String.valueOf(Math.round(elem)));
					hasNext++;
			}
		}
	} else {
		System.out.println("No Rows or database doesnot exist or Invalid Equation or no operator exists");
		return 0;
	}
	
	return ROWCOUNT -1;
}

/* Used to print top
 * 5 rows to the dataTable 
 */	
public static void top() {
	if(dataTable.size() > 0) {
		for (int i = 0; i < 6; i++) { 									//iterating the arrayList of arraylist to print top 5 rows
	        for (int j = 0; j < dataTable.get(i).size(); j++) { 
	        	 System.out.print(dataTable.get(i).get(j) + "\t");
	        } 
	        System.out.println();
	    }
	} else {
		System.out.println("No data on table");
	}
}

/* Used to print the 
 *  entire dataTable 
 */	
public static void print() {
	if(dataTable.size() > 0) {
		for (int i = 0; i < dataTable.size(); i++) { 					//iterating the arrayList of arrayList to print entire dataTable
	        for (int j = 0; j < dataTable.get(i).size(); j++) { 
	        	 System.out.print(dataTable.get(i).get(j) + "\t");
	        } 
	        System.out.println();
	    }
	} else {
		System.out.println("No data on table");
	}
}

/* Used to write the 
 *  entire dataTable 
 to the file mentioned*/
public static int write(String fileName) {
	ROWCOUNT = 0;
	try {
		FileOutputStream fileOutput = new FileOutputStream(fileName); 	//created an object for FileOutputStream
		PrintWriter pw = new PrintWriter(fileOutput);
		for (int i = 0; i < dataTable.size(); i++) { 
	        for (int j = 0; j < dataTable.get(i).size(); j++) { 	//	writing in file with \t tab spacing in between columns
	        	pw.print(dataTable.get(i).get(j) + "\t");
	        } pw.println();
	        /*if( dataTable.get(i).size() > 10) {
	        	break;
	        }*/
	        ROWCOUNT++;
	    }pw.close();
	}
	catch (Exception e) {
	}
	
	return ROWCOUNT;
}


/* Used to read the 
 *  entire dataTable 
 to the file mentioned*/
public static int read(String fileName) {
	
	String fileReadInput = "";
	BufferedReader fileInputData = null;
	ROWCOUNT = 0;	
	
	try {
		File file = new File(fileName);
		fileName = file.getName();
		if(file.exists()) {													//check if file is actually present
		fileInputData = new BufferedReader(new FileReader(file));
		if(file.length()!=0) {												//check if file has data
		while ((fileReadInput = fileInputData.readLine())!= null) { 
			dataTable.add(new ArrayList<String>());									//initializing the arrayList of arrayList
			String Data = fileReadInput;
			String[] dataTableItems = Data.split("\\s+");
			for(int i = 0; i< dataTableItems.length; i++) {					// maintaining the identation while reading 
				dataTable.get(ROWCOUNT).add(dataTableItems[i].toString());	// storing the values in dataAtable
				if(ROWCOUNT == 0) {
					columnList.add(dataTableItems[i].toString());			// storing the values in column List
				}
				/*if(dataTableItems.length >= 10) {
					System.out.println("Printing atmost 10 columns");    	// Printing only 10 columns
					break;
				}*/
			}
			
			ROWCOUNT++;
		}				 
		}
		else {
			System.out.println("File is empty !");
			}
		}
		else{
			System.out.println("Error ! File not found");
		}
	fileInputData.close();
} catch (Exception e) {
//System.out.println( e.getMessage() );
		}
	return ROWCOUNT;
}
}