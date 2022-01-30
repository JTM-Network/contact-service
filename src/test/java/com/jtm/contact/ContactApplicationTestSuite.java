package com.jtm.contact;

import com.jtm.contact.data.service.MessageServiceTest;
import com.jtm.contact.entrypoint.controller.MessageControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MessageServiceTest.class,
        MessageControllerTest.class,
})
public class ContactApplicationTestSuite {}