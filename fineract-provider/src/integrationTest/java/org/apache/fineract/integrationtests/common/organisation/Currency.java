package org.apache.fineract.integrationtests.common.organisation;

public class Currency {

    String code;
    String name;
    Integer decimalPlaces;
    String displaySymbol;
    String nameCode;
    String displayLabel;

    public Currency(String code, String name, Integer decimalPlaces, String displaySymbol,
                    String nameCode, String displayLabel) {
        this.code = code;
        this.name = name;
        this.decimalPlaces = decimalPlaces;
        this.displaySymbol = displaySymbol;
        this.nameCode = nameCode;
        this.displayLabel = displayLabel;
    }

    public boolean isValid() {
        return (this.code != null && this.code.length() == 3) &&
                (this.name != null && this.name.length() > 0) &&
                (this.decimalPlaces != null && this.decimalPlaces >= 0) &&
                (this.nameCode != null && this.nameCode.startsWith("currency.")) &&
                (this.displayLabel != null && this.displayLabel.length() > 0);

    }
}
