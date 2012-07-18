package br.com.caelum.ondeestaobusao.util;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

public class CancelableAssynTask {

	public static void cancel(AsyncTask<?,?,?> task) {
		if (task != null && Status.RUNNING.equals(task.getStatus())) {
			task.cancel(true);
		}
	}
}
