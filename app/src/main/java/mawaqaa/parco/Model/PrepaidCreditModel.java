package mawaqaa.parco.Model;

/**
 * Created by HP on 4/16/2018.
 */

public class PrepaidCreditModel {

    String creditId, creditAmount;

    public PrepaidCreditModel(String creditId, String creditAmount) {
        this.creditId = creditId;
        this.creditAmount = creditAmount;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }
}
