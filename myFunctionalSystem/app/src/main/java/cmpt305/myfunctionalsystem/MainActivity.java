package cmpt305.myfunctionalsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;

public class MainActivity extends MyMenu {

    //public static Intent intent;
    private final String TAG = "myFunctional System";
    private String uname = "user";
    private String password = "password";
    private List<Course> courses;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private boolean validated = false;

    private void login()
    {
        if(validated) {
            Intent intent = new Intent(this, MyAgenda.class);
            startActivity(intent);
        }
    }

    Thread thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        courses = new ArrayList<>();
        Log.d(TAG, "onCreate() called");
    }

    private void makeThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject().put("password", passwordEditText.getText().toString()).put("netid", usernameEditText.getText().toString());
                    String post = json.toString();
                    Log.d(TAG, post);
                    URL myFunctionalServer = new URL("http://159.203.29.177/auth/login/");
                    HttpURLConnection connection = (HttpURLConnection) myFunctionalServer.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setFixedLengthStreamingMode(post.getBytes().length);
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    connection.connect();

                    DataOutputStream reqStream = new DataOutputStream(connection.getOutputStream());
                    reqStream.writeBytes(post);
                    reqStream.flush();

                    try {

                        if (connection.getResponseCode() == 200) {
                            validated = true;
                            uname = usernameEditText.getText().toString();
                            password = passwordEditText.getText().toString();
                        } else
                            Log.d(TAG, String.format("%d\n", connection.getResponseCode()));
                        reqStream.close();

                    }
                    catch (java.io.IOException e) {
                        return;
                    }

                    login();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void launchLogin(View view){
        usernameEditText = (EditText) findViewById(R.id.unameText);
        passwordEditText = (EditText) findViewById(R.id.passwordText);

        if(thread == null)
            makeThread();
        try {
            thread.start();
        }
        catch (IllegalThreadStateException e) {
            e.printStackTrace();
            thread.interrupt();
            makeThread();
            thread.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        thread = null;
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_my_class_search:
                launchActivity(Department.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAllCourseInformation(){

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
