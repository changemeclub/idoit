package com.changeme.todolist.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changeme.todolist.R;

public class PullableLayout extends RelativeLayout {
	
	private View pullableView;
	// 下拉头
	private View refreshView;
	// 下拉的箭头
	private View pullView;
	// 正在刷新的图标
	private View refreshingView;
	// 刷新结果图标
	private View refreshStateImageView;
	// 刷新结果：成功或失败
	private TextView refreshStateTextView;
	
	//当前下拉view的状态,包括初始状态、下拉刷新状态、释放刷新、刷新完成状态
	private int state=INIT;
	private static final int INIT=0;
	private static final int RELEASE_TO_REFRESH=2;
	private static final int REFRESHING=3;
	private static final int DONE=4;
	
	private static final String RESULT_SUCCESS="SUCCESS";
	private static final String RESULT_FAIL="FAIL";
	
	private boolean isFirstLayout=true;
	
	private boolean isRefreshing=false;
	
	private Animation refreshAnimation;
	private Animation rotationAnimation;
	
	//下拉初始距离
	private float pullDownDistance=0;
	private float radio = 2;
	//刷新距离的边界
	private int refreshDist;
	
	private float lastY;
	private float downY ;
	
	private OnRefreshListener onRefreshListener;
	
	public PullableLayout(Context context){
		super(context);
		initView(context);
	}
	
	public PullableLayout(Context context, AttributeSet attrs){
		super(context,attrs);
		initView(context);
	}
	
	private void initView(Context context){
		// 初始化下拉布局
		//获得
		rotationAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		refreshAnimation=(RotateAnimation)AnimationUtils.loadAnimation(context,R.anim.rotating);
		
		LinearInterpolator lir = new LinearInterpolator();
		rotationAnimation.setInterpolator(lir);
		refreshAnimation.setInterpolator(lir);
	}
	
	public boolean dispatchTouchEvent(MotionEvent ev){
		//如果没有刷新，则响应下滑事件
		if(!isRefreshing){
			switch(ev.getAction()){
				case MotionEvent.ACTION_DOWN:
					downY = ev.getY();
					lastY = downY;
					break;
				case MotionEvent.ACTION_MOVE:
					pullDownDistance = pullDownDistance + (ev.getY() - lastY) / radio;
					lastY = ev.getY();
					// 根据下拉距离改变比例
					radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
							* pullDownDistance));
					if (pullDownDistance <= refreshDist
							&& (state == RELEASE_TO_REFRESH || state == DONE))
						{
							// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
							changeState(INIT);
						}
						if (pullDownDistance >= refreshDist && state == INIT)
						{
							// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
							changeState(RELEASE_TO_REFRESH);
						}
						if(pullDownDistance>(refreshDist/4)&&!isRefreshing){
							if(pullDownDistance>refreshDist)
								pullDownDistance=refreshDist;
							requestLayout();
						}
					break;
				case MotionEvent.ACTION_UP:
					//当下拉距离过小的时候，弹回。
					if (pullDownDistance < refreshDist){
						pullDownDistance=0;
						requestLayout();
					}else{
						if (pullDownDistance>=refreshDist&&state == RELEASE_TO_REFRESH)
						{
							pullDownDistance=refreshDist;
							changeState(REFRESHING);
							isRefreshing=true;
							onRefreshListener.onRefresh();
						}
					}
					break;
				default:
					break;
			}
		}
		super.dispatchTouchEvent(ev);
		return true;
	}
	
	private void changeState(int to){
		state=to;
		switch(state){
			case INIT:
				// 下拉布局初始状态
				refreshStateImageView.setVisibility(View.GONE);
				refreshStateTextView.setText(R.string.pull_to_refresh);
				pullView.clearAnimation();
				pullView.setVisibility(View.VISIBLE);
				break;
			case RELEASE_TO_REFRESH:
				// 释放刷新状态
				refreshStateTextView.setText(R.string.release_to_refresh);
				pullView.startAnimation(rotationAnimation);
				break;
			case REFRESHING:
				// 正在刷新状态
				pullView.clearAnimation();
				refreshingView.setVisibility(View.VISIBLE);
				pullView.setVisibility(View.INVISIBLE);
				refreshingView.startAnimation(refreshAnimation);
				refreshStateTextView.setText(R.string.refreshing);
				break;
			case DONE:
				refreshingView.clearAnimation();
				refreshingView.setVisibility(View.GONE);
				pullView.setVisibility(View.GONE);
				refreshStateTextView.setText(R.string.pull_to_refresh);
				pullDownDistance=0;
            	isRefreshing=false;
				break;
			}
		}
	
	public void onLayout(boolean changed, int l, int t, int r, int b){
		if(isFirstLayout){
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			isFirstLayout = false;
			pullView = refreshView.findViewById(R.id.pull_icon);
			refreshStateTextView = (TextView) refreshView
					.findViewById(R.id.state_tv);
			refreshingView = refreshView.findViewById(R.id.refreshing_icon);
			refreshStateImageView = refreshView.findViewById(R.id.state_iv);
			refreshDist = ((ViewGroup) refreshView).getChildAt(0)
					.getMeasuredHeight();
		}else{
			refreshView.layout(0, (int) (pullDownDistance) - refreshView.getMeasuredHeight(),
					refreshView.getMeasuredWidth(), (int) pullDownDistance);
			pullableView.layout(0, (int)pullDownDistance, pullableView.getMeasuredWidth(), (int)pullDownDistance+pullableView.getMeasuredHeight());
		}
	}

	/**
	 * 执行后台操作，并返回结果，隐藏下拉头
	 */
	public void hide(String result){
		if(result.equals(RESULT_SUCCESS)&&isRefreshing)
			changeState(DONE);
		System.out.println("end----");
    	requestLayout();
	}
	
	/**
	 * 刷新监听类
	 * @author ldc
	 *
	 */
	public interface OnRefreshListener{
		/**
		 * 执行刷新操作
		 */
		public void onRefresh();
	}

	public OnRefreshListener getOnRefreshListener() {
		return onRefreshListener;
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}
	
}
