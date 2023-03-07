package org.camunda.community.migration.processInstance.api.model;

import org.camunda.community.migration.processInstance.api.model.chunk.ActivityNodeData;

public interface TaskData extends ActivityNodeData {
  interface TaskDataBuilder extends ActivityNodeDataBuilder<TaskDataBuilder, TaskData> {}
}
