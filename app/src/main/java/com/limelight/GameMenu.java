package com.limelight;

import android.app.AlertDialog;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.limelight.binding.input.GameInputDevice;
import com.limelight.binding.input.KeyboardTranslator;
import com.limelight.binding.input.advance_setting.Controller;
import com.limelight.binding.input.advance_setting.ControllerManager;
import com.limelight.binding.input.advance_setting.CombineKeyBean;
import com.limelight.nvstream.NvConnection;
import com.limelight.nvstream.input.KeyboardPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide options for ongoing Game Stream.
 * <p>
 * Shown on back action in game activity.
 */
public class GameMenu {

    private static final long TEST_GAME_FOCUS_DELAY = 10;
    private static final long KEY_UP_DELAY = 25;
    private static final long KEY_INTERVAL_TIME = 3;
    private static Controller openedController;

    public static class MenuOption {
        private final String label;
        private final boolean withGameFocus;
        private final Runnable runnable;

        public MenuOption(String label, boolean withGameFocus, Runnable runnable) {
            this.label = label;
            this.withGameFocus = withGameFocus;
            this.runnable = runnable;
        }

        public MenuOption(String label, Runnable runnable) {
            this(label, false, runnable);
        }
    }

    private final Game game;
    private final NvConnection conn;
    private final GameInputDevice device;
    private final ControllerManager controllerManager;

    public GameMenu(Game game, NvConnection conn, GameInputDevice device, ControllerManager controllerManager) {
        this.game = game;
        this.conn = conn;
        this.device = device;
        this.controllerManager = controllerManager;

        showMenu();
    }

    private String getString(int id) {
        return game.getResources().getString(id);
    }

    private static byte getModifier(short key) {
        switch (key) {
            case KeyboardTranslator.VK_LSHIFT:
                return KeyboardPacket.MODIFIER_SHIFT;
            case KeyboardTranslator.VK_LCONTROL:
                return KeyboardPacket.MODIFIER_CTRL;
            case KeyboardTranslator.VK_LWIN:
                return KeyboardPacket.MODIFIER_META;

            default:
                return 0;
        }
    }

    private void sendKeys(short[] keys) {
        final byte[] modifier = {(byte) 0};

        for (short key : keys) {
            conn.sendKeyboardInput(key, KeyboardPacket.KEY_DOWN, modifier[0], (byte) 0);

            // Apply the modifier of the pressed key, e.g. CTRL first issues a CTRL event (without
            // modifier) and then sends the following keys with the CTRL modifier applied
            modifier[0] |= getModifier(key);
        }

        new Handler().postDelayed((() -> {

            for (int pos = keys.length - 1; pos >= 0; pos--) {
                short key = keys[pos];

                // Remove the keys modifier before releasing the key
                modifier[0] &= ~getModifier(key);

                conn.sendKeyboardInput(key, KeyboardPacket.KEY_UP, modifier[0], (byte) 0);
            }
        }), KEY_UP_DELAY);
    }

    private void runWithGameFocus(Runnable runnable) {
        // Ensure that the Game activity is still active (not finished)
        if (game.isFinishing()) {
            return;
        }
        // Check if the game window has focus again, if not try again after delay
        if (!game.hasWindowFocus()) {
            new Handler().postDelayed(() -> runWithGameFocus(runnable), TEST_GAME_FOCUS_DELAY);
            return;
        }
        // Game Activity has focus, run runnable
        runnable.run();
    }

    private void run(MenuOption option) {
        if (option.runnable == null) {
            return;
        }

        if (option.withGameFocus) {
            runWithGameFocus(option.runnable);
        } else {
            option.runnable.run();
        }
    }

    private void showMenuDialog(String title, MenuOption[] options) {
        AlertDialog.Builder builder = new AlertDialog.Builder(game);
        builder.setTitle(title);

        final ArrayAdapter<String> actions =
                new ArrayAdapter<String>(game, android.R.layout.simple_list_item_1);

        for (MenuOption option : options) {
            actions.add(option.label);
        }

        builder.setAdapter(actions, (dialog, which) -> {
            String label = actions.getItem(which);
            for (MenuOption option : options) {
                if (!label.equals(option.label)) {
                    continue;
                }

                run(option);
                break;
            }
        });

        builder.show();
    }

