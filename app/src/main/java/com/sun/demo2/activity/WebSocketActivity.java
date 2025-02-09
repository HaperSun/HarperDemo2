package com.sun.demo2.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.sun.base.base.activity.BaseMvpActivity;
import com.sun.base.util.CollectionUtil;
import com.sun.base.util.GsonUtil;
import com.sun.demo2.R;
import com.sun.demo2.databinding.ActivityWebSocketBinding;
import com.sun.demo2.model.AddressBook1Bean;
import com.sun.demo2.model.response.LoginResponse;
import com.sun.media.video.model.SuperPlayerModel;
import com.sun.media.video.ui.activity.FeedVideoActivity;
import com.sun.media.video.ui.activity.ShortVideoActivity;
import com.sun.media.video.ui.activity.VideoPlayActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Harper
 * @date: 2022/6/21
 * @note: WebSocket使用
 */
public class WebSocketActivity extends BaseMvpActivity<ActivityWebSocketBinding> implements View.OnClickListener {

    private Context mContext;
    private ScheduledExecutorService mExecutorService;
    private WebSocketClient mSocketClient;
    private boolean mIsStartTimer;

    public static void start(Context context) {
        Intent intent = new Intent(context, WebSocketActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_web_socket;
    }

    @Override
    public void initView() {
        mContext = this;
        if (mExecutorService == null) {
            mExecutorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern(TAG).daemon(true).build());
        }
        try {
            mSocketClient = new WebSocketClient(new URI("wss://indoor.yunweizhi.net/wss/wsgettagpos"), new Draft_6455()) {

                @Override
                public void onWebsocketPong(WebSocket conn, Framedata f) {
                    super.onWebsocketPong(conn, f);
                }

                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                }

                @Override
                public void onMessage(String s) {
                    // 收到消息的回调
                    if (!TextUtils.isEmpty(s)) {
                        try {
                            LoginResponse response = GsonUtil.getGson().fromJson(s, LoginResponse.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {
                    // 连接出错的回调
                }
            };
            //连接
            mSocketClient.connectBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        vdb.tvSuper.setOnClickListener(this);
        vdb.tvShort.setOnClickListener(this);
        vdb.tvFeed.setOnClickListener(this);
        startTimer(getSocketRequestStr(AddressBook1Bean.getData()));
    }

    private String getSocketRequestStr(List<AddressBook1Bean> list) {
        String tags = CollectionUtil.isEmpty(list) ? "" : GsonUtil.getGson().toJson(list);
        Map<String, String> map = new HashMap<>();
        map.put("key", "mMapKey");
        map.put("tags", tags);
        return GsonUtil.getGson().toJson(map);
    }

    private void startTimer(String s) {
        mExecutorService.scheduleAtFixedRate(() -> {
            if (mIsStartTimer) {
                runOnUiThread(() -> sendSocket(s));
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void sendSocket(String s) {
        if (mSocketClient == null || mSocketClient.isClosed()) {
            try {
                //连接socket
                assert mSocketClient != null;
                mSocketClient.connectBlocking();
                sendSocket(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                mSocketClient.send(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mIsStartTimer = false;
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
        if (mSocketClient != null && mSocketClient.isOpen()) {
            try {
                mSocketClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_super:
                VideoPlayActivity.start(mContext,new SuperPlayerModel());
                break;
            case R.id.tv_short:
                ShortVideoActivity.start(mContext);
                break;
            case R.id.tv_feed:
                FeedVideoActivity.start(mContext);
                break;
            default:
                break;
        }
    }
}