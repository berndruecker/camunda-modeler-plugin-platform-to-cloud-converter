package org.camunda.community.migration.processInstance.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.camunda.community.migration.processInstance.core.dto.ActivityInstanceDto;
import org.camunda.community.migration.processInstance.core.dto.Camunda7Version;
import org.camunda.community.migration.processInstance.core.dto.HistoricActivityInstance.HistoricActivityInstanceQueryResultDto;
import org.camunda.community.migration.processInstance.core.dto.ProcessDefinitionDto;
import org.camunda.community.migration.processInstance.core.dto.ProcessInstanceDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Camunda7Client {
  private static final String PROCESS_INSTANCE = "/process-instance/{id}";
  private static final String SUSPEND_PROCESS_INSTANCE = PROCESS_INSTANCE + "/suspended";
  private static final String PROCESS_INSTANCE_VARIABLES = PROCESS_INSTANCE + "/variables";
  private static final String ACTIVITY_INSTANCES = PROCESS_INSTANCE + "/activity-instances";
  private static final String PROCESS_DEFINITION = "/process-definition/{id}";
  private static final String VERSION = "/version";
  private static final String PROCESS_DEFINITION_SUSPENDED = "/process-instance/suspended";
  private static final String HISTORY = "/history";
  private static final String HISTORIC_ACTIVITY_INSTANCE =
      HISTORY + "/activity-instance?processInstanceId={processInstanceId}";
  private static final String PROCESS_INSTANCE_VARIABLE =
      "/process-instance/{id}/variables/{varName}";
  private final RestTemplate restTemplate;

  public Camunda7Client(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Camunda7Version getVersion() {
    return restTemplate.getForObject(VERSION, Camunda7Version.class);
  }

  public void suspendProcessInstance(String camunda7ProcessInstanceId, boolean suspended) {
    restTemplate.put(
        SUSPEND_PROCESS_INSTANCE,
        Collections.singletonMap("suspended", suspended),
        Collections.singletonMap("id", camunda7ProcessInstanceId));
  }

  public void suspendProcessDefinitionByKey(String bpmnProcessId, boolean suspended) {
    Map<String, Object> body = new HashMap<>();
    body.put("processDefinitionKey", bpmnProcessId);
    body.put("suspended", suspended);
    restTemplate.put(PROCESS_DEFINITION_SUSPENDED, body);
  }

  public ProcessInstanceDto getProcessInstance(String processInstanceId) {
    return restTemplate.getForObject(
        PROCESS_INSTANCE,
        ProcessInstanceDto.class,
        Collections.singletonMap("id", processInstanceId));
  }

  public ProcessDefinitionDto getProcessDefinition(String processDefinitionId) {
    return restTemplate.getForObject(
        PROCESS_DEFINITION,
        ProcessDefinitionDto.class,
        Collections.singletonMap("id", processDefinitionId));
  }

  public Map<String, Object> getVariables(String processInstanceId) {
    return restTemplate
        .exchange(
            PROCESS_INSTANCE_VARIABLES,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            Collections.singletonMap("id", processInstanceId))
        .getBody();
  }

  public ActivityInstanceDto getActivityInstances(String processInstanceId) {
    return restTemplate.getForObject(
        ACTIVITY_INSTANCES,
        ActivityInstanceDto.class,
        Collections.singletonMap("id", processInstanceId));
  }

  public HistoricActivityInstanceQueryResultDto getHistoricActivityInstances(
      String processInstanceId) {
    return restTemplate.getForObject(
        HISTORIC_ACTIVITY_INSTANCE,
        HistoricActivityInstanceQueryResultDto.class,
        Collections.singletonMap("processInstanceId", processInstanceId));
  }

  public void cancelProcessInstance(String processInstanceId) {
    restTemplate.delete(PROCESS_INSTANCE, Collections.singletonMap("id", processInstanceId));
  }

  public void setVariable(String processInstanceId, String variableName, Object variableValue) {
    Map<String, Object> body = new HashMap<>();
    body.put("value", variableValue);
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("id", processInstanceId);
    uriVariables.put("varName", variableName);
    restTemplate.put(PROCESS_INSTANCE_VARIABLE, body, uriVariables);
  }
}
