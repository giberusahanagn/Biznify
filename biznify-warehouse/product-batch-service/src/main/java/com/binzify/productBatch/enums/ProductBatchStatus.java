package com.binzify.productBatch.enums;

public enum ProductBatchStatus {
	ACTIVE("Active"), STORED("Stored"), DISCARDED("Discarded");

	private final String displayName;

	ProductBatchStatus(String displayName) {
		this.displayName = displayName;
	}
	 @Override
	    public String toString() {
	        return displayName;
	    }
	
	 public String getDisplayName() {
		    return displayName;
		}
}
