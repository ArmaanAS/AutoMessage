package com.dash.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.dash.core.StateHandler;

public class DelayedSend implements Runnable {
	private String message;
	private Chat output;
	private static ScheduledThreadPoolExecutor stpe = 
		new ScheduledThreadPoolExecutor(3);
	private static boolean cancelled = false;
	
	public DelayedSend(String msg, Chat out) {
		message = msg;
		output = out;
	}
	
	@Override
	public void run() {
		if (!cancelled) {
			StateHandler.sendMessage(message, output, false);
		}
	}
	
	public static void SendMessage(String msg, Chat out) {
		cancelled = false;
		stpe.schedule(
			new DelayedSend(msg, out), 300, TimeUnit.MILLISECONDS);
	}
	
	public static void SendMessage(String msg, Chat out, int d) {
		cancelled = false;
		stpe.schedule(
			new DelayedSend(msg, out), d, TimeUnit.MILLISECONDS);
	}
	
	public static void cancelAll() {
		cancelled = true;
	}
}
