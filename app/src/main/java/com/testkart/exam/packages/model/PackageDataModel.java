package com.testkart.exam.packages.model;

import java.io.Serializable;

/**
 * Created by elfemo on 8/9/17.
 */

public class PackageDataModel implements Serializable{

    private String packageId;
    private String packageName;
    private boolean isPackageChecked;

    public boolean isPackageChecked() {
        return isPackageChecked;
    }

    public void setPackageChecked(boolean packageChecked) {
        isPackageChecked = packageChecked;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
