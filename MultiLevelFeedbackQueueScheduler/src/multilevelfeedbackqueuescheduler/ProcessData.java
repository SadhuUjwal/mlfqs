/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multilevelfeedbackqueuescheduler;

/**
 *
 * @author ujwal
 */
public class ProcessData {
	int pId;
	int arrTime;
	int execuTime;
	int timeInQueue;

	public int getTimeInQueue() {
		return timeInQueue;
	}

	public void setTimeInQueue(int timeInQueue) {
		this.timeInQueue = timeInQueue;
	}

	ProcessData(int pId, int arrTime,
			int execuTime, int timeInQueue) {
		this.pId = pId;
		this.arrTime = arrTime;
		this.execuTime = execuTime;
		this.timeInQueue = timeInQueue;
	}

	public int getPId() {
		return pId;
	}

	public void setPId(int pId) {
		this.pId = pId;
	}

	public int getArrTime() {
		return arrTime;
	}

	public void setArrTime(int arrTime) {
		this.arrTime = arrTime;
	}

	public int getExecuTime() {
		return execuTime;
	}

	public void setExecuTime(int execuTime) {
		this.execuTime = execuTime;
	}

}
