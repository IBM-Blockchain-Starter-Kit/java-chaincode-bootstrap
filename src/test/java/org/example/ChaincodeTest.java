/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.Chaincode.Response.Status;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.Test;

public class ChaincodeTest {

    @Test
    public void testInit() {
        Chaincode cc = new Chaincode();
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(stub.getFunction()).thenReturn("initFunc");
        when(stub.getParameters()).thenReturn(new ArrayList<String>());
        Response res = cc.init(stub);
        assertEquals(Status.SUCCESS, res.getStatus());
    }

    @Test
    public void testInvoke() {
        Chaincode cc = new Chaincode();
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(stub.getFunction()).thenReturn("initFunc");
        when(stub.getParameters()).thenReturn(new ArrayList<String>());
        Response res = cc.init(stub);
        assertEquals(Status.SUCCESS, res.getStatus());
        when(stub.getFunction()).thenReturn("CreateAsset");
        when(stub.getParameters()).thenReturn(new ArrayList<String>());
        res = cc.invoke(stub);
        // TODO need to stub more now!!
//        assertEquals(Status.SUCCESS, res.getStatus());
    }

}
