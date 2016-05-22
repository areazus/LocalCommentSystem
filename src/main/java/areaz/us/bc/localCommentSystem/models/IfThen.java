package areaz.us.bc.localCommentSystem.models;

import java.util.HashMap;

public class IfThen {
	public static final String[] fieldKeys = {"Verification?", "VFlag", "SAP", "UEH", "FAFSA DATE", "EFC STATUS", "EFC", "COA", "TOTAL AID", "UNMET NEED", "Loan Applied", "Loan Offered", "Loan Disbursed", "Initial CheckList", "Summer Enrollment", "Summer Balance", "Fall Enrollment", "Fall Balance", "Spring Enrollment", "Spring Balance", "VerificationStatus", "VerificationStatusDate"}; 
	public static final String[] conditionKeys = {"equals to", "greater than", "less than", "not equals to", "not greater than", "not less than", "starts with", "starts with", "matches regex"}; 
	public static final String[] highlightsKeys = {"Green", "Blue", "Yellow", "Orange", "Pale Blue", "Pink"};
	public static final HashMap<Integer, String> fieldValues = new HashMap<Integer, String>();
	public static final HashMap<Integer, String> conditionValues = new HashMap<Integer, String>();
	public static final HashMap<Integer, String> highlightsValues = new HashMap<Integer, String>();

	static{
		for(int i=0; i<fieldKeys.length; i++){
			fieldValues.put(i, fieldKeys[i]);
		}
		for(int i=0; i<conditionKeys.length; i++){
			conditionValues.put(i, conditionKeys[i]);
		}
		for(int i=0; i<highlightsKeys.length; i++){
			highlightsValues.put(i, highlightsKeys[i]);
		}
	}
	
	
	public final int field, condition, highlight, ID;
	public final String forp, value;
	
	public IfThen(int ID, int field, String forp, int conition, int highlight, String value){
		this.ID = ID;
		this.field = field;
		this.forp = forp;
		this.condition = conition;
		this.highlight = highlight;
		this.value = value;
	}
	
	public String toString(){
		return "if "+
				fieldKeys[field]
						+" for "+forp
						+" "+conditionKeys[condition]
								+" "+value+", highlight "+
								highlightsKeys[highlight];
	}

}
