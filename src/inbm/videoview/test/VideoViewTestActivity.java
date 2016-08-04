package inbm.videoview.test;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewTestActivity extends Activity {
	private VideoView mVideoView;
	private Button mButton;
	private int clickCount = 0;
	private int videoStartPoint;
	private int videoEndPoint;
	private static final int START_POINT = 0;
	private static final int END_POINT = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		
		final MediaController controller = new MediaController(VideoViewTestActivity.this);
		// 비디오뷰 객체와 연결하여 스트리밍 동영상 재생
		mVideoView = (VideoView) findViewById(R.id.surface_view);
        //mVideoView.setVideoURI(Uri.parse("android.resource://" +		 getPackageName() +"/"+R.raw.dontlisten));
        mVideoView.setVideoURI(Uri.parse("http://112.217.207.84:8080/res/party.mp4"));
		
		//mVideoView.setVideoURI(Uri.parse("rtsp://msoobakc-visangflv02.cdn.x-cdn.com/msoobakc/_definst_/Soobakc_com/2012_M/001/mse/14900/m0a14900(mse)_01_700.mp4"));
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                int duration = mVideoView.getDuration();
                mVideoView.requestFocus();
                mVideoView.start();
                controller.show();

            }
        });

		// 버튼 클릭 리스너에 구간반복 시작, 끝, 해지 처
		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.i("action", " click button");
				// TODO Auto-generated method stub
				if (clickCount % 3 == START_POINT && mVideoView.isPlaying()) {

					setRepeatStartPoint();
					clickCount++;
				} else if (clickCount % 3 == END_POINT
						&& mVideoView.isPlaying()) {
					setRepeatEndPoint();
					mVideoView.seekTo(videoStartPoint);
					clickCount++;
				} else if (clickCount % 3 == 2 && mVideoView.isPlaying()) {
					removeRepeatPoints();
					clickCount++;
				}

			}

		});
		
		//0.05초마다 동영상이 구간반복의 끝지점에 도달했는지 체크 

		Timer timer = new Timer();
		MyTask myTask = new MyTask();
		timer.schedule(myTask, 1000, 10);

	}

	//구간반복 해제 
	protected void removeRepeatPoints() {
		// TODO Auto-generated method stub
		mButton.setText("해제");
		Log.i("action", "remove start and end point");
		videoStartPoint = 0;
		videoEndPoint = 0;

	}

	//구간반복 끝점 설정 
	protected void setRepeatEndPoint() {
		// TODO Auto-generated method stub
		Log.i("action", "set end point");
		mButton.setText("끝 지점(" + mVideoView.getCurrentPosition() + ")");
		videoEndPoint = mVideoView.getCurrentPosition();

	}
	
	//구간반복 시작점 설정
	protected void setRepeatStartPoint() {
		// TODO Auto-generated method stub
		mButton.setText("시작 지점(" + mVideoView.getCurrentPosition() + ")");
		Log.i("action", "set start point");
		videoStartPoint = mVideoView.getCurrentPosition();

	}
	
	
	//동영상이 구간반복 끝지점에 도달했는지 확인
	class MyTask extends TimerTask {
		public void run() {

			Log.i("position", "현재위치: " + mVideoView.getCurrentPosition()
					+ "    시작점 위치: " + videoStartPoint + "    끝점 위치: "
					+ videoEndPoint);
			if (mVideoView.getCurrentPosition() != 0
					&& mVideoView.getCurrentPosition() == videoEndPoint) {
				mVideoView.seekTo(videoStartPoint);
			}
		}
	}
}