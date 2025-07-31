package lb.edu.ul.contactmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import lb.edu.ul.contactmanagement.databinding.ActivityMainBinding;
import lb.edu.ul.contactmanagement.repository.AppDatabase;
import lb.edu.ul.contactmanagement.repository.Contact;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public static AppDatabase db;
    public String currentPhone = "";
    public ActivityResultLauncher launcher;

    public String searchQuery = "%%";

    RecyclerView contactsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "contact_db").build();

        contactsRv = findViewById(R.id.contactsRv);

        setSupportActionBar(binding.toolbar);

        launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean isGranted) {
                if (isGranted) {
                    doTheCall();
                }
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddEditContactActivity.class));
            }
        });

        applySearchQuery();
    }

    public void applySearchQuery(){
        db.contactDao().getAllContacts(searchQuery).removeObservers(this);
        db.contactDao().getAllContacts(searchQuery).observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                CustomAdapter adapter = new CustomAdapter(MainActivity.this, contacts);
                contactsRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                contactsRv.setAdapter(adapter);
            }
        });
    }

    public void doTheCall() {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentPhone));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = "%" + query + "%";
                applySearchQuery();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = "%" + newText + "%";
                applySearchQuery();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}