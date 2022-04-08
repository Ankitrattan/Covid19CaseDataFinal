package com.app.covid19casedata.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkCheck {
     private ConnectivityManager mNetworkConnection;
     private Context mNetworkContext;
     private boolean mNetworkConnectingFlag=false;
	public boolean connectivityCheck(Context context) {
		try {
			mNetworkConnection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(mNetworkConnection != null && mNetworkConnection.getActiveNetworkInfo()!=null && mNetworkConnection.getActiveNetworkInfo().isAvailable() && mNetworkConnection.getActiveNetworkInfo().isConnectedOrConnecting()){
				if(mNetworkConnection.getActiveNetworkInfo().isConnected()){
					mNetworkConnectingFlag=true;
				}else if(mNetworkConnection.getActiveNetworkInfo().isFailover()){
					mNetworkConnectingFlag=false;
				}
			}else{
				mNetworkConnectingFlag=false;
			}
			return mNetworkConnectingFlag;
		}catch (Exception e){
			e.printStackTrace();
			return true;
		}
	}


}




