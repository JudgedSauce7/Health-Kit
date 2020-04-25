package com.example.minorprojecthealthcare;

class DrugSample {
    private String DrugId;
    private String DrugName;
    private String TargetDisease;

    public String getDrugId() {
        return DrugId;
    }

    public void setDrugId(String drugId) {
        DrugId = drugId;
    }

    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }

    public String getTargetDisease() {
        return TargetDisease;
    }

    public void setTargetDisease(String targetDisease) {
        TargetDisease = targetDisease;
    }

    @Override
    public String toString() {
        return "DrugId='" + DrugId + '\'' +
                ", DrugName='" + DrugName + '\'' +
                ", TargetDisease='" + TargetDisease + '\'' +
                '}';
    }
}
