package org.apache.fineract.mix.report.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.fineract.mix.data.MixTaxonomyData;
import org.apache.fineract.mix.data.NamespaceData;
import org.apache.fineract.mix.service.NamespaceReadPlatformServiceImpl;
import org.apache.fineract.mix.service.XBRLBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RunWith(MockitoJUnitRunner.class)
public class XBRLBuilderTest {

    @Mock
    private NamespaceReadPlatformServiceImpl readNamespaceService;
    @InjectMocks
    private final XBRLBuilder xbrlBuilder = new XBRLBuilder();

    @Before
    public void setUp() throws Exception {

        this.readNamespaceService = Mockito.mock(NamespaceReadPlatformServiceImpl.class);
        when(this.readNamespaceService.retrieveNamespaceByPrefix(Matchers.anyString())).thenReturn(
                new NamespaceData(1l, "mockedprefix", "mockedurl"));

    }

    @SuppressWarnings("null")
    @Test
    public void shouldCorrectlyBuildMap() {

        final HashMap<MixTaxonomyData, BigDecimal> map = new HashMap<MixTaxonomyData, BigDecimal>();
        final MixTaxonomyData data1 = Mockito.mock(MixTaxonomyData.class);
        when(data1.getName()).thenReturn("Assets");
        map.put(data1, new BigDecimal(10000));
        final String result = this.xbrlBuilder.build(map, Date.valueOf("2005-11-11"), Date.valueOf("2013-07-17"), "USD");
        System.out.println(result);
        NodeList nodes = null;
        try {
            nodes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(result.getBytes()))
                    .getElementsByTagName("Assets");
        } catch (final SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(nodes);
        assertNotNull(nodes.item(0));
        assertEquals("Assets", nodes.item(0).getNodeName());
    }

}
