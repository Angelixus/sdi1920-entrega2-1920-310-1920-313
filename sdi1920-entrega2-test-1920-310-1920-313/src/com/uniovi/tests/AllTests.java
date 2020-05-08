package com.uniovi.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LogInTest.class, LogOutTest.class, SignUpTest.class, UserListTest.class })
public class AllTests {

}
