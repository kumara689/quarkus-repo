package org.kithsiri.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class SmartIxApp {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
