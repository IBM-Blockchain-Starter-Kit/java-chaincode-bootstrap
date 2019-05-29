/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.owlike.genson.Genson;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class Chaincode extends ChaincodeBase {

    private enum Transaction {
        CREATE_ASSET("CreateAsset"), READ_ASSET("ReadAsset"), UPDATE_ASSET("UpdateAsset"), DELETE_ASSET("DeleteAsset");

        private final String name;

        private static final Map<String, Transaction> BY_NAME = new HashMap<String, Transaction>();

        static {
            for (Transaction type : Transaction.values()) {
                BY_NAME.put(type.name, type);
            }
        }

        private Transaction(String name) {
            this.name = name;
        }

        public static Transaction fromName(String name) {
            if (BY_NAME.containsKey(name)) {
                return BY_NAME.get(name);
            }
            throw new IllegalArgumentException("no such transaction: " + name);
        }
    }

    @Override
    public Response init(ChaincodeStub stub) {
        String fcn = stub.getFunction();
        List<String> params = stub.getParameters();
        System.out.printf("init() %s %s\n", fcn, params.toArray());
        return ChaincodeBase.newSuccessResponse();
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            String fcn = stub.getFunction();
            List<String> params = stub.getParameters();
            System.out.printf("invoke() %s %s\n", fcn, params.toArray());

            Transaction txn = Transaction.fromName(fcn);
            switch (txn) {
                case CREATE_ASSET:
                    return create(stub, params);
                case READ_ASSET:
                    return read(stub, params);
                case UPDATE_ASSET:
                    return update(stub, params);
                case DELETE_ASSET:
                    return delete(stub, params);
                default:
                    return newErrorResponse();
            }
                
        } catch (Throwable e) {
            System.out.println(e.toString());
            return newErrorResponse(e);
        }
    }
    
    private Response create(ChaincodeStub stub, List<String> params) {
        System.out.println("CREATE");
        String key = params.get(0);
        String value = params.get(1);
        
        String assetState = stub.getStringState(key);
        if (!assetState.isEmpty()) {
            String errorMessage = String.format("Asset %s already exists", key);
            System.out.println(errorMessage);
            return newErrorResponse(errorMessage);
        }
        
        Genson genson = new Genson();
        Asset asset = new Asset(value);
        assetState = genson.serialize(asset);
        stub.putStringState(key, assetState);

        String successMessage = String.format("Created asset %s", key);
        return newSuccessResponse(successMessage, assetState.getBytes(UTF_8));
    }

    private Response read(ChaincodeStub stub, List<String> params) {
        System.out.println("READ");
        String key = params.get(0);
        
        String assetState = stub.getStringState(key);

        if (assetState.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", key);
            System.out.println(errorMessage);
            return newErrorResponse(errorMessage);
        }
        
        Genson genson = new Genson();
        Asset asset = genson.deserialize(assetState, Asset.class);
        
        String successMessage = String.format("Read asset %s", key);
        return newSuccessResponse(successMessage, genson.serialize(asset).getBytes(UTF_8));
    }

    private Response update(ChaincodeStub stub, List<String> params) {
        System.out.println("UPDATE");
        String key = params.get(0);
        String value = params.get(1);
        
        String assetState = stub.getStringState(key);

        if (assetState.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", key);
            System.out.println(errorMessage);
            return newErrorResponse(errorMessage);
        }
        
        Genson genson = new Genson();
        Asset asset = new Asset(value);
        assetState = genson.serialize(asset);
        stub.putStringState(key, assetState);
        
        String successMessage = String.format("Updated asset %s", key);
        return newSuccessResponse(successMessage, genson.serialize(asset).getBytes(UTF_8));
    }

    private Response delete(ChaincodeStub stub, List<String> params) {
        System.out.println("DELETE");
        String key = params.get(0);
        
        String assetState = stub.getStringState(key);

        if (assetState.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", key);
            System.out.println(errorMessage);
            return newErrorResponse(errorMessage);
        }
        
        stub.delState(key);
        
        String successMessage = String.format("Deleted asset %s", key);
        return newSuccessResponse(successMessage);
    }

}
