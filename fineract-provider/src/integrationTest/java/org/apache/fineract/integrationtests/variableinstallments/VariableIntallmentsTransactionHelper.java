package org.apache.fineract.integrationtests.variableinstallments;

import java.util.HashMap;
import java.util.Map;

import org.apache.fineract.integrationtests.common.Utils;

import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;


@SuppressWarnings("rawtypes")
public class VariableIntallmentsTransactionHelper {

    private final String URL = "https://localhost:8443/mifosng-provider/api/v1/loans/" ;
    
    private final RequestSpecification requestSpec;
    private final ResponseSpecification responseSpec;
    
    public VariableIntallmentsTransactionHelper(final RequestSpecification requestSpec, 
            final ResponseSpecification responseSpec) {
        this.requestSpec = requestSpec ;
        this.responseSpec = responseSpec ;
    }
    
    
    public Map retrieveSchedule(Integer loanId) {
        String url = URL+loanId+"?associations=repaymentSchedule&exclude=guarantors&"+Utils.TENANT_IDENTIFIER ;
        return Utils.performServerGet(requestSpec, responseSpec, url, "");
    }
    
    public HashMap validateVariations(final String exceptions, Integer loanId) {
        String url = URL+loanId+"/schedule?command=calculateLoanSchedule&"+Utils.TENANT_IDENTIFIER ;
        return Utils.performServerPost(this.requestSpec, this.responseSpec, url, exceptions, "");
    }
    
    public HashMap submitVariations(final String exceptions, Integer loanId) {
        String url = URL+loanId+"/schedule?command=addVariations&"+Utils.TENANT_IDENTIFIER ;
        return Utils.performServerPost(this.requestSpec, this.responseSpec, url, exceptions, "");
    }
}
