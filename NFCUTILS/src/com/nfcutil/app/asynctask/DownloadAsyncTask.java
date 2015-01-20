package com.nfcutil.app.asynctask;

import com.nfcutil.app.interfaces.IAsyncTask;

import android.os.AsyncTask;

public class DownloadAsyncTask extends AsyncTask<Void, Void, Object>{
	IAsyncTask asyncTask;
	
	public DownloadAsyncTask(IAsyncTask _asyncTask) {
		this.asyncTask = _asyncTask;
	}
	
	@Override
	protected void onPreExecute() {
		if(this.asyncTask != null)
			asyncTask.showProgressbar();
	}

	@Override
	protected Object doInBackground(Void... params) {
		try{
			if(this.asyncTask != null)
				return this.asyncTask.doInBackground();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object data) {
		if(this.asyncTask != null){
			this.asyncTask.hideProgressbar();
			this.asyncTask.precessDataAfterDownlaod(data);
		}
	}

}
