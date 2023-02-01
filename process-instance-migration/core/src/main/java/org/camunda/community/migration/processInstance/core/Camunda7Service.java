package org.camunda.community.migration.processInstance.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.camunda.community.migration.processInstance.core.dto.ActivityInstanceDto;
import org.camunda.community.migration.processInstance.core.dto.Camunda7ProcessData;
import org.camunda.community.migration.processInstance.core.dto.ProcessDefinitionDto;
import org.camunda.community.migration.processInstance.core.dto.ProcessInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Camunda7Service {
  private final Camunda7Client camunda7Client;

  @Autowired
  public Camunda7Service(Camunda7Client camunda7Client) {
    this.camunda7Client = camunda7Client;
  }

  public Camunda7ProcessData getProcessData(String camunda7ProcessInstanceId) {
    Camunda7ProcessData processData = new Camunda7ProcessData();
    // process definition key
    ProcessInstanceDto processInstance =
        camunda7Client.getProcessInstance(camunda7ProcessInstanceId);
    ProcessDefinitionDto processDefinition =
        camunda7Client.getProcessDefinition(processInstance.getDefinitionId());
    processData.setProcessDefinitionKey(processDefinition.getKey());
    // variables
    Map<String, Object> variables = camunda7Client.getVariables(camunda7ProcessInstanceId);
    processData.setProcessVariables(variables);
    // activity ids
    ActivityInstanceDto activities = camunda7Client.getActivityInstances(camunda7ProcessInstanceId);
    processData.setActivityIds(extractFromTree(activities));
    return processData;
  }

  private List<String> extractFromTree(ActivityInstanceDto activityInstanceDto) {
    if (activityInstanceDto.getChildActivityInstances() == null
        || activityInstanceDto.getChildActivityInstances().isEmpty()) {
      return Collections.singletonList(activityInstanceDto.getActivityId());
    } else {
      return activityInstanceDto.getChildActivityInstances().stream()
          .flatMap(dto -> extractFromTree(dto).stream())
          .collect(Collectors.toList());
    }
  }

  public void suspendProcessDefinition(String bpmnProcessId, boolean suspended) {
    camunda7Client.suspendProcessDefinitionByKey(bpmnProcessId, suspended);
  }

  public void cancelProcessInstance(
      String camunda7ProcessInstanceId, Long camunda8ProcessInstanceKey) {
    camunda7Client.setVariable(
        camunda7ProcessInstanceId, "camunda8ProcessInstanceKey", camunda8ProcessInstanceKey);
    camunda7Client.cancelProcessInstance(camunda7ProcessInstanceId);
  }
}
