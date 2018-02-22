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

import android.os.IHwBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Range;
import com.android.internal.annotations.GuardedBy;

import lineageos.hardware.DisplayMode;
import lineageos.hardware.HSIC;
import vendor.lineage.livedisplay.V1_0.IColor;

/**
 * This class loads an implementation of the LiveDisplay native interface.
 */
public class LiveDisplayVendorImpl implements IHwBinder.DeathRecipient {

    public static final String TAG = "LiveDisplayVendorImpl";

    @GuardedBy("this")
    private IColor mDeamon;

    private static int sFeatures;

    private LiveDisplayVendorImpl() {}

    private static class LiveDisplayVendorImplHolder {
        private static final LiveDisplayVendorImpl instance = new LiveDisplayVendorImpl();
    }

    public static LiveDisplayVendorImpl getInstance() {
        return LiveDisplayVendorImplHolder.instance;
    }

    private synchronized IColor getColorService() {
        if (mDeamon == null) {
            Log.v(TAG, "mDeamon was null, reconnect to livedisplay IColor");
            try {
                mDeamon = IColor.getService();
            } catch (java.util.NoSuchElementException e) {
                // Service doesn't exist or cannot be opened. Logged below.
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to get livedisplay IColor interface", e);
            }
            if (mDeamon == null) {
                Log.w(TAG, "livedisplay IColor HIDL not available");
                return null;
            }

            mDeamon.asBinder().linkToDeath(this, 0);
        }
        return mDeamon;
    }

    private static void reset() {
        sFeatures = 0;
        synchronized (this) {
            mDaemon = null;
        }
    }

    @Override
    public void serviceDied(long cookie) {
        Log.e(TAG, "livedisplay IColor HAL died");
        reset();
    }

    public boolean hasNativeFeature(int feature) {
        if (sFeatures == 0) {
            IColor daemon = getColorService();
            if (daemon == null) {
                Log.e(TAG, "hasNativeFeature: no livedisplay IColor HAL!");
                return false;
            }
            try {
                sFeatures = daemon.getSupportedFeatures();
                if (sFeatures > 0) {
                    Log.i(TAG, "Using livedisplay IColor backend (features: " + sFeatures + ")");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "hasNativeFeature failed", e);
                return false;
            }
        }
        Log.d(TAG, "hasNativeFeature: sFeatures=" + Integer.toString(sFeatures));
        return (sFeatures & feature) != 0;
    }

