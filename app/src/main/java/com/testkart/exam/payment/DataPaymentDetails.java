package com.testkart.exam.payment;

import com.testkart.exam.payment.model.Package;
import com.testkart.exam.payment.model.Payment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by testkart on 21/6/17.
 */

class DataPaymentDetails implements Serializable{

    private Payment payment;
    private List<Package> packageList;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Package> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<Package> packageList) {
        this.packageList = packageList;
    }
}
