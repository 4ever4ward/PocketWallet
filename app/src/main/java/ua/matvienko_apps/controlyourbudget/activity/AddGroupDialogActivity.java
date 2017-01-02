package ua.matvienko_apps.controlyourbudget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.classes.Group;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

/**
 * Created by alex_ on 27-Sep-16.
 */

public class AddGroupDialogActivity extends AppCompatActivity {

    private EditText groupNameView;
    private EditText groupTypeView;
    private EditText groupPriorityView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addgroup_view);

        groupNameView = (EditText) findViewById(R.id.groupNameView);
        groupTypeView = (EditText) findViewById(R.id.groupTypeView);
        groupPriorityView = (EditText) findViewById(R.id.groupPriorityView);
        Button submitAddButton = (Button) findViewById(R.id.submitAddButton);
        Button cancelAddButton = (Button) findViewById(R.id.cancelAddButton);


        submitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AppDBHelper(AddGroupDialogActivity.this, AppDBContract.GroupsEntry.TABLE_NAME,
                        null, AppDBHelper.DB_VERSION)
                        .addGroup(new Group(groupNameView.getText().toString(),
                                groupTypeView.getText().toString(),
                                Integer.parseInt(groupPriorityView.getText().toString())));
                finish();
            }
        });

        cancelAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
