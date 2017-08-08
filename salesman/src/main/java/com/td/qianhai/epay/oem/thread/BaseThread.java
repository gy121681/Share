package com.td.qianhai.epay.oem.thread;

/**
 * 线程父类
 * @author liangge
 *
 */
public class BaseThread extends Thread
{
	
	/**
	 * 表示线程运行结束
	 */
	public static int STATE_DONE = 0;
	
	/**
	 * 表示线程运行中
	 */
	public static int STATE_RUNNING = 1;
	
	/**
	 * 标识线程运行状态
	 */
	protected static int threadState = 0;
	
	/**
	 * 获取线程运行状态
	 * 
	 * @return 整型的状态值，0表示线程运行结束，1表示线程运行中
	 */
	public static int getThreadState()
	{
	
		return threadState;
	}
}
