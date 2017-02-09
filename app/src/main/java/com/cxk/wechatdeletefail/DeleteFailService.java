package com.cxk.wechatdeletefail;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * Created by cxk on 2017/2/9.
 * <p>
 * email:471497006@qq.com
 * <p>
 * 屏蔽微信"删除联系人"和"拉黑"功能
 */

public class DeleteFailService extends AccessibilityService {

    /**
     * 判断是不是删除联系人弹框
     */
    private boolean isDeleteDialog;
    /**
     * 判断是不是拉黑联系人弹框
     */
    private boolean isDefriendDialog;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            //进入聊天页面会触发该事件
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                //获取当前聊天页面根布局
                AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                //屏蔽微信"删除联系人"和"拉黑"功能
                DeleteFail(rootNode);
                break;

        }

    }

    /**
     * 遍历所有控件，判断是不是删除联系人界面或者拉黑界面，如果是则模拟点击"取消"，让它消失。
     *
     * @param rootNode
     */
    private void DeleteFail(AccessibilityNodeInfo rootNode) {
        if(rootNode!=null){
            for (int i = 0; i < rootNode.getChildCount(); i++) {
                AccessibilityNodeInfo node = rootNode.getChild(i);
                //判断是不是删除联系人弹出框
                if("android.widget.TextView".equals(node.getClassName().toString())){
                    if(!TextUtils.isEmpty(node.getText())){
                        if(node.getText().toString().contains("将同时删除与该联系人的聊天记录")){
                            isDeleteDialog=true;
                        }
                    }
                }

                //判断是不是拉黑联系人弹出框
                if("android.widget.TextView".equals(node.getClassName().toString())){
                    if(!TextUtils.isEmpty(node.getText())){
                        if(node.getText().toString().contains("你将不再收到对方的消息，并且你们相互看不到对方朋友圈的更新")){
                            isDefriendDialog=true;
                        }
                    }
                }

                //模拟点击取消按钮让对话框消失
                if("android.widget.Button".equals(node.getClassName().toString())){
                    if(isDeleteDialog||isDefriendDialog){
                        if(!TextUtils.isEmpty(node.getText())){
                            if(node.getText().toString().equals("取消")){
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "我快被终结了啊-----", Toast.LENGTH_SHORT).show();
    }

    /**
     * 服务开始连接
     */
    @Override
    protected void onServiceConnected() {
        Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
        super.onServiceConnected();
    }

    /**
     * 服务断开
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "服务已被关闭", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }
}
