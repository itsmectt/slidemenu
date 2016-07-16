package com.ctt.slidemenudemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



/**
 * @since 2015.09.12
 * @author apple
 * function ʵ�ֶԼ��ٶȴ������Ĳ�����ʵ�ּǲ��Ĺ���
 * @version 1.0
 *
 * TEST
 * ���Թ�����ʵ����Ԥ�ڵ�Ŀ�꣬���Ǻ�������ǰ�������ƶ��ͼ������Ƿ���Ҫ�޸Ĳ���������ʵ�ʵ�ģ��  range����֤���־���
 * �޸�range�ĳ�ʼֵ��Ӱ���ֻ��ǲ��������ȣ�����
 */
public class lishijilu extends Activity {
	private SensorManager accSensorManager;
	private int step = 0;   //����
	private double oriValue;  //ԭʼ��
	private double lstValue;  //�ϴ���
	private double curValue;  //��ǰ��

	private boolean motiveState = true;  //�˶�״̬
	private boolean processState = false;  //����״̬

	private TextView stepText = null;
	private Button ctlBtn = null;
	private Button pause = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lishijilu);

		//���ô�����
		accSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		stepText = (TextView) findViewById(R.id.step);
		ctlBtn = (Button) findViewById(R.id.controlBtn);
		pause= (Button) findViewById(R.id.pause);

		//����������������⵽���°�ťʱ����activity
		ctlBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//��ʼ״̬
				step = 0;
				stepText.setText("0");

				if (processState == true) {

					ctlBtn.setText("��ʼ");
					processState = false;
				} else {
					ctlBtn.setText("ֹͣ");
					processState = true;

				}

			}
		});
		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (motiveState==true){
					String curStep=String.valueOf(step);
					stepText.setText(curStep);
					motiveState=false;
					onPause();
					pause.setText("����");
				}else{
					pause.setText("��ͣ");
					motiveState=true;
					onResume();
				}


			}
		});
	}

	//�����������������
private SensorEventListener sel=new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				float[] values = event.values;
				//�趨һ�����ȷ�Χ,������֤�����޸ľ���ֵӰ��ģ���Ч������ǰ���ֻ��ķ��ȡ��ٶ�֮��Ļ�Ӱ��ǲ���Ч��
				double range=3;
				//���㵱ǰ����������ģ
				curValue=magnitude(values[0], values[1],values[2]);
				//���ϼ���״̬
				if (motiveState==true)
				{
					if (curValue>=lstValue)
					{
						lstValue=curValue;
					}
					else
					{
						if (Math.abs(curValue - lstValue) >range)
						{
							//��⵽һ�η�ֵ
							oriValue=curValue;
							motiveState=false;
						}
					}
				}
				/**
				 * �ǲ�ԭ����ǣ�����·����һ��һ�£����ٶ�Ҳ��һ��һ�µ�������ֵ��������ֵ��������һ����
				 * �������ж�����ٶȣ�onSensorChanged�����У������������ϵļ��ٶȿ���һ�����壬�ø���ά������ʾ
				 * ���ٶ������ı仯��������ģΪָ�꣡
				 */


				//���¼���
				if (motiveState==false)
				{
					if (curValue<=lstValue)
					{
						lstValue=curValue;
					}
					else
					{
						if (Math.abs(curValue - lstValue) > range)
						{
							//��⵽һ�η�ֵ
							oriValue=curValue;
							if (processState==true)
							{
								//������һ
								step+=1;
								String stepNum=String.valueOf(step);
								if (processState==true)
								{
									//���¶���
									stepText.setText(stepNum);
								}
							}
							motiveState=true;
						}
					}
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int i) {

		}
	};

	@Override
	protected void onResume() {

		//���ٶȴ�����ע�����
		accSensorManager.registerListener(
				 sel,accSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI
		);
		//���������ǣ�������SensorListener���󣬴��������ͣ�Ƶ��

		super.onResume();
	}

	@Override
	protected void onPause() {

		//ȡ��ע�������
		accSensorManager.unregisterListener(sel);

		super.onPause();
	}
	private double magnitude(float x, float y, float z) {
		double magnitude=0;
		magnitude=Math.sqrt(x*x+y*y+z*z);
		return magnitude;
	}

}
