/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Camunda License 1.0. You may not use this file
 * except in compliance with the Camunda License 1.0.
 */
package org.camunda.community.migration.adapter.execution.variable;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GlobalVariableTypingRule extends AbstractVariableTypingRule {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalVariableTypingRule.class);

  @Override
  protected boolean contextMatches(VariableTypingContext context) {
    return variableNameMatches(context);
  }

  protected boolean variableNameMatches(VariableTypingContext context) {
    return variableName() == null || variableName().equals(context.getVariableName());
  }

  protected abstract String variableName();

  public static class SimpleGlobalVariableTypingRule extends GlobalVariableTypingRule {
    private final String variableName;
    private final ObjectMapper objectMapper;
    private final Class<?> targetType;

    public SimpleGlobalVariableTypingRule(
        String variableName, ObjectMapper objectMapper, Class<?> targetType) {
      this.variableName = variableName;
      this.objectMapper = objectMapper;
      this.targetType = targetType;
    }

    @Override
    protected String variableName() {
      return variableName;
    }

    @Override
    protected ObjectMapper objectMapper() {
      return objectMapper;
    }

    @Override
    protected Class<?> targetType() {
      return targetType;
    }
  }
}
