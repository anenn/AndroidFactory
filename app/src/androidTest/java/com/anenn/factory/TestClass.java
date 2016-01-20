package com.anenn.factory;

import android.test.InstrumentationTestCase;

import java.util.UUID;

/**
 * Created by Anenn on 2016/1/3.
 */
public class TestClass extends InstrumentationTestCase {

    public void test() throws Exception {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());
    }
}
