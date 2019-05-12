package com.example.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoTinhThanh;
    ArrayAdapter<String> adapterTinhThanh;
    String TAG="FIREBASE";

    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTimKiem();
            }
        });
    }

    private void xuLyTimKiem() {
        String s = autoTinhThanh.getText().toString();


        Intent intent = new Intent(MainActivity.this, ChonNgayActivity.class);

        intent.putExtra("tinhThanh",s);
        startActivity(intent);


    }

    private void addControls() {
        btnSearch = findViewById(R.id.btnSearch);
        autoTinhThanh = findViewById(R.id.autotxtTinhThanh);


//        arrTinhThanh = getResources().getStringArray(R.array.arrTinhThanh);
        adapterTinhThanh = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_expandable_list_item_1
//                ,T
//                arrTinhThanh
                );

        autoTinhThanh.setAdapter(adapterTinhThanh);

        //lấy đối tượng FirebaseDatabase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là Place (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("Place");
        //truy suất và lắng nghe sự thay đổi dữ liệu
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapterTinhThanh.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    //lấy key của dữ liệu
                    String key=data.getKey();
                    //lấy giá trị của key (nội dung)
//                    String value=data.getValue().toString();
                    adapterTinhThanh.add(key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
