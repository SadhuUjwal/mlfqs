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
import java.util.LinkedList;

public class MultiLevelFeedbackQueueScheduler {

	static final int NUM_PROCESSES = 40;

	LinkedList<ProcessData> proc;
	LinkedList<ProcessData> l1Q;
	LinkedList<ProcessData> l2Q;
	LinkedList<ProcessData> l3Q;

	LinkedList<Integer> pCrtnTimes;
	LinkedList<Integer> pStrtTimes;
	LinkedList<Integer> execuTimes;
	LinkedList<Integer> pTrmntnTimes;

	MultiLevelFeedbackQueueScheduler() {
		proc = new LinkedList<ProcessData>();
		l1Q = new LinkedList<ProcessData>();
		l2Q = new LinkedList<ProcessData>();
		l3Q = new LinkedList<ProcessData>();
		pCrtnTimes = new LinkedList<Integer>();
		pStrtTimes = new LinkedList<Integer>();
		execuTimes = new LinkedList<Integer>();
		pTrmntnTimes = new LinkedList<Integer>();
	}

	public static void main(String args[]) {
		MultiLevelFeedbackQueueScheduler mlfq = new MultiLevelFeedbackQueueScheduler();
		mlfq.initialize();
		int currentTime = 0;
		int linePrint = 0;
		int totalWaitingTime = 0, totalTurnAroundTime = 0, totalResponseTime = 0;
		System.out.println("Time\t\tDescription");
		System.out.println("------------------------------------------------");
		while (true) {
			if (mlfq.proc.size() == 0 && mlfq.l1Q.size() == 0
					&& mlfq.l2Q.size() == 0
					&& mlfq.l3Q.size() == 0) {
				break;
			}
			LinkedList<ProcessData> newlyCreatedProcesses = new LinkedList<ProcessData>();
			for (int i = 0; i < mlfq.proc.size(); i++) {
				if (mlfq.proc.get(i).getArrTime() == currentTime) {
					ProcessData process = mlfq.proc.get(i);
					mlfq.l1Q.add(process);
					newlyCreatedProcesses.add(process);
					if (linePrint == 0) {
						System.out.print(currentTime + " ms\t\t");
						linePrint = 1;
					}
					System.out.print("P" + process.getPId()
							+ " created. P" + process.getPId()
							+ " enters Q0.");
				}
			}
			// Remove newly created processes that are added to Q1 from
			// allProcesses
			for (int i = 0; i < newlyCreatedProcesses.size(); i++)
				mlfq.proc.remove(newlyCreatedProcesses.get(i));
			// Check level1Queue
			ProcessData currentProcess;
			if (mlfq.l1Q.size() != 0) {
				currentProcess = mlfq.l1Q.get(0);
				if (linePrint == 0) {
					System.out.print(currentTime + " ms\t\t");
					linePrint = 1;
				}
				if (currentProcess.getTimeInQueue() == 0)
					mlfq.pStrtTimes.set(
							currentProcess.getPId() - 1, currentTime);
				if (currentProcess.getTimeInQueue() < 8) {
					if (currentProcess.getExecuTime() <= 1) {
						mlfq.l1Q.remove();
						mlfq.pTrmntnTimes.set(
								currentProcess.getPId() - 1, currentTime);
						System.out.print("P" + currentProcess.getPId()
								+ " completed execution and terminated.");
					} else {
						currentProcess.setExecuTime(currentProcess
								.getExecuTime() - 1);
						currentProcess
								.setTimeInQueue(currentProcess
										.getTimeInQueue() + 1);
						System.out.print("P" + currentProcess.getPId()
								+ " is processing in Q1.");
						if (currentProcess.getTimeInQueue() == 8) {
							currentProcess.setTimeInQueue(0);
							mlfq.l2Q.add(currentProcess);
							mlfq.l1Q.remove(currentProcess);
							System.out.print("P"
									+ currentProcess.getPId()
									+ " aged and moved from Q1 to Q2.");
						}
					}
				}
			}
			// Check level2Queue
			else if (mlfq.l2Q.size() != 0) {
				currentProcess = mlfq.l2Q.get(0);
				if (linePrint == 0) {
					System.out.print(currentTime + " ms\t\t");
					linePrint = 1;
				}
				if (currentProcess.getTimeInQueue() < 16) {
					if (currentProcess.getExecuTime() <= 1) {
						mlfq.l2Q.remove();
						mlfq.pTrmntnTimes.set(
								currentProcess.getPId() - 1, currentTime);
						System.out.print("P" + currentProcess.getPId()
								+ " completed execution and terminated.");
					} else {
						currentProcess.setExecuTime(currentProcess
								.getExecuTime() - 1);
						currentProcess
								.setTimeInQueue(currentProcess
										.getTimeInQueue() + 1);
						System.out.print("P" + currentProcess.getPId()
								+ " is processing in Q2.");
						if (currentProcess.getTimeInQueue() == 16) {
							currentProcess.setTimeInQueue(0);
							mlfq.l3Q.add(currentProcess);
							mlfq.l2Q.remove(currentProcess);
							System.out.print("P"
									+ currentProcess.getPId()
									+ " aged and moved from Q2 to Q3.");
						}
					}
				}
			}
			// check level3Queue
			else if (mlfq.l3Q.size() != 0) {
				currentProcess = mlfq.l3Q.get(0);
				if (linePrint == 0) {
					System.out.print(currentTime + " ms\t\t");
					linePrint = 1;
				}
				System.out.print("P" + currentProcess.getPId()
						+ " is processing in Q3.");
				if (currentProcess.getExecuTime() <= 1) {
					mlfq.l3Q.remove();
					mlfq.pTrmntnTimes.set(
							currentProcess.getPId() - 1, currentTime);
					System.out.print("P" + currentProcess.getPId()
							+ " completed execution and terminated.");
				} else
					currentProcess.setExecuTime(currentProcess
							.getExecuTime() - 1);
			}
			currentTime++;
			if (linePrint == 1)
				System.out.println();
			linePrint = 0;
		}

		for (int i = 0; i < mlfq.execuTimes.size(); i++) {
			totalWaitingTime += mlfq.pTrmntnTimes.get(i)
					- (mlfq.pCrtnTimes.get(i) + mlfq.execuTimes
							.get(i));
			totalTurnAroundTime += mlfq.pTrmntnTimes.get(i)
					- mlfq.pCrtnTimes.get(i);
			totalResponseTime += mlfq.pStrtTimes.get(i)
					- mlfq.pCrtnTimes.get(i);
		}

		System.out.println("\nAverage waiting time = "
				+ (totalWaitingTime / mlfq.pCrtnTimes.size()));
		System.out.println("Average turnaround time = "
				+ (totalTurnAroundTime / mlfq.pCrtnTimes.size()));
		System.out.println("Average response time = "
				+ (totalResponseTime / mlfq.pCrtnTimes.size()));
	}

	public void initialize() {
		System.out.println("ProcessId\tArrivalTime\tExecutionTime");
		for (int i = 0; i < NUM_PROCESSES; i++) {
			int arrivalTime = (int) (Math.random() * 30);
			int actualExecutionTime = (int) (Math.random() * 50) + 1;
			ProcessData newProcess = new ProcessData(
					proc.size() + 1, arrivalTime, actualExecutionTime,
					0);
			proc.add(newProcess);
			System.out.println(newProcess.getPId() + "\t\t"
					+ newProcess.getArrTime() + "\t\t"
					+ newProcess.getExecuTime());
			pCrtnTimes.add(arrivalTime);
			execuTimes.add(actualExecutionTime);
			pStrtTimes.add(-1);
			pTrmntnTimes.add(-1);
		}
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

