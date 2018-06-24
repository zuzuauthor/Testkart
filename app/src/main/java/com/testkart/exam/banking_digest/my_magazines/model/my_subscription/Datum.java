
package com.testkart.exam.banking_digest.my_magazines.model.my_subscription;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

    @SerializedName("package_id")
    @Expose
    private String packageId;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("magazines")
    @Expose
    private List<Magazine> magazines = null;
    private final static long serialVersionUID = 8998523381897539973L;

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

    public List<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(List<Magazine> magazines) {
        this.magazines = magazines;
    }

}
