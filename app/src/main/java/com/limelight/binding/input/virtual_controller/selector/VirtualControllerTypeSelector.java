package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.widget.Spinner;

public abstract class VirtualControllerTypeSelector extends Spinner {
    public VirtualControllerTypeSelector(Context context) {
        super(context);
    }

    public abstract void refreshLayout();
}
