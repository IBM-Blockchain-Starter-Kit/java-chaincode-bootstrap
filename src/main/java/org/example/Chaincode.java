/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.printf(e.toString());
            return newErrorResponse(e);
        }
    }
    
    private Response create(ChaincodeStub stub, List<String> params) {
        System.out.printf("CREATE");
        return newSuccessResponse("CREATE");
    }

    private Response read(ChaincodeStub stub, List<String> params) {
        System.out.printf("READ");
        return newSuccessResponse("READ");
    }

    private Response update(ChaincodeStub stub, List<String> params) {
        System.out.printf("UPDATE");
        return newSuccessResponse("UPDATE");
    }

    private Response delete(ChaincodeStub stub, List<String> params) {
        System.out.printf("DELETE");
        return newSuccessResponse("DELETE");
    }

}
