package lb.edu.ul.contactmanagement;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lb.edu.ul.contactmanagement.databinding.ActivityAddEditContactBinding;
import lb.edu.ul.contactmanagement.repository.Contact;

public class AddEditContactActivity extends AppCompatActivity {
    Contact updatedContact;
    EditText fnameEd, lnameEd, phoneEd;
    Button addEditButton;
    boolean isEdit = false;
    private AppBarConfiguration appBarConfiguration;
    private ActivityAddEditContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddEditContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        fnameEd = findViewById(R.id.fnameEd);
        lnameEd = findViewById(R.id.lnameEd);
        phoneEd = findViewById(R.id.phoneEd);
        addEditButton = findViewById(R.id.addEditButton);

        Intent j = getIntent();
        int contactId = j.getIntExtra("contact_id", -1);
        if(contactId != -1){
            isEdit = true;
            addEditButton.setText("Edit Contact");
            MainActivity.db.contactDao().getContactById(contactId).observe(this, new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    updatedContact = contact;
                    fnameEd.setText(contact.fname);
                    lnameEd.setText(contact.lname);
                    phoneEd.setText(contact.phone);
                }
            });
        }
        else{
            addEditButton.setText("Add Contact");
            isEdit = false;
        }
    }

    public void addEditButtonHandler(View v){
        String fname = fnameEd.getText().toString();
        if (TextUtils.isEmpty(fname)) {
            fnameEd.setError("Can't be empty!");
            return;
        }
        String lname = lnameEd.getText().toString();
        if (TextUtils.isEmpty(lname)) {
            lnameEd.setError("Can't be empty!");
            return;
        }
        String phone = phoneEd.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            phoneEd.setError("Can't be empty!");
            return;
        }
        if(isEdit) {
            updatedContact.fname = fname;
            updatedContact.lname = lname;
            updatedContact.phone = phone;
            updateInBackground(updatedContact);
        }
        else{
            Contact contact = new Contact();
            contact.fname = fname;
            contact.lname = lname;
            contact.phone = phone;
            insertInBackground(contact);
        }
    }

    private void updateInBackground(Contact updatedContact) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MainActivity.db.contactDao().updateContact(updatedContact);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }

    private void insertInBackground(Contact contact) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MainActivity.db.contactDao().insertAll(contact);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }
}