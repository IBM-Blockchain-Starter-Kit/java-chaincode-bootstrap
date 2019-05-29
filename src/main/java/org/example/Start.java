/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;

public final class Start {

    private Start() {
    }

    public static void main(final String[] args) {
        new Chaincode().start(args);
    }

}
