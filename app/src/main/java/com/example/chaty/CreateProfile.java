package com.example.chaty;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {
    EditText edtName;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    TextView editDOB;
    Boolean sex;
    private int mYear, mMonth, mDay;
    Button btnXacNhan;
    String phone,email;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        // nhận các thông tin từ acti khác gửi
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        editDOB = findViewById(R.id.edtDateOfBirth);
        // chỗ này là lấy ngày tháng
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateProfile.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                editDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        edtName = findViewById(R.id.edtName);
        // kiểm tra dữ liệu
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edtName, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.edtDateOfBirth, RegexTemplate.NOT_EMPTY, R.string.invalid_dob);
        btnXacNhan = findViewById(R.id.btnLuuHoSo);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData() && awesomeValidation.validate() ){
                try {
                    // nhận  các thông tin từ acti khác gửi
                    JSONObject respObj2 = new JSONObject(getIntent().getStringExtra("respObj2"));
                    String token = respObj2.get("token").toString();
                    radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup1);

                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton = (RadioButton) findViewById(selectedId);
                    if(radioSexButton.getText().toString().equals("Nam")) {
                        sex = true;
                    }
                    else{
                        sex = false;
                    }
                    // call api
                    postProfile(respObj2.get("accountId"),edtName.getText().toString(),sex, editDOB.getText().toString(),respObj2.get("token").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}
        });


    }
    private void postProfile(Object _id, String name, boolean sex, String dob , String token ) {
        String url =BuildConfig.API+"profile";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("_id", _id);
            object.put("name",name);
            object.put("sex",sex);
            object.put("dob",dob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CreateProfile.this,"Tạo thông tin thành công",Toast.LENGTH_LONG).show();

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            JSONObject respObj2 = new JSONObject(respObj.getString("data"));
                            Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                            intent.putExtra("respObj2",respObj2.toString());
                            intent.putExtra("token",token);
                            intent.putExtra("profileId",respObj2.get("_id").toString());
                            intent.putExtra("email",email);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CreateProfile.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };


        requestQueue.add(jsonObjectRequest);

    }
    public boolean validData()
    {
        if(editDOB.length() == 0)
        {
            editDOB.setError("Ngày sinh không được để trống");
            return false;
        }
        editDOB.setError(null);
        return true;
    }

}