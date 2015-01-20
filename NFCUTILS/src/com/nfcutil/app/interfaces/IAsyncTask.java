package com.nfcutil.app.interfaces;

public interface IAsyncTask {
	public void showProgressbar();
	public void hideProgressbar();
	public Object doInBackground();
	public void precessDataAfterDownlaod(Object data);
}
