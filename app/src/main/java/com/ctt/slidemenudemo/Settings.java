package com.ctt.slidemenudemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by apple on 15/12/16.
 */
public class Settings extends Activity{
    //��һ������
    private String[] msg=new String[]{"���ǽ���","�Ӳ�����","���ѵ���֪ͨ"};
    private boolean[] msgstate=new boolean[]{true, false, false};
    //�ڶ�������
    private String[] spo=new String[]{"ÿ��","ÿ����","ÿ����","�Ӳ�"};
    private boolean[] spostate=new boolean[]{true, false, false, false};
    private RadioOnClick radioOnClick= new RadioOnClick(1);
    boolean msgClicked,spoClicked=false;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        TextView question= (TextView) findViewById(R.id.question);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //��������Ϣ�ı���
                Intent intent = new Intent(Settings.this, Textques.class);
                Settings.this.startActivity(intent);
            }
        });

        TextView message= (TextView) findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //�Ƿ������Ϣ����1.���ǽ���2.�Ӳ�3.���ѵ���֪ͨ
                msgClicked=true;
                spoClicked=false;
                AlertDialog ad =new AlertDialog.Builder(Settings.this).setTitle("������Ϣģʽ")
                        .setSingleChoiceItems(msg,radioOnClick.getIndex(),radioOnClick).create();
                ad.show();
            }
        });
        TextView update= (TextView) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this,"�Ѿ�Ϊ���°汾", Toast.LENGTH_LONG).show();
            }
        });
        TextView time= (TextView) findViewById(R.id.spotime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //��ѡÿ�졢���������ա�������
                spoClicked=true;
                msgClicked=false;
                AlertDialog add =new AlertDialog.Builder(Settings.this).setTitle("�˶�����")
                        .setSingleChoiceItems(spo,radioOnClick.getIndex(),radioOnClick).create();
                add.show();
            }
        });
        TextView hw= (TextView) findViewById(R.id.height_weight);
        hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, height_weight.class);
                Settings.this.startActivity(intent);
            }
        });
    }
    class RadioOnClick implements DialogInterface.OnClickListener{
        private int index;

        public RadioOnClick(int index){
            this.index = index;
        }
        public void setIndex(int index){
            this.index=index;
        }
        public int getIndex(){
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton){
            setIndex(whichButton);
            if (msgClicked==true) {
                    Toast.makeText(Settings.this, "��ѡ��:" + msg[index], Toast.LENGTH_LONG).show();
                }
            if (spoClicked==true) {
                Toast.makeText(Settings.this, "����" + spo[index] + "������������", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
