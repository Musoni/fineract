package org.apache.fineract.integrationtests;

import java.util.HashMap;

import org.apache.fineract.integrationtests.common.ClientHelper;
import org.apache.fineract.integrationtests.common.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

public class ClientTest {

    private ResponseSpecification responseSpec;
    private RequestSpecification requestSpec;
    private ClientHelper clientHelper;

    @Before
    public void setup() {
        Utils.initializeRESTAssured();
        this.requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        this.requestSpec.header("Authorization", "Basic " + Utils.loginIntoServerAndGetBase64EncodedAuthenticationKey());
        this.responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();

    }

    @Test
    public void testClientStatus() {
        this.clientHelper = new ClientHelper(this.requestSpec, this.responseSpec);
        final Integer clientId = ClientHelper.createClient(this.requestSpec, this.responseSpec);
        Assert.assertNotNull(clientId);

        HashMap<String, Object> status = ClientHelper.getClientStatus(requestSpec, responseSpec, String.valueOf(clientId));
        ClientStatusChecker.verifyClientIsActive(status);

        HashMap<String, Object> clientStatusHashMap = this.clientHelper.closeClient(clientId);
        ClientStatusChecker.verifyClientClosed(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.reactivateClient(clientId);
        ClientStatusChecker.verifyClientPending(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.rejectClient(clientId);
        ClientStatusChecker.verifyClientRejected(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.activateClient(clientId);
        ClientStatusChecker.verifyClientActiavted(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.closeClient(clientId);
        ClientStatusChecker.verifyClientClosed(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.reactivateClient(clientId);
        ClientStatusChecker.verifyClientPending(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.withdrawClient(clientId);
        ClientStatusChecker.verifyClientWithdrawn(clientStatusHashMap);

    }
    
    @Test
    public void testClientAsPersonStatus() {
    	
    	this.clientHelper = new ClientHelper(this.requestSpec, this.responseSpec);
        final Integer clientId = ClientHelper.createClientAsPerson(this.requestSpec, this.responseSpec);
        Assert.assertNotNull(clientId);

        HashMap<String, Object> status = ClientHelper.getClientStatus(requestSpec, responseSpec, String.valueOf(clientId));
        ClientStatusChecker.verifyClientIsActive(status);

        HashMap<String, Object> clientStatusHashMap = this.clientHelper.closeClient(clientId);
        ClientStatusChecker.verifyClientClosed(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.reactivateClient(clientId);
        ClientStatusChecker.verifyClientPending(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.rejectClient(clientId);
        ClientStatusChecker.verifyClientRejected(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.activateClient(clientId);
        ClientStatusChecker.verifyClientActiavted(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.closeClient(clientId);
        ClientStatusChecker.verifyClientClosed(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.reactivateClient(clientId);
        ClientStatusChecker.verifyClientPending(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.withdrawClient(clientId);
        ClientStatusChecker.verifyClientWithdrawn(clientStatusHashMap);

    }
    
    @Test
    public void testClientAsEntityStatus() {
    	
    	this.clientHelper = new ClientHelper(this.requestSpec, this.responseSpec);
        final Integer clientId = ClientHelper.createClientAsEntity(this.requestSpec, this.responseSpec);
        Assert.assertNotNull(clientId);

        HashMap<String, Object> status = ClientHelper.getClientStatus(requestSpec, responseSpec, String.valueOf(clientId));
        ClientStatusChecker.verifyClientIsActive(status);

        HashMap<String, Object> clientStatusHashMap = this.clientHelper.closeClient(clientId);
        ClientStatusChecker.verifyClientClosed(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.reactivateClient(clientId);
        ClientStatusChecker.verifyClientPending(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.rejectClient(clientId);
        ClientStatusChecker.verifyClientRejected(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.activateClient(clientId);
        ClientStatusChecker.verifyClientActiavted(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.closeClient(clientId);
        ClientStatusChecker.verifyClientClosed(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.reactivateClient(clientId);
        ClientStatusChecker.verifyClientPending(clientStatusHashMap);

        clientStatusHashMap = this.clientHelper.withdrawClient(clientId);
        ClientStatusChecker.verifyClientWithdrawn(clientStatusHashMap);

    }

}
