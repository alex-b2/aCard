package com.alex.bilka.acard;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    private RadioButton genderFemaleButton;
    private RadioButton genderMaleButton;
    private EditText ageEditDate;
    private EditText EditName;
    private RadioGroup genderRadioGroup;

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // To track statistics around application
        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        // inform the Parse Cloud that it is ready for notifications
       // PushService.setDefaultPushCallback(this, MainActivity.class);
        // Set up our UI member properties.
        genderFemaleButton = (RadioButton) findViewById(R.id.radioButtonFemale);
        genderMaleButton = (RadioButton) findViewById(R.id.radioButtonMale);
        ageEditDate = (EditText) findViewById(R.id.editTextDate);
        EditName = (EditText) findViewById(R.id.editTextName);
        genderRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);

    //    ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Display the current values for this user, such as their age and gender.
        displayUserProfile();
        refreshUserProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Save the user's profile to their installation.
    public void saveUserProfile(View view) {
        String ageTextString = ageEditDate.getText().toString();
        String NameTextString = EditName.getText().toString();
        Date d;
        if (ageTextString.length() > 0) {
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                // here set the pattern as you date in string was containing like date/month/year
                 d = sdf.parse(ageTextString);
            }catch(ParseException ex){
                //TO DO Find error
                // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
                Log.i("Main My", ex.getMessage());
                d = new Date();
            }
            ParseInstallation.getCurrentInstallation().put("birthday", d);
        }

        if (NameTextString.length() > 0) {
            ParseInstallation.getCurrentInstallation().put("name", NameTextString);
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == genderFemaleButton.getId()) {
            ParseInstallation.getCurrentInstallation().put("gender", GENDER_FEMALE);
        } else if (genderRadioGroup.getCheckedRadioButtonId() == genderMaleButton.getId()) {
            ParseInstallation.getCurrentInstallation().put("gender", GENDER_MALE);
        } else {
            ParseInstallation.getCurrentInstallation().remove("gender");
        }

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(ageEditDate.getWindowToken(), 0);

        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_succes, Toast.LENGTH_SHORT);

                    toast.show();
                } else {
                    e.printStackTrace();

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    // Refresh the UI with the data obtained from the current ParseInstallation object.
    private void displayUserProfile() {
        String gender = ParseInstallation.getCurrentInstallation().getString("gender");
        Date age = ParseInstallation.getCurrentInstallation().getDate("age");

        if (gender != null) {
            genderMaleButton.setChecked(gender.equalsIgnoreCase(GENDER_MALE));
            genderFemaleButton.setChecked(gender.equalsIgnoreCase(GENDER_FEMALE));
        } else {
            genderMaleButton.setChecked(false);
            genderFemaleButton.setChecked(false);
        }

        if (age != null) {
            ageEditDate.setText(age.toString());
        }

    }

    // Get the latest values from the ParseInstallation object.
    private void refreshUserProfile() {
        ParseInstallation.getCurrentInstallation().fetchInBackground(new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    displayUserProfile();
                }
            }
        });
    }
}
