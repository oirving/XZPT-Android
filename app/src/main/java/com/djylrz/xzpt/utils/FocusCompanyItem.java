package com.djylrz.xzpt.utils;

public class FocusCompanyItem {
    int companyLoge;
    String companyName;

    /**
     *
     * @param companyLoge 企业logo
     * @param companyName 企业名称
     */
    public FocusCompanyItem(int companyLoge, String companyName) {
        this.companyLoge = companyLoge;
        this.companyName = companyName;
    }

    public int getCompanyLoge() {
        return companyLoge;
    }

    public String getCompanyName() {
        return companyName;
    }
}
