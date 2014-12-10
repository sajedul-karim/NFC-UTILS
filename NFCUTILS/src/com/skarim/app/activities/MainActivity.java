package com.skarim.app.activities;

import com.example.nfcutils.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	Button btnP2P,btnReadCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}
	
	private void initViews() {
		btnP2P=(Button) findViewById(R.id.btnP2P);
		btnReadCard=(Button) findViewById(R.id.btnReadCard);
		btnP2P.setOnClickListener(this);
		btnReadCard.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View view) {

		if(view.getId()==R.id.btnP2P){
			Intent intent=new Intent(this, P2PSendActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}else if(view.getId()==R.id.btnReadCard){
			Intent intent=new Intent(this, ReadCard.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
