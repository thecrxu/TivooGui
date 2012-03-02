package model;

import java.util.*;

public abstract class TivooEventType implements Comparable<TivooEventType> {

    public abstract String toString();
    
    @SuppressWarnings("serial")
    private static Set<String> commonAttributes = new HashSet<String>() {{
	add("Title"); add("Description"); add("Start Time"); add("End Time");
    }};
    
    private Set<String> specialAttributes = new HashSet<String>();
    
    public static Set<String> getCommonAttributes() {
	return Collections.unmodifiableSet(commonAttributes);
    }
    
    public Set<String> getSpecialAttributes() {
	return Collections.unmodifiableSet(specialAttributes);
    }
    
    protected void addSpecialAttributes(Set<String> theset) {
	specialAttributes.addAll(theset);
    }
    
    public int compareTo(TivooEventType t) {
	return toString().compareTo(t.toString());
    }
    
    public boolean equals(Object o) {
	TivooEventType t = (TivooEventType) o;
	return toString().equals(t.toString());
    }
    
    public int hashCode() {
	return toString().hashCode();
    }
    
}