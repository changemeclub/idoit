package com.changeme.todolist;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.changeme.todolist.adapter.TaskListAdapter;
import com.changeme.todolist.model.ToDoTask;
import com.changeme.todolist.sql.ToDoContentProvider;
import com.changeme.todolist.view.PullableLayout;
import com.changeme.todolist.view.PullableLayout.OnRefreshListener;
import com.changeme.todolist.view.SlideListView;

/**
 * 两个页面，待办工作及干的漂亮
 * 干的漂亮以时光轴的形式展示。
 * O 2016-03-04
 * |读书
 * |写代码
 * O 2016-03-05
 * |锻炼身体
 * |连身
 * |跑步
 * 
 * @author ldc
 *
 */
public class MainActivity extends Activity implements NewItemFragment.OnAddNewItemListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private TaskListAdapter listAdapter;
    private ArrayList<ToDoTask> taskList;
    private SlideListView taskListView;
    private ContentResolver contentResolver;
    private PullableLayout pullableLayout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();      
    }
    
    //初始化事件
    private void initEvent(){
        listAdapter.setTaskCompleteListener(new TaskCompleteListener(){
   			@Override
   			public void onTaskStatusChange(ToDoTask item) {
   				 ContentValues contentValues=new ContentValues();
   			     contentValues.put(ToDoContentProvider.IS_COMPLETED_COLUMN,item.isCompleted());
   			     contentResolver.update(ToDoContentProvider.CONTENT_URI,contentValues,ToDoContentProvider.NAME_COLUMN+"='"+item.getName()+"'",null);
   			}
           });
        
        pullableLayout.setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				getLoaderManager().restartLoader(0,null,MainActivity.this);
			}
        	
        });
    }
    
    //初始化数据
    private void initData(){
    	contentResolver=getContentResolver();
    	handler=new Handler();
    }

    private void initView(){
        initTaskListView();
        initTaskLoader();
    }
    
    private void initTaskListView(){
        taskList=new ArrayList<ToDoTask>();
        listAdapter=new TaskListAdapter(this,R.layout.task_list_item,taskList);
        taskListView=(SlideListView)findViewById(R.id.slideTaskList);
        taskListView.setAdapter(listAdapter);
        pullableLayout=(PullableLayout)findViewById(R.id.refresh_view);
    }

    private void initTaskLoader(){
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAddNewItem(ToDoTask item) {
        if(item==null) return;
//        ToDoTaskDao.getInstance(this).insert(item);
        ContentValues contentValues=new ContentValues();
        contentValues.put(ToDoContentProvider.NAME_COLUMN,item.getName());
        contentValues.put(ToDoContentProvider.DATE_COLUMN,item.getCreateDate());
        contentValues.put(ToDoContentProvider.PLANDAY_COLUMN,item.getPlanDays());
        contentValues.put(ToDoContentProvider.HABBIT_COLUMN,item.isHabbit());
        contentValues.put(ToDoContentProvider.TODAY_ISDO_COLUMN,item.isTodayIsDo());
        contentValues.put(ToDoContentProvider.DURING_DATE_COLUMN,item.getDuringDays());
        contentValues.put(ToDoContentProvider.INTERRUPT_DAY_COLUMN,item.getInterruptedDays());
        contentValues.put(ToDoContentProvider.IS_COMPLETED_COLUMN,item.isCompleted());
        contentResolver.insert(ToDoContentProvider.CONTENT_URI, contentValues);
        getLoaderManager().restartLoader(0,null,this);
        
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader=new CursorLoader(this,ToDoContentProvider.CONTENT_URI,null,ToDoContentProvider.IS_COMPLETED_COLUMN+"=0",null,ToDoContentProvider.TODAY_ISDO_COLUMN+" ASC,"+ToDoContentProvider.KEY_ID+" DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        restartInitTaskListAdapter(loader, cursor);
//        pullableLayout.hide("SUCCESS");	
        updateRefreshdedUI();
    }

    private void restartInitTaskListAdapter(Loader<Cursor> loader, Cursor cursor){
        int keyTaskIndex=cursor.getColumnIndexOrThrow(ToDoContentProvider.NAME_COLUMN);
        int createDateIndex=cursor.getColumnIndexOrThrow(ToDoContentProvider.DATE_COLUMN);
        taskList.clear();
        while (cursor.moveToNext()){
            String name=cursor.getString(keyTaskIndex);
            String createDate=cursor.getString(createDateIndex);
            int planDays=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.PLANDAY_COLUMN));
            int interruptDays=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.INTERRUPT_DAY_COLUMN));
            int duaringDay=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.DURING_DATE_COLUMN));
            int isTodayDo=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.TODAY_ISDO_COLUMN));
            int habbit=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.HABBIT_COLUMN));
            int completed=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.IS_COMPLETED_COLUMN));

            ToDoTask toDoTask=new ToDoTask(name,createDate,null, planDays, habbit, interruptDays, duaringDay, completed,isTodayDo);
            taskList.add(toDoTask);
        }
        cursor.close();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        //搜索菜单处理
        {
        	SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
            SearchableInfo searchableInfo=searchManager.getSearchableInfo(getComponentName());
            SearchView searchView=(SearchView)menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchableInfo);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            Uri uri = Uri.fromFile(new File(getFilesDir(), "test_1.jpg"));
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

            ShareActionProvider shareProvider = new ShareActionProvider(this);
            shareProvider.setShareIntent(shareIntent);

            MenuItem shareMenuItem=menu.findItem(R.id.menu_share);
           
            shareMenuItem.setActionProvider(shareProvider);
        }
               
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(this,id+"",Toast.LENGTH_LONG).show();
        Intent newTaskIntent=new Intent(MainActivity.this,NewTaskActivity.class);
       
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 更新可下拉View的界面，隐藏下拉头
     * 使用有问题
     */
    private void updateRefreshdedUI(){
    	 handler=new Handler(){
    		public void handleMessage(Message msg) {
    			System.out.println("begin----");
    			pullableLayout.hide("SUCCESS");	
    		}
    	};
    	new Thread(){
    		public void run(){
    			handler.sendMessage(new Message());
    		}
    	}.start();
    }
}
