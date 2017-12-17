/**
 * 
 * © Copyright ConsenSys 2017
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eth.ent.identity.logging.jlogadapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import eth.ent.identity.logging.Logger;
import eth.ent.identity.logging.LoggerFactory;

public class JLogLoggerTest {

  private Handler handlerSpy;
  private Logger log;

  @Before public void beforeEach() {
    LoggerFactory.setLogImplementation(new JLogLoggerFactory());
    handlerSpy = Mockito.spy(Handler.class);
    log = LoggerFactory.getLogger(this.getClass());
    java.util.logging.Logger jLogLogger = ((JLogLogger) log).getJLogLogger();
    jLogLogger.addHandler(handlerSpy);
    jLogLogger.setUseParentHandlers(false);
    jLogLogger.setLevel(Level.ALL);
  }

  @After public void afterEach() {
    LogManager.getLogManager().getLogger("").removeHandler(handlerSpy);
  }

  @Test public void testInfo() {
    log.info("testInfo");
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.INFO, logRecord.getLevel());
        assertEquals("testInfo", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

  @Test public void testInfoWithArgs() {
    log.info("testInfo", "arg1", null);
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.INFO, logRecord.getLevel());
        assertEquals("testInfo [arg1, null]", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

  @Test public void testDebug() {
    log.debug("testDebug");
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.FINE, logRecord.getLevel());
        assertEquals("testDebug", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

  @Test public void testDebugWithArgs() {
    log.debug("testInfo", "arg1", null);
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.FINE, logRecord.getLevel());
        assertEquals("testInfo [arg1, null]", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

  @Test public void testErrorNoException() {
    log.error("testErrorNoException");
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.SEVERE, logRecord.getLevel());
        assertEquals("testErrorNoException", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

  @Test public void testErrorExceptionAndArgs() {
    try {
      throw new RuntimeException();
    } catch (Throwable e) {
      log.error("testErrorExceptionAndArgs", e, "arg1", "arg2");
      verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
        @Override public boolean matches(LogRecord logRecord) {
          assertEquals(Level.SEVERE, logRecord.getLevel());
          Pattern patt = Pattern.compile("^testErrorExceptionAndArgs \\[arg1, arg2\\]: java.lang.RuntimeException",
              Pattern.MULTILINE);
          assertTrue(logRecord.getMessage() + " matches " + patt, patt.matcher(logRecord.getMessage()).find());
          assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
          return true;
        }
      }));
    }
  }

  @Test public void testNullData() {
    log.error("testNullData", (Object[]) null);
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.SEVERE, logRecord.getLevel());
        assertEquals("testNullData [null]", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

  @Test public void testNullException() {
    Exception exToLog = null;
    log.error("testNullException", exToLog);
    verify(handlerSpy).publish(argThat(new ArgumentMatcher<LogRecord>() {
      @Override public boolean matches(LogRecord logRecord) {
        assertEquals(Level.SEVERE, logRecord.getLevel());
        assertEquals("testNullException: null", logRecord.getMessage());
        assertEquals(JLogLoggerTest.class.getName(), logRecord.getSourceClassName());
        return true;
      }
    }));
  }

}
