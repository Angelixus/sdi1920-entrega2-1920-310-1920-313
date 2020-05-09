package com.uniovi.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FriendRequestTest.class, FriendTest.class, LogInTest.class, LogOutTest.class, SecurityTest.class,
		SignUpTest.class, UserListTest.class })
public class AllTests {

}
