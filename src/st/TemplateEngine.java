package st;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Stack;

public class TemplateEngine {

    public static final Integer DELETE_UNMATCHED = 1;
    public static final Integer KEEP_UNMATCHED = 0;
    public static final Integer CASE_SENSITIVE = 2;
    public static final Integer CASE_INSENSITIVE = 0;
    public static final Integer BLUR_SEARCH = 4;
    public static final Integer ACCURATE_SEARCH = 0;
    public static final Integer DEFAULT = 0;

    private static final Character TEMPLATE_START_PREFIX = '$';
    private static final Character TEMPLATE_START = '{';
    private static final Character TEMPLATE_END = '}';

    public TemplateEngine(){

    }

<<<<<<< HEAD
	public String evaluate(String templateString, EntryMap entryMap, Integer matchingMode) {
		if (!isEvaluationPossible(templateString, entryMap)) {
			return templateString;
		}

		if (!isMatchingModeValid(matchingMode)) {
			matchingMode = Integer.valueOf(0);
		}

		HashSet<Template> templates = identifyTemplates(templateString);

		ArrayList<Template> sortedTemplates = sortTemplates(templates);

		Result result = instantiate(templateString, sortedTemplates, entryMap.getEntries(), matchingMode);

		return result.getInstancedString();
	}

