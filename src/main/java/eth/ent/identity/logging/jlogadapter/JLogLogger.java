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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple JLog implementation of logging
 */
public class JLogLogger implements eth.ent.identity.logging.Logger {

  final Logger log;
  final String targetClassName;

  public JLogLogger(Class<?> clazz) {
    targetClassName = clazz.getName();
    log = Logger.getLogger(targetClassName);
  }

  protected Logger getJLogLogger() {
    return log;
  }

  private String buildString(String message, Object... data) {
    StringBuilder logMessage = new StringBuilder();
    logMessage.append(message);
    if (data == null) {
      logMessage.append(" [null]");
    } else if (data.length > 0) {
      logMessage.append(' ');
      logMessage.append(Arrays.asList(data).toString());
    }
    return logMessage.toString();
  }

  private String getStackTrace(Throwable ex) {
    if (ex == null) {
      return "null";
    }
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }

  @Override public void info(String message, Object... data) {
    log.logp(Level.INFO, targetClassName, null, buildString(message, data));
  }

  @Override public void debug(String message, Object... data) {
    log.logp(Level.FINE, targetClassName, null, buildString(message, data));
  }

  @Override public void error(String message, Object... data) {
    log.logp(Level.SEVERE, targetClassName, null, buildString(message, data));
  }

  @Override public void error(String message, Throwable ex, Object... data) {
    log.logp(Level.SEVERE, targetClassName, null, buildString(message, data) + ": " + getStackTrace(ex));
  }

}