    private void showDeleteSpecialKeys(){
        List<CombineKeyBean> specialKeyBeans = controllerManager.getCombineKeyController().loadCombineKeyConfig();
        List<MenuOption> menuOptions = new ArrayList<>();

        for (int specialKeyNum = 0;specialKeyNum < specialKeyBeans.size();specialKeyNum ++){
            CombineKeyBean combineKeyBean = specialKeyBeans.get(specialKeyNum);

            menuOptions.add(new MenuOption("删除:" + combineKeyBean.getName(),
                    () -> controllerManager.getCombineKeyController().deleteCombineKey(combineKeyBean) ));

        }
        showMenuDialog("删除指令",menuOptions.toArray(new MenuOption[0]));
        menuOptions.add(new MenuOption(getString(R.string.game_menu_cancel), null));
    }

    private void showSpecialKeysMenu() {
        if (controllerManager != null){
            List<CombineKeyBean> specialKeyBeans = controllerManager.getCombineKeyController().loadCombineKeyConfig();
            List<MenuOption> menuOptions = new ArrayList<>();
            for (CombineKeyBean bean : specialKeyBeans){
                menuOptions.add(new MenuOption(bean.getName(), () -> sendKeys(bean.getKeyValue())));
            }
            menuOptions.add(new MenuOption("增加指令", () -> controllerManager.getCombineKeyController().open()));
            menuOptions.add(new MenuOption("删除指令", () -> showDeleteSpecialKeys()));
            menuOptions.add(new MenuOption(getString(R.string.game_menu_cancel), null));

            showMenuDialog(getString(R.string.game_menu_send_keys), menuOptions.toArray(new MenuOption[0]));
        } else {
            showMenuDialog(getString(R.string.game_menu_send_keys), new MenuOption[]{
                    new MenuOption(getString(R.string.game_menu_send_keys_esc),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_ESCAPE})),
                    new MenuOption(getString(R.string.game_menu_send_keys_f11),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_F11})),
                    new MenuOption(getString(R.string.game_menu_send_keys_ctrl_v),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_LCONTROL, KeyboardTranslator.VK_V})),
                    new MenuOption(getString(R.string.game_menu_send_keys_win),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_LWIN})),
                    new MenuOption(getString(R.string.game_menu_send_keys_win_d),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_LWIN, KeyboardTranslator.VK_D})),
                    new MenuOption(getString(R.string.game_menu_send_keys_win_g),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_LWIN, KeyboardTranslator.VK_G})),
                    new MenuOption(getString(R.string.game_menu_send_keys_shift_tab),
                            () -> sendKeys(new short[]{KeyboardTranslator.VK_LSHIFT, KeyboardTranslator.VK_TAB})),
                    new MenuOption(getString(R.string.game_menu_cancel), null),
            });
        }


    }

    private void showMenu() {
        if (openedController != null){
            openedController.close();
            openedController = null;
            return;
        }
        List<MenuOption> options = new ArrayList<>();

        if (controllerManager != null){
            options.add(new MenuOption("配置选择", () -> {
                controllerManager.getConfigController().open();
                openedController = controllerManager.getConfigController();

            }));
            options.add(new MenuOption("按键编辑", () -> {
                controllerManager.getEditController().open();
                openedController = controllerManager.getEditController();
            }));
            options.add(new MenuOption("配置设置", () -> {
                controllerManager.getSettingController().open();
                openedController = controllerManager.getSettingController();
            }));
            options.add(new MenuOption(getString(R.string.game_menu_toggle_all_keyboard), () -> {
                controllerManager.getKeyboardController().open();
                openedController = controllerManager.getKeyboardController();
            }));
        }
        options.add(new MenuOption(getString(R.string.game_menu_send_keys), () -> showSpecialKeysMenu()));
        options.add(new MenuOption(getString(R.string.game_menu_toggle_keyboard), true,
                () -> game.toggleKeyboard()));

        if (device != null) {
            options.addAll(device.getGameMenuOptions());
        }
        options.add(new MenuOption(getString(R.string.game_menu_toggle_performance_overlay), () -> game.togglePerformanceOverlay()));
        options.add(new MenuOption(getString(R.string.game_menu_disconnect), () -> game.disconnect()));
        options.add(new MenuOption(getString(R.string.game_menu_cancel), null));

        showMenuDialog("Game Menu", options.toArray(new MenuOption[options.size()]));
    }
}
