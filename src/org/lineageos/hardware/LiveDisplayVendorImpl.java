/*
 * Copyright (C) 2015 The CyanogenMod Project
 * Copyright (C) 2018 The LineageOS Project
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

    private static int sFeatures;

    static {
        try {
            sFeatures = IColor.getService().getSupportedFeatures();
            if (sFeatures > 0) {
                Log.i(TAG, "Using LiveDisplay backend (features: " + sFeatures + ")");
            }
        } catch (Exception e) {
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
            return Utils.HIDLModeListToArray(IColor.getService().getDisplayModes());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static DisplayMode native_getCurrentDisplayMode() {
        try {
            DisplayMode mode = Utils.fromHIDLMode(IColor.getService().getCurrentDisplayMode());
            return mode.id == -1 ? null : mode;
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static DisplayMode native_getDefaultDisplayMode() {
        try {
            DisplayMode mode = Utils.fromHIDLMode(IColor.getService().getDefaultDisplayMode());
            return mode.id == -1 ? null : mode;
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static boolean native_setDisplayMode(DisplayMode mode, boolean makeDefault) {
        try {
            return IColor.getService().setDisplayMode(mode.id, makeDefault);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_setAdaptiveBacklightEnabled(boolean enabled) {
        try {
            return IColor.getService().setAdaptiveBacklightEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_isAdaptiveBacklightEnabled() {
        try {
            return IColor.getService().isAdaptiveBacklightEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_setOutdoorModeEnabled(boolean enabled) {
        try {
            return IColor.getService().setOutdoorModeEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_isOutdoorModeEnabled() {
        try {
            return IColor.getService().isOutdoorModeEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static Range<Integer> native_getColorBalanceRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().getColorBalanceRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static int native_getColorBalance() {
        try {
            return IColor.getService().getColorBalance();
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return 0;
        }
    }

    public static boolean native_setColorBalance(int value) {
        try {
            return IColor.getService().setColorBalance(value);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static boolean native_setPictureAdjustment(final HSIC hsic) {
        try {
            return IColor.getService().setPictureAdjustment(Utils.toHIDLHSIC(hsic));
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return false;
        }
    }

    public static HSIC native_getPictureAdjustment() {
        try {
            return Utils.fromHIDLHSIC(IColor.getService().getPictureAdjustment());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static HSIC native_getDefaultPictureAdjustment() {
        try {
            return Utils.fromHIDLHSIC(IColor.getService().getDefaultPictureAdjustment());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getHueRange() {
        try {
            return Utils.fromHIDLIntRange(IColor.getService().getHueRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getSaturationRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().getSaturationRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getIntensityRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().getIntensityRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getContrastRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().getContrastRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    public static Range<Float> native_getSaturationThresholdRange() {
        try {
            return Utils.fromHIDLRange(IColor.getService().getSaturationThresholdRange());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }
}