	private Boolean isEvaluationPossible(String templateString, EntryMap entryMap) {
		if (templateString == null) {
			return Boolean.FALSE;
		}
		if (templateString.isEmpty()) {
			return Boolean.FALSE;
		}
		if (entryMap == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private Boolean isMatchingModeValid(Integer matchingMode) {
		if (matchingMode == null) {
			return Boolean.FALSE;
		}

		if (matchingMode < 0) {
			return Boolean.FALSE;
		}
		if (matchingMode > 7) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private Boolean keepUnmatched(Integer matchingMode) {
		if ((matchingMode & DELETE_UNMATCHED) == DELETE_UNMATCHED) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	private Boolean caseInsensative(Integer matchingMode) {
		if ((matchingMode & CASE_SENSITIVE) == CASE_SENSITIVE) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	private Boolean accurateSearch(Integer matchingMode) {
		if ((matchingMode & BLUR_SEARCH) == BLUR_SEARCH) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	private HashSet<Template> identifyTemplates(String templateString) {
		HashSet<Template> templates = new HashSet<>();
		Stack<Integer> templateCandidates = new Stack<>();
		Integer charIndex = 0;
		Boolean underSequence = Boolean.FALSE;
		while (charIndex < templateString.length()) {
			if (Character.compare(templateString.charAt(charIndex), TEMPLATE_START_PREFIX) == 0) {
				underSequence = Boolean.TRUE;
				charIndex++;
				continue;
			}
			if (Character.compare(templateString.charAt(charIndex), TEMPLATE_START) == 0) {
				if (underSequence) {
					templateCandidates.add(charIndex);
				}
				underSequence = Boolean.FALSE;
				charIndex++;
				continue;
			}
			if (Character.compare(templateString.charAt(charIndex), TEMPLATE_END) == 0) {
				if (!templateCandidates.isEmpty()) {
					Template template;
					Integer startIndex = templateCandidates.pop();
					if ((startIndex + 1) == charIndex) {
						template = new Template(startIndex, charIndex, "");
					} else {
						template = new Template(startIndex, charIndex,
								templateString.substring(startIndex + 1, charIndex));
					}
					templates.add(template);
				}
				underSequence = Boolean.FALSE;
				charIndex++;
				continue;
			}
			underSequence = Boolean.FALSE;
			charIndex++;
		}
		return templates;
	}

	private ArrayList<Template> sortTemplates(HashSet<Template> templates) {
		ArrayList<Template> sortedTemplates = new ArrayList<>();
		Template currentTemplate;
		Integer minLength;
		Integer startIndex;
		while (!templates.isEmpty()) {
			currentTemplate = null;
			minLength = Integer.MAX_VALUE;
			startIndex = Integer.MAX_VALUE;
			for (Template current : templates) {
				if (current.getContent().length() < minLength) {
					currentTemplate = current;
					minLength = current.getContent().length();
					startIndex = current.getStartIndex();
				} else {
					if (current.getContent().length() == minLength) {
						if (current.getStartIndex() < startIndex) {
							currentTemplate = current;
							minLength = current.getContent().length();
							startIndex = current.getStartIndex();
						}
					}
				}
			}
			if (currentTemplate != null) {
				templates.remove(currentTemplate);
				sortedTemplates.add(currentTemplate);
			} else {
				throw new RuntimeException();
			}
		}
		return sortedTemplates;
	}

	private Result instantiate(String instancedString, ArrayList<Template> sortedTemplates,
			ArrayList<EntryMap.Entry> sortedEntries, Integer matchingMode) {
		
		Integer templatesReplaced = 0;
		Boolean replaceHappened;
		Template currentTemplate;
		EntryMap.Entry currentEntry;

		String replacement = "";
		String specialReplacement = "";
		String x_string;
		int x_int = 0;
		int yearForUse = 0;
		EntryMap.Entry base_entry;


		
		
		for (Integer i = 0; i < sortedTemplates.size(); i++) {
			currentTemplate = sortedTemplates.get(i);
			replaceHappened = Boolean.FALSE;
			for (Integer j = 0; j < sortedEntries.size(); j++) {
				currentEntry = sortedEntries.get(j);
				
				
				Template tempT = new Template(1, 6, "year") ; 
				
				if (isAMatch(currentTemplate, currentEntry, matchingMode)) {
										
					if (isAMatch(tempT, currentEntry, matchingMode)) {
						
						// System.out.println("match: " + tempT.content + " (" + currentEntry.pattern + " " + currentEntry.getValue() + ")");
						
						String[] words = currentEntry.getValue().split("\\s+");

						if ((words.length == 3)
								&& (words[0].equalsIgnoreCase("in") && (words[2].equalsIgnoreCase("years")))) {
							x_string = words[1];

							if (isNumeric(x_string)) {
								x_int = Integer.parseInt(x_string);
								if (!(x_int < 0)) {

									if (hasBaseYear(sortedEntries)) {

										base_entry = getBaseYear_Entry(sortedEntries, matchingMode);

										if ((isNumeric((base_entry.value)))
												&& (Integer.parseInt(base_entry.value) > 0)) {
											
											yearForUse = Integer.parseInt(base_entry.value);
										} else {
											yearForUse = Calendar.getInstance().get(Calendar.YEAR);
										}
									} else {
										yearForUse = Calendar.getInstance().get(Calendar.YEAR);
									}
									replacement = getReplacement(x_int, yearForUse, "inXyears");

								} else {
									replacement = currentEntry.getValue();
								}
							} else {
								replacement = currentEntry.getValue();
							}
							specialReplacement = replacement;

						} else if ((words.length == 3)
								&& (words[1].equalsIgnoreCase("years") && (words[2].equalsIgnoreCase("ago")))) {

							x_string = words[0];

							if (isNumeric(x_string)) {
								x_int = Integer.parseInt(x_string);
								if (!(x_int < 0)) {
									if (hasBaseYear(sortedEntries)) {
										base_entry = getBaseYear_Entry(sortedEntries, matchingMode);

										if ((isNumeric((base_entry.value)))
												&& (Integer.parseInt(base_entry.value) > 0)) {
											yearForUse = Integer.parseInt(base_entry.value);
										} else {
											yearForUse = Calendar.getInstance().get(Calendar.YEAR);
										}
									} else {
										yearForUse = Calendar.getInstance().get(Calendar.YEAR);
									}
									replacement = getReplacement(x_int, yearForUse, "Xyearsago");

								} else {
									replacement = currentEntry.getValue();
								}
							} else {
								replacement = currentEntry.getValue();
							}
							specialReplacement = replacement;
						} else {
							specialReplacement = currentEntry.getValue();	
						}
						instancedString = doReplace(instancedString, currentTemplate, i, specialReplacement,
								sortedTemplates);
					} else {
						instancedString = doReplace(instancedString, currentTemplate, i, currentEntry.getValue(),
								sortedTemplates);
						replaceHappened = Boolean.TRUE;
						break;
					}
				}
			}
			if (replaceHappened) {
				templatesReplaced++;
			} else {
				if (!keepUnmatched(matchingMode)) {
					instancedString = doReplace(instancedString, currentTemplate, i, "", sortedTemplates);
				}
			}
		}
		return new Result(instancedString, templatesReplaced);
	}

	private Boolean isNumeric(String str) {
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private String getReplacement(int x, int year, String format) {
		String replacement = "";
		if (format.equals("Xyearsago")) {
			replacement = Integer.toString(year - x);
		} else if (format.equals("inXyears")) {
			replacement = Integer.toString(year + x);
		}
		return replacement;
	}

	private Boolean hasBaseYear(ArrayList<EntryMap.Entry> sortedEntries) {
		Template tempT = new Template(1,10,"base_YEAR");

		for (EntryMap.Entry entry : sortedEntries) {
			if (isAMatch(tempT, entry, TemplateEngine.DEFAULT)) { 
				return true;
			}
		}
		return false;
	}
	private EntryMap.Entry getBaseYear_Entry(ArrayList<EntryMap.Entry> sortedEntries, Integer matchingMode) {
		EntryMap.Entry result = null;
		Template tempT = new Template(1,10,"base_YEAR");
		try {
			for (EntryMap.Entry entry : sortedEntries) {
				if (isAMatch(tempT, entry, matchingMode)) {   
					result = entry;
				}
			}
		} catch (NullPointerException np) {
			return null;
		}
		return result;
	}

	private Boolean isAMatch(Template template, EntryMap.Entry entry, Integer matchingMode) {
		String leftHandSide;
		String rightHandSide;
		if (!accurateSearch(matchingMode)) {
			leftHandSide = template.getContent().replaceAll("\\s", "");
			rightHandSide = entry.getPattern().replaceAll("\\s", "");
		} else {
			leftHandSide = template.getContent();
			rightHandSide = entry.getPattern();
		}
		if (caseInsensative(matchingMode)) {
			return leftHandSide.toLowerCase().equals(rightHandSide.toLowerCase());
		} else {
			return leftHandSide.equals(rightHandSide);
		}
	}

	private String doReplace(String instancedString, Template currentTemplate, Integer currentTemplateIndex,
			String replaceValue, ArrayList<Template> sortedTemplates) {
		Integer diff = 3 + currentTemplate.getContent().length() - replaceValue.length();
		String firstHalf;
		String secondHalf;
		if (currentTemplate.getStartIndex() == 1) {
			firstHalf = "";
		} else {
			firstHalf = instancedString.substring(0, currentTemplate.getStartIndex() - 1);
		}
		if (currentTemplate.getEndIndex() == instancedString.length()) {
			secondHalf = "";
		} else {
			secondHalf = instancedString.substring(currentTemplate.getEndIndex() + 1);
		}

		StringBuilder builder = new StringBuilder();
		builder.append(firstHalf);
		builder.append(replaceValue);
		builder.append(secondHalf);
		String updatedInstancedString = builder.toString();

		Template temp = null;
		for (int i = currentTemplateIndex + 1; i < sortedTemplates.size(); i++) {
			temp = sortedTemplates.get(i);
			if ((temp.getStartIndex() < currentTemplate.getStartIndex())
					&& (temp.getEndIndex() > currentTemplate.getEndIndex())) {
				sortedTemplates.get(i).setEndIndex(temp.getEndIndex() - diff);
				sortedTemplates.get(i).setContent(updatedInstancedString
						.substring(sortedTemplates.get(i).getStartIndex() + 1, sortedTemplates.get(i).getEndIndex()));
			} else {
				if (temp.getStartIndex() > currentTemplate.getEndIndex()) {
					sortedTemplates.get(i).setStartIndex(temp.getStartIndex() - diff);
					sortedTemplates.get(i).setEndIndex(temp.getEndIndex() - diff);
				}
			}
		}
		return updatedInstancedString;
	}

	class Template {
		Integer startIndex;
		Integer endIndex;
		String content;

		Template(Integer startIndex, Integer endIndex, String content) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.content = content;
		}

		public Integer getStartIndex() {
			return startIndex;
		}

		public Integer getEndIndex() {
			return endIndex;
		}

		public String getContent() {
			return content;
		}

		public void setStartIndex(Integer startIndex) {
			this.startIndex = startIndex;
		}

		public void setEndIndex(Integer endIndex) {
			this.endIndex = endIndex;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Template template = (Template) o;

			if (getStartIndex() != null ? !getStartIndex().equals(template.getStartIndex())
					: template.getStartIndex() != null)
				return false;
			if (getEndIndex() != null ? !getEndIndex().equals(template.getEndIndex()) : template.getEndIndex() != null)
				return false;
			return getContent() != null ? getContent().equals(template.getContent()) : template.getContent() == null;
		}

		@Override
		public int hashCode() {
			int result = getStartIndex() != null ? getStartIndex().hashCode() : 0;
			result = 31 * result + (getEndIndex() != null ? getEndIndex().hashCode() : 0);
			result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
			return result;
		}
	}
=======
    public String evaluate(String templateString, EntryMap entryMap, Integer matchingMode){
        if (!isEvaluationPossible(templateString, entryMap)){
            return templateString;
        }
        
        if (!isMatchingModeValid(matchingMode)){
            matchingMode = Integer.valueOf(0);
        }

        HashSet<Template> templates = identifyTemplates(templateString);

        ArrayList<Template> sortedTemplates = sortTemplates(templates);

        Result result = instantiate(templateString, sortedTemplates, entryMap.getEntries(), matchingMode);

        return result.getInstancedString();
    }

    private Boolean isEvaluationPossible(String templateString, EntryMap entryMap){
        if (templateString == null){
            return Boolean.FALSE;
        }
        if (templateString.isEmpty()) {
            return Boolean.FALSE;
        }
        if (entryMap == null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean isMatchingModeValid(Integer matchingMode){
        if (matchingMode == null) {
            return Boolean.FALSE;
        }
        
        if (matchingMode < 0){
            return Boolean.FALSE;
        }
        if (matchingMode > 7){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean keepUnmatched(Integer matchingMode) {
        if ((matchingMode & DELETE_UNMATCHED) == DELETE_UNMATCHED) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    
    private Boolean caseInsensative(Integer matchingMode) {
        if ((matchingMode & CASE_SENSITIVE) == CASE_SENSITIVE) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private Boolean accurateSearch(Integer matchingMode) {
        if ((matchingMode & BLUR_SEARCH) == BLUR_SEARCH) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private HashSet<Template> identifyTemplates(String templateString){
        HashSet<Template> templates = new HashSet<>();
        Stack<Integer> templateCandidates = new Stack<>();
        Integer charIndex = 0;
        Boolean underSequence = Boolean.FALSE;
        while (charIndex < templateString.length()){
            if (Character.compare(templateString.charAt(charIndex), TEMPLATE_START_PREFIX) == 0){
                underSequence = Boolean.TRUE;
                charIndex++;
                continue;
            }
            if (Character.compare(templateString.charAt(charIndex), TEMPLATE_START) == 0){
                if(underSequence){
                    templateCandidates.add(charIndex);
                }
                underSequence = Boolean.FALSE;
                charIndex++;
                continue;
            }
            if (Character.compare(templateString.charAt(charIndex), TEMPLATE_END) == 0){
                if (!templateCandidates.isEmpty()){
                    Template template;
                    Integer startIndex = templateCandidates.pop();
                    if ((startIndex + 1) == charIndex){
                        template = new Template(startIndex, charIndex, "");
                    } else{
                        template = new Template(startIndex, charIndex, templateString.substring(startIndex+1, charIndex));
                    }
                    templates.add(template);
                }
                underSequence = Boolean.FALSE;
                charIndex++;
                continue;
            }
            underSequence = Boolean.FALSE;
            charIndex++;
        }
        return templates;
    }

    private ArrayList<Template> sortTemplates(HashSet<Template> templates){
        ArrayList<Template> sortedTemplates = new ArrayList<>();
        Template currentTemplate;
        Integer minLength;
        Integer startIndex;
        while (!templates.isEmpty()) {
            currentTemplate = null;
            minLength = Integer.MAX_VALUE;
            startIndex = Integer.MAX_VALUE;
            for (Template current : templates){
                if (current.getContent().length() < minLength){
                    currentTemplate = current;
                    minLength = current.getContent().length();
                    startIndex = current.getStartIndex();
                } else{
                    if (current.getContent().length() == minLength){
                        if (current.getStartIndex() < startIndex){
                            currentTemplate = current;
                            minLength = current.getContent().length();
                            startIndex = current.getStartIndex();
                        }
                    }
                }
            }
            if (currentTemplate != null) {
                templates.remove(currentTemplate);
                sortedTemplates.add(currentTemplate);
            } else{
                throw new RuntimeException();
            }
        }
        return sortedTemplates;
    }

    private Result instantiate(String instancedString, ArrayList<Template> sortedTemplates, ArrayList<EntryMap.Entry> sortedEntries, Integer matchingMode){
        Integer templatesReplaced = 0;
        Boolean replaceHappened;
        Template currentTemplate;
        EntryMap.Entry currentEntry;
        for (Integer i=0; i<sortedTemplates.size(); i++){
            currentTemplate = sortedTemplates.get(i);
            replaceHappened = Boolean.FALSE;
            for(Integer j=0; j<sortedEntries.size(); j++){
                currentEntry = sortedEntries.get(j);
                if (isAMatch(currentTemplate, currentEntry, matchingMode)){
                    instancedString = doReplace(instancedString, currentTemplate, i, currentEntry.getValue(), sortedTemplates,sortedEntries);
                    replaceHappened = Boolean.TRUE;
                    break;
                }
            }
            if(replaceHappened){
                templatesReplaced ++;
            } else{
                if(!keepUnmatched(matchingMode)){//delete the string if keep unmatched is not on
                    instancedString = doReplace(instancedString, currentTemplate, i, "", sortedTemplates,sortedEntries);
                }
            }
        }
        return new Result(instancedString, templatesReplaced);
    }

    private Boolean isAMatch(Template template, EntryMap.Entry entry, Integer matchingMode){
        String leftHandSide;
        String rightHandSide;
        if (!accurateSearch(matchingMode)) {
            leftHandSide = template.getContent().replaceAll("\\s","");
            rightHandSide = entry.getPattern().replaceAll("\\s","");
        } else {
            leftHandSide = template.getContent();
            rightHandSide = entry.getPattern();
        }
        if (caseInsensative(matchingMode)){
            return leftHandSide.toLowerCase().equals(rightHandSide.toLowerCase());
        } else{
            return leftHandSide.equals(rightHandSide);
        }
    }
    /////////////////HELPER METHODS///////////////
    private static boolean containsTemplate(String findThis,ArrayList<Template> searchThis){
    	for (Template s : searchThis){
    		if (s.getContent().equals(findThis)){
    			return true;
    		}
    	}
    	return false;
    }
    private static boolean StringisNumber(String s) {
    	for (char c : s.toCharArray()){
    		if (!Character.isDigit(c)){
    			return false;
    		}
    	}
    	return true;
    }
    private static boolean StringisPostiveNumber(String s) {
    	for (char c : s.toCharArray()){
    		if (!Character.isDigit(c)){
    			return false;
    		}
    		if (Character.getNumericValue(c)<0){
    			return false;
    		}
    	}
    	return true;
    }
    private static String returnvalue(ArrayList<EntryMap.Entry> sortedEntries,String findThis){
    	for (EntryMap.Entry e : sortedEntries){
    		System.out.println("FIND THIS " + findThis);
    		System.out.println(e.getPattern());
    		System.out.println(e.getValue());
    		if (e.getPattern().equals(findThis)){
    			return e.getValue();
    		}
    	}
    	return null;//means it wasn't found
    		
    }
    ///////////////////////////////////////////
    private String doReplace(String instancedString, Template currentTemplate, Integer currentTemplateIndex, String replaceValue, ArrayList<Template> sortedTemplates,ArrayList<EntryMap.Entry> sortedEntries){
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	Integer how_many_less=0; 
    	Integer how_many_more=0;
    	String type = "";
    	if (replaceValue.length()>=10 && replaceValue.substring(replaceValue.length()-10).equals(" years ago")&&StringisNumber(replaceValue.substring(0,replaceValue.length()-10))) {
    		how_many_less=Integer.valueOf(replaceValue.substring(0,replaceValue.length()-10));
    		type="years in past";
    	}
    	if (replaceValue.length()>=10 && replaceValue.substring(0,3).equals("in ") && replaceValue.substring(replaceValue.length()-6,replaceValue.length()).equals(" years")&&StringisNumber(replaceValue.substring(3,replaceValue.length()-6))){
    		how_many_more = Integer.valueOf(replaceValue.substring(3,replaceValue.length()-6)); //int max value 
    		type="years in future";
    	}
    	if (currentTemplate.getContent().equals("year") && replaceValue.length()>=10){//impossible to be of correct format if less than 10
    		if (type.equals("years in past")){
    			if (how_many_less>=0){//X in X years ago is greater than 0 also add a check here. what if there is no integer value of
    				if (returnvalue(sortedEntries,"base_year")!=null&&StringisNumber(returnvalue(sortedEntries,"base_year"))){
    					replaceValue=(Integer.valueOf(returnvalue(sortedEntries,"base_year"))-how_many_less)+"";
    				}
    				else {
    					replaceValue=(year-how_many_less)+"";
    				}
    			}
    		}
    		else if(type.equals("years in future")){//in X years
    			if (how_many_more>=0){
    				if (returnvalue(sortedEntries,"base_year")!=null&&StringisNumber(returnvalue(sortedEntries,"base_year"))){
    					replaceValue=(Integer.valueOf(returnvalue(sortedEntries,"base_year"))+how_many_more)+"";	
    				}
    				else{
    					replaceValue=(year+how_many_more)+"";
    				}
    			}
    		}
    	}
        Integer diff = 3 + currentTemplate.getContent().length() - replaceValue.length();
        String firstHalf;
        String secondHalf;
        if (currentTemplate.getStartIndex() == 1){
            firstHalf = "";
        } else{
            firstHalf = instancedString.substring(0, currentTemplate.getStartIndex()-1);
        }
        if (currentTemplate.getEndIndex() == instancedString.length()){
            secondHalf = "";
        } else{
            secondHalf = instancedString.substring(currentTemplate.getEndIndex()+1);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(firstHalf);
        builder.append(replaceValue);
        builder.append(secondHalf);
        String updatedInstancedString = builder.toString();

        Template temp = null;
        for (int i=currentTemplateIndex+1; i<sortedTemplates.size(); i++){
            temp = sortedTemplates.get(i);
            if ((temp.getStartIndex() < currentTemplate.getStartIndex()) && (temp.getEndIndex() > currentTemplate.getEndIndex()))
            {
                sortedTemplates.get(i).setEndIndex(temp.getEndIndex() - diff);
                sortedTemplates.get(i).setContent(updatedInstancedString.substring(sortedTemplates.get(i).getStartIndex()+1, sortedTemplates.get(i).getEndIndex()));
            } else {
                if (temp.getStartIndex() > currentTemplate.getEndIndex()) {
                    sortedTemplates.get(i).setStartIndex(temp.getStartIndex() - diff);
                    sortedTemplates.get(i).setEndIndex(temp.getEndIndex() - diff);
                }
            }
        }
        return updatedInstancedString;
    }

    class Template {
        Integer startIndex;
        Integer endIndex;
        String content;

        Template(Integer startIndex, Integer endIndex, String content) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.content = content;
        }

        public Integer getStartIndex() {
            return startIndex;
        }

        public Integer getEndIndex() {
            return endIndex;
        }

        public String getContent() {
            return content;
        }

        public void setStartIndex(Integer startIndex) {
            this.startIndex = startIndex;
        }

        public void setEndIndex(Integer endIndex) {
            this.endIndex = endIndex;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Template template = (Template) o;

            if (getStartIndex() != null ? !getStartIndex().equals(template.getStartIndex()) : template.getStartIndex() != null)
                return false;
            if (getEndIndex() != null ? !getEndIndex().equals(template.getEndIndex()) : template.getEndIndex() != null)
                return false;
            return getContent() != null ? getContent().equals(template.getContent()) : template.getContent() == null;
        }

        @Override
        public int hashCode() {
            int result = getStartIndex() != null ? getStartIndex().hashCode() : 0;
            result = 31 * result + (getEndIndex() != null ? getEndIndex().hashCode() : 0);
            result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
            return result;
        }
    }
>>>>>>> 1d43c98029fa94ab3003c41fec31889b03c78e6b
    
    class Result{
        String instancedString;
        Integer templatesReplaced;

        Result(String instancedString, Integer templatesReplaced) {
            this.instancedString = instancedString;
            this.templatesReplaced = templatesReplaced;
        }

        String getInstancedString() {
            return instancedString;
        }

        Integer getTemplatesReplaced() {
            return templatesReplaced;
        }
    }
<<<<<<< HEAD
} // final
=======
}
>>>>>>> 1d43c98029fa94ab3003c41fec31889b03c78e6b