    public DisplayMode[] native_getDisplayModes() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getDisplayModes: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.HIDLModeListToArray(daemon.getDisplayModes());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getDisplayModes failed", e);
            reset();
        }
        return null;
    }

    public DisplayMode native_getCurrentDisplayMode() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getCurrentDisplayMode: no livedisplay IColor HAL!");
            return null;
        }
        try {
            DisplayMode mode = Utils.fromHIDLMode(daemon.getCurrentDisplayMode());
            // mode.id is -1 means it's invalid.
            return mode.id == -1 ? null : mode;
        } catch (RemoteException e) {
            Log.e(TAG, "native_getDisplayModes failed", e);
            reset();
        }
        return null;
    }

    public DisplayMode native_getDefaultDisplayMode() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getDefaultDisplayMode: no livedisplay IColor HAL!");
            return null;
        }
        try {
            DisplayMode mode = Utils.fromHIDLMode(daemon.getDefaultDisplayMode());
            // mode.id is -1 means it's invalid.
            return mode.id == -1 ? null : mode;
        } catch (RemoteException e) {
            Log.e(TAG, "native_getDefaultDisplayMode failed", e);
            reset();
        }
        return null;
    }

    public boolean native_setDisplayMode(DisplayMode mode, boolean makeDefault) {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_setDisplayMode: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.setDisplayMode(mode.id, makeDefault);
        } catch (RemoteException e) {
            Log.e(TAG, "native_setDisplayMode failed", e);
            reset();
        }
        return false;
    }

    public boolean native_setAdaptiveBacklightEnabled(boolean enabled) {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_setAdaptiveBacklightEnabled: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.setAdaptiveBacklightEnabled(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "native_setAdaptiveBacklightEnabled failed", e);
            reset();
        }
        return false;
    }

    public boolean native_isAdaptiveBacklightEnabled() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_isAdaptiveBacklightEnabled: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.isAdaptiveBacklightEnabled();
        } catch (RemoteException e) {
            Log.e(TAG, "native_isAdaptiveBacklightEnabled failed", e);
            reset();
        }
        return false;
    }

    public boolean native_setOutdoorModeEnabled(boolean enabled) {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_setOutdoorModeEnabled: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.setOutdoorModeEnabled(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "native_setOutdoorModeEnabled failed", e);
            reset();
        }
        return false;
    }

    public boolean native_isOutdoorModeEnabled() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_isOutdoorModeEnabled: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.isOutdoorModeEnabled();
        } catch (RemoteException e) {
            Log.e(TAG, "native_isOutdoorModeEnabled failed", e);
            reset();
        }
        return false;
    }

    public Range<Integer> native_getColorBalanceRange() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getColorBalanceRange: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLRange(daemon.getColorBalanceRange());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getColorBalanceRange failed", e);
            reset();
        }
        return null;
    }

    public int native_getColorBalance() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getColorBalance: no livedisplay IColor HAL!");
            return 0;
        }
        try {
            return daemon.getColorBalance();
        } catch (RemoteException e) {
            Log.e(TAG, "native_getColorBalance failed", e);
            reset();
        }
        return 0;
    }

    public boolean native_setColorBalance(int value) {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_setColorBalance: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.setColorBalance(value);
        } catch (RemoteException e) {
            Log.e(TAG, "native_setColorBalance failed", e);
            reset();
        }
        return false;
    }

    public boolean native_setPictureAdjustment(final HSIC hsic) {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_setPictureAdjustment: no livedisplay IColor HAL!");
            return false;
        }
        try {
            return daemon.setPictureAdjustment(Utils.toHIDLHSIC(hsic));
        } catch (RemoteException e) {
            Log.e(TAG, "native_setPictureAdjustment failed", e);
            reset();
        }
        return false;
    }

    public HSIC native_getPictureAdjustment() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getPictureAdjustment: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLHSIC(daemon.getPictureAdjustment());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getPictureAdjustment failed", e);
            reset();
        }
        return null;
    }

    public HSIC native_getDefaultPictureAdjustment() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getDefaultPictureAdjustment: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLHSIC(daemon.getDefaultPictureAdjustment());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getDefaultPictureAdjustment failed", e);
            reset();
        }
        return null;
    }

    public Range<Float> native_getHueRange() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getHueRange: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLIntRange(daemon.getHueRange());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getHueRange failed", e);
            reset();
        }
        return null;
    }

    public Range<Float> native_getSaturationRange() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getSaturationRange: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLRange(daemon.getSaturationRange());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getSaturationRange failed", e);
            reset();
        }
        return null;
    }

    public Range<Float> native_getIntensityRange() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getIntensityRange: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLRange(daemon.getIntensityRange());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getIntensityRange failed", e);
            reset();
        }
        return null;
    }

    public Range<Float> native_getContrastRange() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getContrastRange: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLRange(daemon.getContrastRange());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getContrastRange failed", e);
            reset();
        }
        return null;
    }

    public Range<Float> native_getSaturationThresholdRange() {
        IColor daemon = getColorService();
        if (daemon == null) {
            Log.e(TAG, "native_getSaturationThresholdRange: no livedisplay IColor HAL!");
            return null;
        }
        try {
            return Utils.fromHIDLRange(daemon.getSaturationThresholdRange());
        } catch (RemoteException e) {
            Log.e(TAG, "native_getSaturationThresholdRange failed", e);
            reset();
        }
        return null;
    }
}
