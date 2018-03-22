package com.wuhenzhizao.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wuhenzhizao.R;
import com.wuhenzhizao.databinding.ListitemBranchBinding;
import com.wuhenzhizao.network.ApiManager;
import com.wuhenzhizao.service.BuildResponse;
import com.wuhenzhizao.service.BuildService;
import com.wuhenzhizao.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wuhenzhizao on 2017/8/23.
 */

public class MainActivity extends AppCompatActivity {
    private BuildService service;

    private void sendBuildRequest(String branch, int position) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .progress(true, 100)
                .content("正在发送构建请求...")
                .build();
        dialog.show();
        Call<BuildResponse> call;
        if (position == 1) {
            call = service.postBuildRequest("20d3d58e16eac6ae3158e8cc979a4a", branch, "true", "");
        } else {
            call = service.postBuildRequest("20d3d58e16eac6ae3158e8cc979a4a", branch, "", "true");
        }
        call.enqueue(new Callback<BuildResponse>() {
            public void onFailure(Call<BuildResponse> call, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "构建启动失败，请检查网络设置", Toast.LENGTH_SHORT).show();
            }

            public void onResponse(Call<BuildResponse> call, Response<BuildResponse> response) {
                dialog.dismiss();
                if ((response.isSuccessful()) && (response.body() != null)) {
                    Toast.makeText(MainActivity.this, "构建启动成功，构建成功后将会邮件通知", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showOptionMenuDialog(final String branch) {
        new MaterialDialog.Builder(this)
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .items(R.array.menu)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        sendBuildRequest(branch, position);
                    }
                }).build().show();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        AppUtils.transparencyBar(getWindow());
        AppUtils.StatusBarLightMode(getWindow());
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://fir.im/pt32"));
                startActivity(intent);
            }
        });
        BranchRecyclerViewAdapter adapter = new BranchRecyclerViewAdapter(this, Arrays.asList(getResources().getStringArray(R.array.branches)));
        adapter.setListener(new BranchRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, String environment) {
                showOptionMenuDialog(environment);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        recyclerView.setAdapter(adapter);

        service = ApiManager.instance().getService(BuildService.class);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, AboutMxActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private static class BranchRecyclerViewAdapter extends RecyclerView.Adapter<BranchRecyclerViewAdapter.BranchRecylcerViewHolder> {
        private List<String> branchList = new ArrayList();
        private LayoutInflater inflater;
        private OnItemClickListener listener;

        public BranchRecyclerViewAdapter(Context context, List<String> branchList) {
            this.branchList.addAll(branchList);
            this.inflater = LayoutInflater.from(context);
        }

        public int getItemCount() {
            return branchList.size();
        }

        public void onBindViewHolder(final BranchRecylcerViewHolder holder, int position) {
            final String branchName = branchList.get(position);
            holder.binding.tvBranchName.setText(branchName);
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    listener.onItemClicked(view, branchName);
                }
            });
        }

        public BranchRecylcerViewHolder onCreateViewHolder(ViewGroup parent, int positon) {
            return new BranchRecylcerViewHolder((ListitemBranchBinding) DataBindingUtil.inflate(this.inflater, R.layout.listitem_branch, parent, false));
        }

        public void setListener(OnItemClickListener paramOnItemClickListener) {
            this.listener = paramOnItemClickListener;
        }

        public interface OnItemClickListener {
            void onItemClicked(View view, String environment);
        }

        class BranchRecylcerViewHolder extends RecyclerView.ViewHolder {
            public ListitemBranchBinding binding;

            public BranchRecylcerViewHolder(ListitemBranchBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
