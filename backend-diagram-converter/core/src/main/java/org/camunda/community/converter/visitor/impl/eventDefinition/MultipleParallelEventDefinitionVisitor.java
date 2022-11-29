package org.camunda.community.converter.visitor.impl.eventDefinition;

import org.camunda.community.converter.DomElementVisitorContext;
import org.camunda.community.converter.version.SemanticVersion;
import org.camunda.community.converter.visitor.AbstractEventDefinitionVisitor;

public class MultipleParallelEventDefinitionVisitor extends AbstractEventDefinitionVisitor {
  @Override
  public String localName() {
    return "multipleParallelEventDefinition";
  }

  @Override
  protected SemanticVersion availableFrom(DomElementVisitorContext context) {
    return null;
  }
}
