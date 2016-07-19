package org.apache.fineract.mix.report.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.mix.service.XBRLResultServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReadTaxonomyMappingServiceImplTest {

    private XBRLResultServiceImpl readService;

    @Before
    public void setUp() throws Exception {
        final RoutingDataSource dataSource = Mockito.mock(RoutingDataSource.class);
        this.readService = new XBRLResultServiceImpl(dataSource, null, null);

    }

    @Test
    public void shouldCorrectlyGetGLCode() {
        final ArrayList<String> result = this.readService.getGLCodes("{12000}+{11000}");
        assertEquals("12000", result.get(0));
        assertEquals("11000", result.get(1));
    }

}
