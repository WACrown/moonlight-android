package com.limelight.nvstream.input;

import android.view.KeyEvent;

public class KeyboardPacket {
    public static final byte KEY_DOWN = 0x03;
    public static final byte KEY_UP = 0x04;

    public static final byte MODIFIER_SHIFT = 0x01;
    public static final byte MODIFIER_CTRL = 0x02;
    public static final byte MODIFIER_ALT = 0x04;

    public static int getKeycode(String key){
        switch (key){
            case "A" :return KeyEvent.KEYCODE_A;
            case "B" :return KeyEvent.KEYCODE_B;
            case "C" :return KeyEvent.KEYCODE_C;
            case "D" :return KeyEvent.KEYCODE_D;
            case "E" :return KeyEvent.KEYCODE_E;
            case "F" :return KeyEvent.KEYCODE_F;
            case "G" :return KeyEvent.KEYCODE_G;
            case "H" :return KeyEvent.KEYCODE_H;
            case "I" :return KeyEvent.KEYCODE_I;
            case "J" :return KeyEvent.KEYCODE_J;
            case "K" :return KeyEvent.KEYCODE_K;
            case "L" :return KeyEvent.KEYCODE_L;
            case "M" :return KeyEvent.KEYCODE_M;
            case "N" :return KeyEvent.KEYCODE_N;
            case "O" :return KeyEvent.KEYCODE_O;
            case "P" :return KeyEvent.KEYCODE_P;
            case "Q" :return KeyEvent.KEYCODE_Q;
            case "R" :return KeyEvent.KEYCODE_R;
            case "S" :return KeyEvent.KEYCODE_S;
            case "T" :return KeyEvent.KEYCODE_T;
            case "U" :return KeyEvent.KEYCODE_U;
            case "V" :return KeyEvent.KEYCODE_V;
            case "W" :return KeyEvent.KEYCODE_W;
            case "X" :return KeyEvent.KEYCODE_X;
            case "Y" :return KeyEvent.KEYCODE_Y;
            case "Z" :return KeyEvent.KEYCODE_Z;
            //
            case "CTRLL"  :return KeyEvent.KEYCODE_CTRL_LEFT;
            case "SHIFTL" :return KeyEvent.KEYCODE_SHIFT_LEFT;
            case "CTRLR"  :return KeyEvent.KEYCODE_CTRL_RIGHT;
            case "SHIFTR" :return KeyEvent.KEYCODE_SHIFT_RIGHT;
            case "ALTL"   :return KeyEvent.KEYCODE_ALT_LEFT;
            case "ALTR"   :return KeyEvent.KEYCODE_ALT_RIGHT;
            case "ENTER"  :return KeyEvent.KEYCODE_ENTER;
            case "BACK"   :return KeyEvent.KEYCODE_DEL;
            case "SPACE"  :return KeyEvent.KEYCODE_SPACE;
            case "TAB"    :return KeyEvent.KEYCODE_TAB;
            case "CAPS"   :return KeyEvent.KEYCODE_CAPS_LOCK;
            case "ESC"    :return KeyEvent.KEYCODE_ESCAPE;
            //功能键
            case "WIN"    :return KeyEvent.KEYCODE_META_LEFT;
            case "DEL"    :return KeyEvent.KEYCODE_FORWARD_DEL;
            case "INS"    :return KeyEvent.KEYCODE_INSERT;
            case "HOME"   :return KeyEvent.KEYCODE_MOVE_HOME;
            case "END"    :return KeyEvent.KEYCODE_MOVE_END;
            case "PGUP"   :return KeyEvent.KEYCODE_PAGE_UP;
            case "PGDN"   :return KeyEvent.KEYCODE_PAGE_DOWN;
            case "BREAK"  :return KeyEvent.KEYCODE_BREAK;
            case "SLCK"   :return KeyEvent.KEYCODE_SCROLL_LOCK;
            case "PRINT"  :return KeyEvent.KEYCODE_SYSRQ;
            case "UP"     :return KeyEvent.KEYCODE_DPAD_UP;
            case "DOWN"   :return KeyEvent.KEYCODE_DPAD_DOWN;
            case "LEFT"   :return KeyEvent.KEYCODE_DPAD_LEFT;
            case "RIGHT"  :return KeyEvent.KEYCODE_DPAD_RIGHT;
            //
            case "1"   :return KeyEvent.KEYCODE_1;
            case "2"   :return KeyEvent.KEYCODE_2;
            case "3"   :return KeyEvent.KEYCODE_3;
            case "4"   :return KeyEvent.KEYCODE_4;
            case "5"   :return KeyEvent.KEYCODE_5;
            case "6"   :return KeyEvent.KEYCODE_6;
            case "7"   :return KeyEvent.KEYCODE_7;
            case "8"   :return KeyEvent.KEYCODE_8;
            case "9"   :return KeyEvent.KEYCODE_9;
            case "0"   :return KeyEvent.KEYCODE_0;
            //
            case "F1"  :return KeyEvent.KEYCODE_F1;
            case "F2"  :return KeyEvent.KEYCODE_F2;
            case "F3"  :return KeyEvent.KEYCODE_F3;
            case "F4"  :return KeyEvent.KEYCODE_F4;
            case "F5"  :return KeyEvent.KEYCODE_F5;
            case "F6"  :return KeyEvent.KEYCODE_F6;
            case "F7"  :return KeyEvent.KEYCODE_F7;
            case "F8"  :return KeyEvent.KEYCODE_F8;
            case "F9"  :return KeyEvent.KEYCODE_F9;
            case "F10" :return KeyEvent.KEYCODE_F10;
            case "F11" :return KeyEvent.KEYCODE_F11;
            case "F12" :return KeyEvent.KEYCODE_F12;
            //
            case "~"   :return KeyEvent.KEYCODE_GRAVE;
            case "_"   :return KeyEvent.KEYCODE_MINUS;
            case "="   :return KeyEvent.KEYCODE_EQUALS;
            case "["   :return KeyEvent.KEYCODE_LEFT_BRACKET;
            case "]"   :return KeyEvent.KEYCODE_RIGHT_BRACKET;
            case "\\"  :return KeyEvent.KEYCODE_BACKSLASH;
            case ";"   :return KeyEvent.KEYCODE_SEMICOLON;
            case "\""  :return KeyEvent.KEYCODE_APOSTROPHE;
            case "<"   :return KeyEvent.KEYCODE_COMMA;
            case ">"   :return KeyEvent.KEYCODE_PERIOD;
            case "/"   :return KeyEvent.KEYCODE_SLASH;
            //
            case "NUM1"   :return KeyEvent.KEYCODE_NUMPAD_1;
            case "NUM2"   :return KeyEvent.KEYCODE_NUMPAD_2;
            case "NUM3"   :return KeyEvent.KEYCODE_NUMPAD_3;
            case "NUM4"   :return KeyEvent.KEYCODE_NUMPAD_4;
            case "NUM5"   :return KeyEvent.KEYCODE_NUMPAD_5;
            case "NUM6"   :return KeyEvent.KEYCODE_NUMPAD_6;
            case "NUM7"   :return KeyEvent.KEYCODE_NUMPAD_7;
            case "NUM8"   :return KeyEvent.KEYCODE_NUMPAD_8;
            case "NUM9"   :return KeyEvent.KEYCODE_NUMPAD_9;
            case "NUM0"   :return KeyEvent.KEYCODE_NUMPAD_0;
            case "NUM."   :return KeyEvent.KEYCODE_NUMPAD_DOT;
            case "NUM+"   :return KeyEvent.KEYCODE_NUMPAD_ADD;
            case "NUM_"   :return KeyEvent.KEYCODE_NUMPAD_SUBTRACT;
            case "NUM*"   :return KeyEvent.KEYCODE_NUMPAD_MULTIPLY;
            case "NUM/"   :return KeyEvent.KEYCODE_NUMPAD_DIVIDE;
            case "NUMENT" :return KeyEvent.KEYCODE_NUMPAD_ENTER;
            case "NUMLCK" :return KeyEvent.KEYCODE_NUM_LOCK;

        }
        return -1;
    }
}
