package com.openshift.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

@Deprecated
public class OpenshiftActivity extends Activity {
		
//	private BroadcastReceiver requestReceiver;

//	private OpenshiftServiceHelper mOpenshiftServiceHelper;
//	private OpenshiftWebService ws;
//	private RESTRequest<UserRestResource> request;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_openshift);
	    
//	    RESTDroid.init(getApplicationContext());
//	    	 
//	    try {
////			ws = (OpenshiftWebService) RESTDroid.getInstance().getWebService(OpenshiftWebService.class);
////			ws.registerModule(new OpenshiftModule(this));
////			request = ws.getUserInformation();
////			request.setRequestListeners(this, GetUserRequestListeners.class);
////			request.setFailBehaviorClass(NoActionFailBehavior.class);
//			
//			
//		} catch (RESTDroidNotInitializedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    Button button = (Button) findViewById(R.id.button1);
//	        
//	    
//	    
////		IntentFilter filter = new IntentFilter(OpenshiftActions.USER_DETAIL);
////		requestReceiver = new BroadcastReceiver() {
////
////			@Override
////			public void onReceive(Context context, Intent intent) {
////				int resultCode = intent.getIntExtra(OpenshiftServiceHelper.EXTRA_RESULT_CODE, 0);
////				
////				Response<UserResource> userInformation = (Response<UserResource>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
////				
////				if(resultCode==200){
////					new AlertDialog.Builder(OpenshiftActivity.this).setTitle("Message").setMessage("Login Name: "+userInformation.getResource().getLogin()).setNeutralButton("Close", null).show();	
////				}
////				else {
////					showToast("Failed to Retrieve User Information: "+userInformation.getMessage());
////				}
////				
////				
////			}
////
////		};
////		
////		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
////		this.registerReceiver(requestReceiver, filter);
//
//	    
//	    
//	    button.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ws.executeRequest(request);
//
//					
////				mOpenshiftServiceHelper.getUserInformation();
//			}
//		});
	}
	
	private void showToast(String message) {
		if(!isFinishing()){
			Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}
	
    @Override
    public void onPause() {
        super.onPause();
//        ws.onPause();
    }
    
    @Override
    public void onResume() {
        super.onResume();
//        ws.onResume();
    }	
	
//    public class GetUserRequestListeners extends RequestListeners {
//    	
//        private OnSucceededRequestListener onSucceeded = new OnSucceededRequestListener() {
//
//            public void onSucceededRequest(int resultCode) {
//                //TODO : notify your UI that the request has succeeded
//            }
//            
//        };
//        
//        private OnStartedRequestListener onStarted = new OnStartedRequestListener() {
//
//        public void onStartedRequest() {
//        Log.i("foo", "getUserRequest has started !");
//        }
//       
//        };
//       
//        private OnFailedRequestListener onFailed = new OnFailedRequestListener() {
//
//        public void onFailedRequest(int resultCode) {
//        Log.e("foo", "Unfortunately getUserRequest failed with result code " + resultCode);
//        }
//       
//        };
//       
//        private OnFinishedRequestListener onFinished = new OnFinishedRequestListener() {
//
//        public void onFinishedRequest(int resultCode) {
//        Log.i("foo", "getUserRequest has finished with result code " + resultCode);
//		if(resultCode==200){
////			new AlertDialog.Builder(OpenshiftActivity.this).setTitle("Message").setMessage("API: "+((UserRestResource)mRequest.getResource()).getApiVersion()).setNeutralButton("Close", null).show();	
//		}
//        Log.i("foo", "Request is pending " + mRequest.isPending());
//		
////        user = (User) mRequest.getResourceRepresentation();
//        }
//       
//        };
//       
//        public GetUserRequestListeners() {
//        super();
//        addOnSucceededRequestListener(onSucceeded);
//        addOnStartedRequestListener(onStarted);
//        addOnFailedRequestListener(onFailed);
//        addOnFinishedRequestListener(onFinished);
//        }
//       }

}
