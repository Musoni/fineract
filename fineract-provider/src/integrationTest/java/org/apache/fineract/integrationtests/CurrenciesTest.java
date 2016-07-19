package org.apache.fineract.integrationtests;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.fineract.integrationtests.common.CurrenciesHelper;
import org.apache.fineract.integrationtests.common.CurrencyDomain;
import org.apache.fineract.integrationtests.common.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

@SuppressWarnings({ "unused", "rawtypes" })
public class CurrenciesTest {

    private ResponseSpecification responseSpec;
    private RequestSpecification requestSpec;

    @Before
    public void setup() {
        Utils.initializeRESTAssured();
        this.requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        this.requestSpec.header("Authorization", "Basic " + Utils.loginIntoServerAndGetBase64EncodedAuthenticationKey());
        this.responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    @Test
    public void testCurrencyElements() {

        CurrencyDomain currency = CurrenciesHelper.getCurrencybyCode(requestSpec, responseSpec, "USD");
        CurrencyDomain usd = CurrencyDomain.create("USD", "US Dollar", 2, "$", "currency.USD", "US Dollar ($)").build();

        Assert.assertTrue(currency.getDecimalPlaces() >= 0);
        Assert.assertNotNull(currency.getName());
        Assert.assertNotNull(currency.getDisplaySymbol());
        Assert.assertNotNull(currency.getDisplayLabel());
        Assert.assertNotNull(currency.getNameCode());

        Assert.assertEquals(usd, currency);
    }

    @Test
    public void testUpdateCurrencySelection() {

        // Test updation
        ArrayList<String> currenciestoUpdate = new ArrayList<String>();
        currenciestoUpdate.add("KES");
        currenciestoUpdate.add("BND");
        currenciestoUpdate.add("LBP");
        currenciestoUpdate.add("GHC");
        currenciestoUpdate.add("USD");

        ArrayList<String> currenciesOutput = CurrenciesHelper.updateSelectedCurrencies(this.requestSpec, this.responseSpec,
                currenciestoUpdate);
        Assert.assertNotNull(currenciesOutput);

        Assert.assertEquals("Verifying Do Outputed Currencies Match after Updation", currenciestoUpdate, currenciesOutput);

        // Test that output matches updation
        ArrayList<CurrencyDomain> currenciesBeforeUpdate = new ArrayList<CurrencyDomain>();
        for (String e : currenciestoUpdate) {
            currenciesBeforeUpdate.add(CurrenciesHelper.getCurrencybyCode(requestSpec, responseSpec, e));
        }
        Collections.sort(currenciesBeforeUpdate);

        ArrayList<CurrencyDomain> currenciesAfterUpdate = CurrenciesHelper.getSelectedCurrencies(requestSpec, responseSpec);
        Assert.assertNotNull(currenciesAfterUpdate);

        Assert.assertEquals("Verifying Do Selected Currencies Match after Updation", currenciesBeforeUpdate, currenciesAfterUpdate);
    }
}