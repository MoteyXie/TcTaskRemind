package com.motey.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.motey.UI.TaskNode;
import com.motey.model.MyProperty;
import com.motey.model.TaskModel;
import com.motey.model.TaskTypeModel;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.loose.workflow.WorkflowService;
import com.teamcenter.services.loose.workflow._2014_06.Workflow;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.exceptions.NotLoadedException;


public class SoaTaskUtil {
	
	public static final String DO_TASK_COMPLETED_ACTION = "SOA_EPM_complete_action";
	public static final String DO_TASK_FAILED_ACTION = "SOA_EPM_fail_action";
	public static final String REVIEW_TASK_APPROVED_ACTION = "SOA_EPM_approve_action";
	public static final String REVIEW_TASK_REJECTED_ACTION = "SOA_EPM_reject_action";
	public static final String REVIEW_TASK_APPROVED_RESULT = "SOA_EPM_approve";
	public static final String REVIEW_TASK_REJECTED_RESULT = "SOA_EPM_reject";
	
	public static Map<ModelObject, ModelObject[]> flowChildsCache = new HashMap<>();
	public static Map<ModelObject, ArrayList<Integer[]>> taskRelatedArrayCache = new HashMap<>();
	
	public static Map<ModelObject,  ArrayList<TaskNode>> flowTaskNodeCache = new HashMap<>();
	private static ArrayList<Integer[]> taskRelatedArray;
	private static HashMap<String, ModelObject> childTaskMap;
	private static ModelObject[] startTasks;
	private static ModelObject[] childTasks;
	private static ModelObject[] completeTask;
	
	public static void doTaskComplete(ModelObject modelObject, String comment){
		doTaskAction(modelObject, DO_TASK_COMPLETED_ACTION, comment);
	}
	
	public static void doTaskFailed(ModelObject modelObject, String comment){
		doTaskAction(modelObject, DO_TASK_FAILED_ACTION, comment);
	}
	
	public static void reviewTaskApproved(ModelObject modelObject, String comment){
		reviewTaskAction(modelObject, REVIEW_TASK_APPROVED_ACTION, REVIEW_TASK_APPROVED_RESULT, comment);
	}
	
	public static void reviewTaskRejected(ModelObject modelObject, String comment){
		reviewTaskAction(modelObject, REVIEW_TASK_REJECTED_ACTION, REVIEW_TASK_REJECTED_RESULT, comment);
	}
	
	/**
	 * 条件节点
	 * @param modelObject 任务对象
	 * @param condition 条件，可通过此类中的getCondition()方法获取所有条件
	 * @param comment 注释
	 */
	@SuppressWarnings("deprecation")
	public static void conditionTaskAction(ModelObject modelObject, String condition, String comment){
		
		WorkflowService localWorkflowService = WorkflowService.getService(AppXSession.getConnection());
		Workflow.ApplySignatureInput[] arrayOfApplySignatureInput = new Workflow.ApplySignatureInput[0];
		
		localWorkflowService.performActionWithSignature(
				modelObject,
				DO_TASK_COMPLETED_ACTION, 
				comment,
				null,
				condition, 
				modelObject, 
				arrayOfApplySignatureInput);
		
	}
	
	/**
	 * 审核节点
	 * @param modelObject
	 * @param action
	 * @param result
	 * @param comment
	 */
	@SuppressWarnings("deprecation")
	public static void reviewTaskAction(ModelObject modelObject, String action, String result, String comment){
		
		MyProperty.refreshObject(modelObject);
		ModelObject userSignOff = getUserSignOff(modelObject);
		WorkflowService localWorkflowService = WorkflowService.getService(AppXSession.getConnection());
		Workflow.ApplySignatureInput[] arrayOfApplySignatureInput = new Workflow.ApplySignatureInput[0];
		
		localWorkflowService.performActionWithSignature(
				modelObject,
				action,
				comment,
				null,
				result, 
				userSignOff, 
				arrayOfApplySignatureInput);
		
	}
	
