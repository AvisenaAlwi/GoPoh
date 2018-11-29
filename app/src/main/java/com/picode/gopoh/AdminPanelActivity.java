package com.picode.gopoh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.picode.gopoh.Control.ControlAdminPanel;
import com.picode.gopoh.Interface.OnGetRoomChatsSuccess;
import com.picode.gopoh.Model.ModelAdminPanel;
import com.picode.gopoh.Model.RoomChat;
import com.stfalcon.chatkit.dialogs.DialogsList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminPanelActivity extends AppCompatActivity implements OnGetRoomChatsSuccess {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dialoglist)
    DialogsList dialogsList;
    @BindView(R.id.rvShimmer)
    ShimmerRecyclerView shimmerRecyclerView;

    ControlAdminPanel controlAdminPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");

        shimmerRecyclerView.showShimmerAdapter();

        controlAdminPanel = new ControlAdminPanel(this, dialogsList, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_panel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_announcement:
                controlAdminPanel.showAddannouncement();
                break;
            case R.id.menu_logout:
                controlAdminPanel.logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetRoomChatsChanged(List<RoomChat> roomChats) {
        controlAdminPanel.setupDialogList(roomChats);
        shimmerRecyclerView.hideShimmerAdapter();
        shimmerRecyclerView.setVisibility(View.GONE);
    }
}
