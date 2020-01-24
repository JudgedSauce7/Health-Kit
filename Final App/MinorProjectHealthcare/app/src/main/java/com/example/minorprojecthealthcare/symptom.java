package com.example.minorprojecthealthcare;

class symptom {
    private String SymName;
    private boolean isSelected;

    public String getSymName() {
        return SymName;
    }

    public void setSymName(String symName) {
        SymName = symName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean checked) {
        isSelected = checked;
    }

    @Override
    public String toString() {
        return "symptom{" +
                "SymName='" + SymName + '\'' +
                '}';
    }
    public symptom(String symName) {
        SymName = symName;
    }
}
