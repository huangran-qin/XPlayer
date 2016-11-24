package cn.ikan.libs.player.inter;

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Taurus on 2016/10/17.
 */

public interface OnPlayerKeyListener {
    boolean onKeyDownPadUp(View v, int keyCode, KeyEvent event);
    boolean onKeyDownPadDown(View v, int keyCode, KeyEvent event);
    boolean onKeyDownPadLeft(View v, int keyCode, KeyEvent event);
    boolean onKeyDownPadRight(View v, int keyCode, KeyEvent event);
    boolean onKeyDownPadEnter(View v, int keyCode, KeyEvent event);
    boolean onKeyDownBack(View v, int keyCode, KeyEvent event);
    boolean onKeyDownMenu(View v, int keyCode, KeyEvent event);

    boolean onKeyUpPadUp(View v, int keyCode, KeyEvent event);
    boolean onKeyUpPadDown(View v, int keyCode, KeyEvent event);
    boolean onKeyUpPadLeft(View v, int keyCode, KeyEvent event);
    boolean onKeyUpPadRight(View v, int keyCode, KeyEvent event);
    boolean onKeyUpPadEnter(View v, int keyCode, KeyEvent event);
    boolean onKeyUpBack(View v, int keyCode, KeyEvent event);
    boolean onKeyUpMenu(View v, int keyCode, KeyEvent event);
}
