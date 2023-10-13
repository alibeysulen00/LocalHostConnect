package com.example.localhostconnect.view;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.localhostconnect.R;
import com.example.localhostconnect.api.OnDataReceivedListener;
import com.example.localhostconnect.controller.UserController;
import com.example.localhostconnect.model.UserData;
import com.example.localhostconnect.model.db.AppDatabase;
import com.example.localhostconnect.model.db.User;
import com.example.localhostconnect.model.db.UserDao;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataReceivedListener {

    private List<UserData> userDataList = new ArrayList<>();
    private UserAdapter userAdapter;
    private AppDatabase db;
    private UserController userController;

    private EditText nameEditText;
    private Button searchButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "user")
                .fallbackToDestructiveMigration()
                .build();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userDataList);
        recyclerView.setAdapter(userAdapter);

        nameEditText = findViewById(R.id.nameEditText);
        searchButton = findViewById(R.id.searchButton);

        userController = new UserController(this, userDataList, userAdapter, db);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                if (!name.isEmpty()) {
                    userController.getUserDataByName(MainActivity.this, name);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onDataReceived(UserData userData) {
        User user = new User();
        user.id = userData.getId();
        user.name = userData.getName();
        user.lastname = userData.getLastname();
        user.birth_date = userData.getBirth_date();
        user.age = userData.getAge();

        new InsertUserAsyncTask(db.userDao()).execute(user);
        // Yeni veri eklendiği zaman, sadece bu veriyi RecyclerView'da göster.
        UserData userData1 = new UserData();
        userData1.setId(user.id);
        userData1.setName(user.name);
        userData1.setLastname(user.lastname);
        userData1.setBirth_date(user.birth_date);
        userData1.setAge(user.age);
        userDataList.add(userData1);
        userAdapter.notifyItemInserted(userDataList.size() - 1);
    }

    @Override
    public void onDataError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            try {

                userDao.insert(users[0]);
                Log.d("InsertUserAsyncTask", "User inserted successfully");
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Log.e("InsertUserAsyncTask", "Error inserting user");
            }
            return null;
        }
    }

    private class GetUsersAsyncTask extends AsyncTask<Void, Void, List<User>> {
        private UserDao userDao;

        private GetUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> userList) {
            super.onPostExecute(userList);
            userDataList.clear();

            for (User u : userList) {
                UserData userData1 = new UserData();
                userData1.setId(u.id);
                userData1.setName(u.name);
                userData1.setLastname(u.lastname);
                userData1.setBirth_date(u.birth_date);
                userData1.setAge(u.age);
                userDataList.add(userData1);
            }
            userAdapter.notifyDataSetChanged();
        }
    }
}
