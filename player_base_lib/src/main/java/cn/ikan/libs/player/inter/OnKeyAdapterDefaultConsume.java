package cn.ikan.libs.player.inter;

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Taurus on 16/10/19.
 */
public class OnKeyAdapterDefaultConsume implements View.OnKeyListener {

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (keyEvent.getAction()) {
            case KeyEvent.ACTION_DOWN:
                return onKeyActionDown(view, i, keyEvent);
            case KeyEvent.ACTION_UP:
                return onKeyActionUp(view, i, keyEvent);
        }
        return false;
    }

    public boolean onKeyActionUp(View view, int keyCode, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyActionDown(View view, int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyDownPadUp(view, keyEvent);
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyDownPadDown(view, keyEvent);
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyDownPadLeft(view, keyEvent);
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyDownPadRight(view, keyEvent);
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                return onKeyDownPadEnter(view,keyEvent);
        }
        return true;
    }

    public boolean onKeyDownPadEnter(View view, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyDownPadUp(View view, KeyEvent keyEvent) {
        return true;
    }

    public boolean onKeyDownPadDown(View view, KeyEvent keyEvent) {
        return true;
    }

    public boolean onKeyDownPadLeft(View view, KeyEvent keyEvent) {
        return true;
    }

    public boolean onKeyDownPadRight(View view, KeyEvent keyEvent) {
        return true;
    }
}
