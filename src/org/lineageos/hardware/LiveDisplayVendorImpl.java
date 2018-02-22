/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.hardware;

import android.os.RemoteException;
import android.util.Log;
import android.util.Range;

import lineageos.hardware.DisplayMode;
import lineageos.hardware.HSIC;

import vendor.lineage.livedisplay.V1_0.IColor;

/**
 * This class loads an implementation of the LiveDisplay native interface.
 */
public class LiveDisplayVendorImpl {

    public static final String TAG = "LiveDisplayVendorImpl";

    public static final int DISPLAY_MODES = 0x1;
    public static final int COLOR_BALANCE = 0x2;
    public static final int OUTDOOR_MODE = 0x4;
    public static final int ADAPTIVE_BACKLIGHT = 0x8;
    public static final int PICTURE_ADJUSTMENT = 0x10;

    private static int sFeatures;

    static {
        try {
            sFeatures = IColor.getService().native_getSupportedFeatures();
            if (sFeatures > 0) {
                Log.i(TAG, "Using LiveDisplay backend (features: " + sFeatures + ")");
            }
        } catch (RemoteException e) {
            Log.e(TAG, "IColor.getService exception: " + e);
            reset();
        }
    }

    private static void reset() {
        sFeatures = 0;
    }

    public static boolean hasNativeFeature(int feature) {
        Log.d(TAG, "hasNativeFeature: sFeatures=" + Integer.toString(sFeatures));
        return (sFeatures & feature) != 0;
    }

    public static DisplayMode[] native_getDisplayModes() {
        try {
            return Utils.HIDLModeListToArray(IColor.getService().native_getDisplayModes());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static DisplayMode native_getCurrentDisplayMode() {
        try {
            DisplayMode mode = Utils.fromHIDLMode(IColor.getService().native_getCurrentDisplayMode());
            return mode.id == -1 ? null : mode;
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static DisplayMode native_getDefaultDisplayMode() {
        try {
            DisplayMode mode = Utils.fromHIDLMode(IColor.getService().native_getDefaultDisplayMode());
            return mode.id == -1 ? null : mode;
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static boolean native_setDisplayMode(DisplayMode mode, boolean makeDefault) {
        try {
            return IColor.getService().native_setDisplayMode(mode.id, makeDefault);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_setAdaptiveBacklightEnabled(boolean enabled) {
        try {
            return IColor.getService().native_setAdaptiveBacklightEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_isAdaptiveBacklightEnabled() {
        try {
            return IColor.getService().native_isAdaptiveBacklightEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_setOutdoorModeEnabled(boolean enabled) {
        try {
            return IColor.getService().native_setOutdoorModeEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_isOutdoorModeEnabled() {
        try {
            return IColor.getService().native_isOutdoorModeEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static Range<Integer> native_getColorBalanceRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().native_getColorBalanceRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static int native_getColorBalance() {
        try {
            return IColor.getService().native_getColorBalance();
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return 0;
        }
    }

    public static boolean native_setColorBalance(int value) {
        try {
            return IColor.getService().native_setColorBalance(value);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_setPictureAdjustment(final HSIC hsic) {
        try {
            return IColor.getService().native_setPictureAdjustment(Utils.toHIDLHSIC(hsic));
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static HSIC native_getPictureAdjustment() {
        try {
            return Utils.fromHIDLHSIC(IColor.getService().native_getPictureAdjustment());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static HSIC native_getDefaultPictureAdjustment() {
        try {
            return Utils.fromHIDLHSIC(IColor.getService().native_getPictureAdjustment());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getHueRange() {
        try {
            return Utils.fromHIDLIntRange(IColor.getService().native_getHueRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getSaturationRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().native_getSaturationRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getIntensityRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().native_getIntensityRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getContrastRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().native_getContrastRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getSaturationThresholdRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().native_getSaturationThresholdRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }
}