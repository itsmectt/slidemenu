package com.ctt.slidemenudemo;




import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class sportstrack extends Activity {
	// int count=-0;
	MapView mMapView = null;
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	// double distance=0;

	BaiduMap mBaiduMap;
	private Timer timer = new Timer();
	private TimerTask task;
	// List<LatLng> points = new ArrayList<LatLng>();
	List<LatLng> pointstwo = new ArrayList<LatLng>();
	LatLng p1;
	LatLng p2;
	// UI���
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	//��ʼ�˶�button

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			// ��������ص��ӿ�����MainActivity�Ѿ�ʵ���˻ص��ӿڣ�����MainActivity.this����

			if (msg.what == 1) {
				// if(points.size()>3){
				// pointstwo.add((LatLng)points.get(points.size()-3));
				// pointstwo.add((LatLng)points.get(points.size()-2));
				// pointstwo.add((LatLng)points.get(points.size()-1));
				// }else{
				// pointstwo.add((LatLng)points.get(count));
				// }
				// Log.d("points",""+points.get(points.size()-3).latitude);
				// Log.d("points",""+points.get(points.size()-2).latitude);
				// Log.d("points",""+points.get(points.size()-1).latitude);
				pointstwo.add(p1);
				pointstwo.add(p2);
				// distance+=DistanceUtil. getDistance(p1, p2);
				// LatLng llDot = new LatLng(p2.latitude,p2.longitude);
			//	try{
				OverlayOptions ooDot = new DotOptions().center(p2).radius(6)
						.color(0xAAFF0000);
				mBaiduMap.addOverlay(ooDot);

				OverlayOptions ooPolyline = new PolylineOptions().width(4)
						.color(0xAAFF0000).points(pointstwo);
				mBaiduMap.addOverlay(ooPolyline);
				p1 = p2;
				mLocClient.requestLocation();
			//	}catch(Exception e){
				//	e.printStackTrace();
			//	}

			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		/*
		 * forbid lock screen
		 */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.sportstrack);

		//button
		final Button button= (Button) findViewById(R.id.track);
		final boolean[] btnState = {true};
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnState[0] ==true) {
					button.setText("�켣��¼��");
					btnState[0] =false;
				}else {
					button.setText("��ʼ�˶�");
					btnState[0] =true;
				}
			}
		});
		
		// ��ȡ��ͼ�ؼ�����
		Log.d("points", "wwww");

				

		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);

		// ������ͨͼ
		mBaiduMap.setTrafficEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(17).build()));// �������ż���
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setIsNeedAddress(true);
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		

	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				// p1=new LatLng(39.93923, 116.357428);
				p1 = p2 = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p2);
				// count++;
				// points.add(p2);
				// pointstwo.add(ll);
				mBaiduMap.animateMapStatus(u);
			} else {
				// count++;
				p2 = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p2);
				// points.add(p2);
				Log.d("points", "wwww");
				OverlayOptions ooDot = new DotOptions().center(p2).radius(6)
						.color(0xAAFF0000);
				mBaiduMap.addOverlay(ooDot);

				mBaiduMap.animateMapStatus(u);

			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		startTimer();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		pauseTimer();
		super.onDestroy();
	}

	public void startTimer() {
		if (timer == null) {
			timer = new Timer();
		}
		if (task == null) {
			task = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);

				}
			};
		}
		if (timer != null && task != null) {
			timer.schedule(task, 3000, 1000);
		}
	}

	public void pauseTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}
}
