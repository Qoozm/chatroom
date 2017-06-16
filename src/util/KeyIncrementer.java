package util;

public class KeyIncrementer {

	private static final char MIN_DATA = '0';
	private static final char MAX_DATA = '9';
	private static final char START_DATA = '1';
	private static final String DEFAULT_KEYHEAD = "";
	private int keyLength = 6;
	private String keyHead = null;
	
	public KeyIncrementer() {
		
		this.keyHead = DEFAULT_KEYHEAD;
	}
	
	public KeyIncrementer(String keyHead, int keyLength) {
		
		this.keyHead = keyHead;
		this.keyLength = keyLength;
	}
	
	public String nextKey(String currentKey) {
		
		StringBuilder strKey = new StringBuilder(keyHead);
		currentKey = currentKey.trim();
		int index = keyHead.length();
		String data = currentKey.substring(index, currentKey.length());
		Long valueOfData = Long.valueOf(data);
		valueOfData++;
		
		if (valueOfData < splitDataPosition()) {
			
			for (int i = 0; i < keyLength - String.valueOf(valueOfData).length(); i++) {
				strKey.append(MIN_DATA);
			}
			strKey.append(valueOfData);
		}
		else if (valueOfData >= splitDataPosition() && valueOfData < maxValueOfData()) {
			strKey.append(valueOfData);
		}
		else {
			return null;
		}
		return strKey.toString();
	}
	
	public String initStartKey() {
		
		StringBuilder strKey = new StringBuilder();
		
		strKey.append(keyHead);
		for (int i = 0; i < keyLength - 1; i++) {
			strKey.append(MIN_DATA);
		}
		strKey.append(START_DATA);
		
		return strKey.toString();
	}
	
	public long splitDataPosition() {
		
		StringBuilder strKey = new StringBuilder();
		
		strKey.append(START_DATA);
		for (int i = 0; i < keyLength - 1; i++) {
			strKey.append(MIN_DATA);
		}
		return Long.valueOf(strKey.toString());
	}
	
	public long maxValueOfData() {
		
		StringBuilder strkey = new StringBuilder();
		
		for (int i = 0; i < keyLength; i++) {
			strkey.append(MAX_DATA);
		}
		
		return Long.valueOf(strkey.toString());
	}
	 
}
