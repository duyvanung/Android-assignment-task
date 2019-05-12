package com.example.search;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChonNgayActivity extends AppCompatActivity {

    //lấy tên biến y xì bên kia gửi qua
    TextView txtKetQua;

    ListView lvLichTrinh;
    ArrayAdapter<String> adapterLichTrinh;
    String TAG="FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ngay);
        addControl();
        addEvent();
    }

    private void addEvent() {
        lvLichTrinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        ChonNgayActivity.this,
                        "Bạn chọn \t" + adapterLichTrinh.getItem(position),
                        Toast.LENGTH_LONG
                        ).show();

            }
        });
    }

    private void addControl() {
        txtKetQua = findViewById(R.id.txtKetQua);
        Intent intent = getIntent(); //lấy intent mở màn hình này
        String tinhThanh = intent.getStringExtra("tinhThanh"); // rỗng khi không tìm thấy

        txtKetQua.setText("Vui lòng chọn lịch trình của "+ tinhThanh);

        lvLichTrinh = findViewById(R.id.lvLichTrinh);
        adapterLichTrinh = new ArrayAdapter<String>(
                ChonNgayActivity.this,
                android.R.layout.simple_expandable_list_item_1
        );
        lvLichTrinh.setAdapter(adapterLichTrinh);

        //lấy đối tượng FirebaseDatabase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là "Place/"+tinhThanh (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("Place/"+tinhThanh);
        //truy suất và lắng nghe sự thay đổi dữ liệu
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapterLichTrinh.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    //lấy key của dữ liệu
                    String key=data.getKey();
                    //lấy giá trị của key (nội dung)
//                    String value=data.getValue().toString();
                    adapterLichTrinh.add(key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }



}