	/**
	 *
	 * @param taskObject
	 * @return
	 */
	private static ModelObject getUserSignOff(ModelObject taskObject) {
		
		try {
			MyProperty.refreshObject(taskObject);
			ModelObject[] mos = MyProperty.getModelObjectArrayProperty(taskObject, "user_all_signoffs");
			for (ModelObject mo : mos) {
//				if(MySession.getUserName().equals(MyProperty.getStringProperty(mo, "fnd0Assignee")))
				return mo;	
				
			}
		} catch (NotLoadedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param taskModel 要执行的任务 ，必须为DO节点
	 * @param command 执行动作  DO_TASK_COMPLETED | DO_TASK_FAILED
	 * @param comment 注释
	 */
	@SuppressWarnings("deprecation")
	public static void doTaskAction(ModelObject modelObject, String command, String comment){
		MyProperty.refreshObject(modelObject);
		WorkflowService localWorkflowService = WorkflowService.getService(AppXSession.getConnection());
		Workflow.ApplySignatureInput[] arrayOfApplySignatureInput = new Workflow.ApplySignatureInput[0];
		
		localWorkflowService.performActionWithSignature(
				modelObject,
				command, comment, null,
				"SOA_EPM_completed", 
				modelObject, 
				arrayOfApplySignatureInput);
		
	}
	
	public static String[] getConditions(ModelObject taskObject){
		
		List<String> conditions = new ArrayList<>();
		
		MyProperty.refreshObject(taskObject);
		
		try {
			ModelObject template = MyProperty.getModelObjectProperty(taskObject, "task_template");
			ModelObject[] successors = MyProperty.getModelObjectArrayProperty(taskObject, "successors");
			
			
			for(int i = 0; i < successors.length; i++){
				
				String condition = getCheckCondition(template, successors[i]);
				if(condition != null)conditions.add(condition);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conditions.toArray(new String[conditions.size()]);
	}
	
	public static String getCheckCondition(ModelObject template, ModelObject successor) throws Exception{
		
		String templateName = MyProperty.getStringProperty(template, "object_name");
		templateName = "-source_task="+templateName;
		ModelObject successorTemplate = MyProperty.getModelObjectProperty(successor, "task_template");
		ModelObject[] actions = MyProperty.getModelObjectArrayProperty(successorTemplate, "actions");
		ModelObject[] rules = null;
		ModelObject[] handlers = null;
		
		for(int i = 0; i < actions.length; i++){
			
			rules = MyProperty.getModelObjectArrayProperty(actions[i], "rules");
			if(rules == null || rules.length == 0)continue; 
			
			for(int j = 0; j < rules.length; j++){
				
				handlers = MyProperty.getModelObjectArrayProperty(rules[j], "rule_handlers");
				
				for(int k = 0; k < handlers.length; k++){
					
					if("EPM-check-condition".equals(MyProperty.getStringProperty(handlers[k], "object_name"))){
						
						String arguments = MyProperty.getStringProperty(handlers[k], "arguments");
						if(arguments.contains(templateName)){
							String a = arguments.replace(templateName, "").trim();
							a = a.replace("-decision=", "").trim();
							return a;
						}
						
					}
				}
				
			}
		}
	    
		return null;
	}
	
	public static List<Integer[]> getTaskRelatedArray(){
		return taskRelatedArray;
	}
	
	public static ArrayList<TaskNode> getFlowTaskNodes(ModelObject amount) throws Exception{
		
		if(flowTaskNodeCache.containsKey(amount)){
			return flowTaskNodeCache.get(amount);
		}
		
		ArrayList<TaskNode> results = new ArrayList<>();
		
		ModelObject flow = MyProperty.getModelObjectProperty(amount, "root_task");
		
		ModelObject flowTemp = MyProperty.getModelObjectProperty(flow, "task_template");
		
		startTasks = MyProperty.getModelObjectArrayProperty(flowTemp, "start_successors");
		childTasks = MyProperty.getModelObjectArrayProperty(flow, "child_tasks");
		completeTask = MyProperty.getModelObjectArrayProperty(flowTemp, "complete_predecessors");
		
		if(childTaskMap == null){
			childTaskMap = new HashMap<String, ModelObject>();
		}else{
			childTaskMap.clear();
		}
		
		for (ModelObject childTask : childTasks) {
			childTaskMap.put(MyProperty.getStringProperty(childTask, "object_name"), childTask);
		}
		
		TaskNode t0 = createTaskNode("开始");
		results.add(t0);
		
		for (ModelObject startTask : startTasks) {
				
				TaskNode startTaskNode = createTaskNode(startTask);
				t0.addNextNode(startTaskNode);
				results.add(startTaskNode);
				nextTaskNode(startTaskNode, results);
		}
		
		results.add(createTaskNode("结束"));

		startTasks = null;
		childTasks = null;
		
		flowTaskNodeCache.put(amount, results);
		
		return results;
		
	}
	
	@Deprecated
	public static ModelObject[] getFlowTasks(ModelObject amount) throws Exception{
		
		ModelObject flow = MyProperty.getModelObjectProperty(amount, "root_task");
		
		if(flowChildsCache.containsKey(flow)){
			taskRelatedArray = taskRelatedArrayCache.get(flow);
			return flowChildsCache.get(flow);
		}
		
		List<ModelObject> taskModelArray = new ArrayList<>();
		if(taskRelatedArray == null){
			taskRelatedArray = new ArrayList<>();
		}else{
			taskRelatedArray.clear();
		}
		
		ModelObject flowTemp = MyProperty.getModelObjectProperty(flow, "task_template");
		
		ModelObject[] startTasks = MyProperty.getModelObjectArrayProperty(flowTemp, "start_successors");
		ModelObject[] childTasks = MyProperty.getModelObjectArrayProperty(flow, "child_tasks");
		
		for (ModelObject startTask : startTasks) {
			
				taskModelArray.add(startTask);
				nextTask(startTask, taskModelArray, taskRelatedArray);
				
		}
		
		ModelObject[] results = new ModelObject[taskModelArray.size()];
				
		//将流程模板转换成实例的流程对象
		for(int i = 0; i < taskModelArray.size(); i++){
			
			for(int j = 0; j < childTasks.length; j++){
				
				if(MyProperty.getStringProperty(childTasks[j], "object_name")
						.equals(MyProperty.getStringProperty(taskModelArray.get(i), "object_name"))){
					results[i] = childTasks[j];
					
//					if(MyProperty.getStringProperty(results[i], "object_name")
//							.equals(MyProperty.getStringProperty(endTasks[0], "object_name"))){
//						
//						endTaskPosition = i;
//					}	
					
					break;
				}
			}
		}
		
		flowChildsCache.put(flow, results);
		taskRelatedArrayCache.put(flow, taskRelatedArray);
		
//		if(endTaskPosition != results.length - 1){
//			
//			ModelObject temp = results[endTaskPosition];
//			results[endTaskPosition] = results[results.length - 1];
//			results[results.length - 1] = temp;
//		}
		
		startTasks = null;
		childTasks = null;
		taskModelArray = null;
		
		return results;
	}
	
	@Deprecated
	public static void nextTask(ModelObject currentTask, List<ModelObject> taskModelArray, List<Integer[]> taskRelatedArray) throws Exception{
		
		ModelObject[] successors = MyProperty.getModelObjectArrayProperty(currentTask, "successors");
		
		int thisIndex = taskModelArray.size() - 1;
		
		for (ModelObject successor : successors) {
			
			if(taskModelArray.contains(successor)){
				
				for(int i = 0; i < taskModelArray.size(); i++){
					
					if(successor.equals(taskModelArray.get(i))){
						taskRelatedArray.add(new Integer[]{thisIndex, i});
						break;
					}
					
				}
				
				continue;
			}
			
			taskModelArray.add(successor);
			
			taskRelatedArray.add(new Integer[]{thisIndex, taskModelArray.size() - 1});
			
			nextTask(successor, taskModelArray, taskRelatedArray);
		}
		
		successors = null;
	}
	
	public static void nextTaskNode(TaskNode currentTaskNode, List<TaskNode> results) throws Exception{
		
		ModelObject[] successors = MyProperty.getModelObjectArrayProperty(
									currentTaskNode.getTask().getTaskObject(),
									"successors");
		
//		if(successors == null || successors.length ==  0){
//			currentTaskNode.addNextNode(createTaskNode("结束"));
//		}
		
		if(currentTaskNode.getTask().getName().equals(MyProperty.getStringProperty(completeTask[0], "object_name"))){
			currentTaskNode.addNextNode(createTaskNode("结束"));
		}
		
		TaskNode temp = null;
		for (ModelObject successor : successors) {
			
			temp = createTaskNode(successor);
			
			currentTaskNode.addNextNode(temp);
			
			if(results.contains(temp)){
				
				continue;
			}
			
			results.add(temp);
			nextTaskNode(temp, results);
		}
		
		successors = null;
	}
	
	public static TaskNode createTaskNode(ModelObject template) throws Exception{
		
		//根据任务模板的名称获取到实际任务
		template = childTaskMap.get(MyProperty.getStringProperty(template, "object_name"));
		
		if(template == null)return null;
		
		TaskModel t = new TaskModel();
		String name	= MyProperty.getStringProperty(template, "object_name");
		String taskType	= MyProperty.getStringProperty(template, "task_type");
		t = new TaskModel(template);
		t.setName(name);
		t.setType(taskType);
		
		return new TaskNode(t);
	}
	
	public static TaskNode createTaskNode(String name){
		TaskModel t = null;
		t = new TaskModel();
		t.setName(name);
		t.setType(TaskTypeModel.TASK);
		t.setUid("Custom_Node_Flag_"+name);
		
		return new TaskNode(t);
	}
	
}
